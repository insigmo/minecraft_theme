package dev.minecraft.jetbrains.listeners

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import dev.minecraft.jetbrains.audio.AudioEvent
import dev.minecraft.jetbrains.audio.AudioService
import java.util.concurrent.atomic.AtomicLong

class TabSoundListener : FileEditorManagerListener {
    private val lastPlayedAt = AtomicLong(0L)

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if (event.oldFile == null || event.newFile == null || event.oldFile == event.newFile) return

        val now = System.currentTimeMillis()
        val prev = lastPlayedAt.get()
        if (now - prev < 120) return
        if (!lastPlayedAt.compareAndSet(prev, now)) return

        AudioService.getInstance().playEvent(AudioEvent.TabSwitch)
    }
}

