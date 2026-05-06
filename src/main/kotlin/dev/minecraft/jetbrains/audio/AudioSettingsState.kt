package dev.minecraft.jetbrains.audio

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "MinecraftAudioSettings", storages = [Storage("minecraft-audio.xml")])
@Service(Service.Level.APP)
class AudioSettingsState : PersistentStateComponent<AudioSettingsState.State> {
    data class State(
        var muted: Boolean = false,
        var soundEnabled: Boolean = true,
        var musicEnabled: Boolean = true,
        var runDebugSoundsEnabled: Boolean = true,
        var tabSwitchSoundEnabled: Boolean = true,
        var soundVolume: Int = 50,
        var musicVolume: Int = 50
    )

    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): AudioSettingsState =
            ApplicationManager.getApplication().getService(AudioSettingsState::class.java)
    }
}

