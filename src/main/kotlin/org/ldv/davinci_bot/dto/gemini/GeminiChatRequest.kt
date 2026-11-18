package org.ldv.davinci_bot.dto.gemini

data class GeminiChatRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)
