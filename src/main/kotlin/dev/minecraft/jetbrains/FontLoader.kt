// src/main/kotlin/dev/minecraft/jetbrains/FontLoader.kt
package dev.minecraft.jetbrains

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import java.awt.Font
import java.awt.GraphicsEnvironment

@Service(Service.Level.APP)
class FontLoader {

    private val log = logger<FontLoader>()

    init {
        loadFont()
    }

    private fun loadFont() {
        try {
            val stream = javaClass.getResourceAsStream("/fonts/Miracode.ttf")
                ?: run { log.warn("Miracode.ttf not found in resources"); return }

            val font = Font.createFont(Font.TRUETYPE_FONT, stream)
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            ge.registerFont(font)

            log.info("Miracode font registered successfully")
        } catch (e: Exception) {
            log.warn("Failed to register Miracode font", e)
        }
    }

    companion object {
        fun getInstance(): FontLoader =
            com.intellij.openapi.application.ApplicationManager
                .getApplication()
                .getService(FontLoader::class.java)
    }
}