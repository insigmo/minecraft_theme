package dev.minecraft.jetbrains.statusbar

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import dev.minecraft.jetbrains.Constants
import dev.minecraft.jetbrains.audio.AudioService
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.Icon
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI

class MuteStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = Constants.STATUSBAR_WIDGET_ID

    override fun getDisplayName(): String = "Minecraft Audio"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget = MuteStatusBarWidget()

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true

    private class MuteStatusBarWidget : CustomStatusBarWidget, Disposable {

        private val audioService = AudioService.getInstance()

        private val label = JBLabel().apply {
            border = JBUI.Borders.empty(0, 6)
        }

        private val muteListener: (Boolean) -> Unit = { muted -> updateLabel(muted) }

        init {
            updateLabel(audioService.isMuted())
            audioService.addMuteListener(muteListener)
            label.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    updateLabel(audioService.toggleMuted())
                }
            })
        }

        override fun ID(): String = Constants.STATUSBAR_WIDGET_ID

        override fun install(statusBar: StatusBar) {}

        override fun getComponent() = label

        private fun updateLabel(muted: Boolean) {
            label.icon = if (muted) ICON_MUTED else ICON_ON
            label.toolTipText = if (muted) "MC: Muted — click to unmute" else "MC: On — click to mute"
        }

        override fun dispose() {
            audioService.removeMuteListener(muteListener)
        }

        companion object {
            private val ICON_ON    = IconLoader.getIcon("/icons/mc_sound_on.png",    MuteStatusBarWidget::class.java)
            private val ICON_MUTED = IconLoader.getIcon("/icons/mc_sound_muted.png", MuteStatusBarWidget::class.java)
        }
    }
}
