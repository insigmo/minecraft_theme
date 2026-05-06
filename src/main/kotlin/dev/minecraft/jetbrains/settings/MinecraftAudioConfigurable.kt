package dev.minecraft.jetbrains.settings

import com.intellij.openapi.options.Configurable
import dev.minecraft.jetbrains.audio.AudioService
import dev.minecraft.jetbrains.audio.AudioSettingsState
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider

class MinecraftAudioConfigurable : Configurable {
    private var panel: JPanel? = null
    private lateinit var muted: JCheckBox
    private lateinit var sfxEnabled: JCheckBox
    private lateinit var musicEnabled: JCheckBox
    private lateinit var runDebugEnabled: JCheckBox
    private lateinit var tabSoundEnabled: JCheckBox
    private lateinit var sfxVolume: JSlider
    private lateinit var musicVolume: JSlider

    override fun getDisplayName(): String = "Minecraft Theme Audio"

    override fun createComponent(): JComponent {
        val settings = AudioSettingsState.getInstance().state
        panel = JPanel(GridBagLayout())
        var row = 0

        muted = JCheckBox("Mute all plugin audio", settings.muted)
        sfxEnabled = JCheckBox("Enable sound effects", settings.soundEnabled)
        musicEnabled = JCheckBox("Enable background music", settings.musicEnabled)
        runDebugEnabled = JCheckBox("Enable Run/Debug sounds", settings.runDebugSoundsEnabled)
        tabSoundEnabled = JCheckBox("Enable tab switch sound", settings.tabSwitchSoundEnabled)
        sfxVolume = JSlider(0, 100, settings.soundVolume)
        musicVolume = JSlider(0, 100, settings.musicVolume)

        addRow(muted, row++)
        addRow(sfxEnabled, row++)
        addRow(musicEnabled, row++)
        addRow(runDebugEnabled, row++)
        addRow(tabSoundEnabled, row++)
        addRow(JLabel("SFX volume"), row++)
        addRow(sfxVolume, row++)
        addRow(JLabel("Music volume"), row++)
        addRow(musicVolume, row)

        return panel!!
    }

    override fun isModified(): Boolean {
        val state = AudioSettingsState.getInstance().state
        return muted.isSelected != state.muted ||
            sfxEnabled.isSelected != state.soundEnabled ||
            musicEnabled.isSelected != state.musicEnabled ||
            runDebugEnabled.isSelected != state.runDebugSoundsEnabled ||
            tabSoundEnabled.isSelected != state.tabSwitchSoundEnabled ||
            sfxVolume.value != state.soundVolume ||
            musicVolume.value != state.musicVolume
    }

    override fun apply() {
        val state = AudioSettingsState.getInstance().state
        state.muted = muted.isSelected
        state.soundEnabled = sfxEnabled.isSelected
        state.musicEnabled = musicEnabled.isSelected
        state.runDebugSoundsEnabled = runDebugEnabled.isSelected
        state.tabSwitchSoundEnabled = tabSoundEnabled.isSelected
        state.soundVolume = sfxVolume.value
        state.musicVolume = musicVolume.value

        val audioService = AudioService.getInstance()
        audioService.setMuted(state.muted)
        audioService.restartMusicIfNeeded()
    }

    override fun reset() {
        val state = AudioSettingsState.getInstance().state
        muted.isSelected = state.muted
        sfxEnabled.isSelected = state.soundEnabled
        musicEnabled.isSelected = state.musicEnabled
        runDebugEnabled.isSelected = state.runDebugSoundsEnabled
        tabSoundEnabled.isSelected = state.tabSwitchSoundEnabled
        sfxVolume.value = state.soundVolume
        musicVolume.value = state.musicVolume
    }

    override fun disposeUIResources() {
        panel = null
    }

    private fun addRow(component: JComponent, row: Int) {
        panel?.add(
            component,
            GridBagConstraints().apply {
                gridx = 0
                gridy = row
                weightx = 1.0
                fill = GridBagConstraints.HORIZONTAL
                anchor = GridBagConstraints.WEST
            }
        )
    }
}

