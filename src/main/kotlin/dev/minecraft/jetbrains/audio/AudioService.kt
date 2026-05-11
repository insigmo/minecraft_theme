package dev.minecraft.jetbrains.audio

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import dev.minecraft.jetbrains.Constants
import java.awt.Toolkit
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

@Service(Service.Level.APP)
class AudioService : Disposable {
    private val settings: AudioSettingsState = AudioSettingsState.getInstance()
    private val muteListeners = CopyOnWriteArrayList<(Boolean) -> Unit>()
    private var musicClip: Clip? = null
    private var musicPlaylist: List<String> = emptyList()
    private var currentTrackIndex: Int = 0
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {
        restartMusicIfNeeded()
    }

    fun isMuted(): Boolean = settings.state.muted

    fun setMuted(muted: Boolean) {
        settings.state.muted = muted
        if (muted) {
            stopMusic()
        } else {
            restartMusicIfNeeded()
        }
        muteListeners.forEach { it(muted) }
    }

    fun toggleMuted(): Boolean {
        val next = !isMuted()
        setMuted(next)
        return next
    }

    fun addMuteListener(listener: (Boolean) -> Unit) {
        muteListeners.add(listener)
    }

    fun removeMuteListener(listener: (Boolean) -> Unit) {
        muteListeners.remove(listener)
    }

    fun playEvent(event: AudioEvent) {
        if (settings.state.muted || !settings.state.soundEnabled) return

        val path = when (event) {
            AudioEvent.TabSwitch -> {
                if (!settings.state.tabSwitchSoundEnabled) return
                Constants.TAB_SWITCH_SOUND
            }
            AudioEvent.RunStart -> {
                if (!settings.state.runDebugSoundsEnabled) return
                Constants.RUN_START_SOUND
            }
            AudioEvent.DebugStart -> {
                if (!settings.state.runDebugSoundsEnabled) return
                Constants.DEBUG_START_SOUND
            }
            AudioEvent.RunStop -> {
                if (!settings.state.runDebugSoundsEnabled) return
                Constants.RUN_STOP_SOUND
            }
            AudioEvent.DebugStop -> {
                if (!settings.state.runDebugSoundsEnabled) return
                Constants.DEBUG_STOP_SOUND
            }
            AudioEvent.Error -> Constants.ERROR_SOUND
        }

        playOnce(path, settings.state.soundVolume)
    }

    fun restartMusicIfNeeded() {
        stopMusic()
        if (settings.state.muted || !settings.state.musicEnabled) return

        musicPlaylist = findAllMusicResources()
        if (musicPlaylist.isEmpty()) return

        currentTrackIndex = 0
        playNextTrack()
    }

    fun stopMusic() {
        musicClip?.let { clip ->
            runCatching {
                clip.stop()
                clip.close()
            }
        }
        musicClip = null
    }

    private fun playOnce(resourcePath: String, volumePercent: Int) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val clip = openClip(resourcePath, volumePercent)
            if (clip == null) {
                runCatching { Toolkit.getDefaultToolkit().beep() }
                return@executeOnPooledThread
            }
            runCatching {
                clip.addLineListener { event ->
                    if (event.type.toString() == "STOP") {
                        clip.close()
                    }
                }
                clip.start()
            }.onFailure {
                runCatching { clip.close() }
            }
        }
    }

    private fun openClip(resourcePath: String, volumePercent: Int): Clip? {
        val url = javaClass.getResource(resourcePath) ?: return null
        return runCatching {
            AudioSystem.getAudioInputStream(url).use { input ->
                val clip = AudioSystem.getClip()
                clip.open(input)
                setClipVolume(clip, volumePercent)
                clip
            }
        }.getOrNull()
    }

    private fun findAllMusicResources(): List<String> {
        val musicFiles = mutableListOf<String>()

        var index = 1
        while (true) {
            val path = "/audio/music/background_$index.wav"
            if (javaClass.getResource(path) != null) {
                musicFiles.add(path)
                index++
            } else {
                break
            }
        }

        if (musicFiles.isEmpty() && javaClass.getResource(Constants.MUSIC_LOOP) != null) {
            musicFiles.add(Constants.MUSIC_LOOP)
        }

        return musicFiles
    }

    private fun playNextTrack() {
        if (musicPlaylist.isEmpty()) return
        if (settings.state.muted || !settings.state.musicEnabled) return

        val trackPath = musicPlaylist[currentTrackIndex]
        musicClip = openClip(trackPath, settings.state.musicVolume)?.also { clip ->
            clip.addLineListener { event ->
                if (event.type.toString() == "STOP") {
                    clip.close()
                    currentTrackIndex = (currentTrackIndex + 1) % musicPlaylist.size
                    // Ждём 5 секунд перед следующим треком
                    scheduler.schedule(
                        {
                            ApplicationManager.getApplication().executeOnPooledThread {
                                playNextTrack()
                            }
                        },
                        5L,
                        TimeUnit.SECONDS
                    )
                }
            }
            clip.start()
        }
    }

    private fun setClipVolume(clip: Clip, volumePercent: Int) {
        val gain = clip.getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl ?: return
        val clamped = volumePercent.coerceIn(0, 100)
        val min = gain.minimum
        val max = gain.maximum
        val value = min + (max - min) * (clamped / 100.0f)
        gain.value = value
    }

    override fun dispose() {
        stopMusic()
        scheduler.shutdown()
    }

    companion object {
        fun getInstance(): AudioService =
            ApplicationManager.getApplication().getService(AudioService::class.java)
    }
}
