package dev.minecraft.jetbrains.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import dev.minecraft.jetbrains.audio.AudioEvent
import dev.minecraft.jetbrains.audio.AudioService

class RunDebugSoundListener : ExecutionListener {
    override fun processStarted(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler
    ) {
        val audioService = AudioService.getInstance()
        if (isDebug(executorId)) {
            audioService.playEvent(AudioEvent.DebugStart)
        } else {
            audioService.playEvent(AudioEvent.RunStart)
        }
    }

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        val audioService = AudioService.getInstance()
        if (isDebug(executorId)) {
            audioService.playEvent(AudioEvent.DebugStop)
        } else {
            audioService.playEvent(AudioEvent.RunStop)
        }
        if (exitCode != 0) {
            audioService.playEvent(AudioEvent.Error)
        }
    }

    override fun processNotStarted(executorId: String, env: ExecutionEnvironment) {
        AudioService.getInstance().playEvent(AudioEvent.Error)
    }

    private fun isDebug(executorId: String): Boolean =
        executorId.equals("Debug", ignoreCase = true) ||
            executorId.lowercase().contains("debug")
}

