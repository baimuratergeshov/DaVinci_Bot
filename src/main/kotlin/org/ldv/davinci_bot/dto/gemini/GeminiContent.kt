package org.ldv.davinci_bot.dto.gemini

data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)
