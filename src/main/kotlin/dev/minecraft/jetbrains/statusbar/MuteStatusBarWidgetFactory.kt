package dev.minecraft.jetbrains.statusbar

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.ui.ClickListener
import com.intellij.ui.components.JBLabel
import dev.minecraft.jetbrains.Constants
import dev.minecraft.jetbrains.audio.AudioService
import java.awt.event.MouseEvent
import javax.swing.JComponent

class MuteStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = Constants.STATUSBAR_WIDGET_ID

    override fun getDisplayName(): String = "Minecraft Audio Mute"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget = MuteStatusBarWidget()

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}

private class MuteStatusBarWidget : CustomStatusBarWidget, Disposable {
    private val audioService = AudioService.getInstance()
    private val label = JBLabel().apply {
        border = com.intellij.util.ui.JBUI.Borders.empty(0, 6)
        toolTipText = "Toggle Minecraft audio mute"
    }

    private val muteListener: (Boolean) -> Unit = { updateLabel(it) }

    init {
        updateLabel(audioService.isMuted())
        audioService.addMuteListener(muteListener)
        object : ClickListener() {
            override fun onClick(event: MouseEvent, clickCount: Int): Boolean {
                val muted = audioService.toggleMuted()
                updateLabel(muted)
                return true
            }
        }.installOn(label)
    }

    override fun ID(): String = Constants.STATUSBAR_WIDGET_ID

    override fun install(statusBar: StatusBar) = Unit

    override fun getComponent(): JComponent = label

    private fun updateLabel(muted: Boolean) {
        label.text = if (muted) "MC: Muted" else "MC: On"
    }

    override fun dispose() {
        audioService.removeMuteListener(muteListener)
    }
}

