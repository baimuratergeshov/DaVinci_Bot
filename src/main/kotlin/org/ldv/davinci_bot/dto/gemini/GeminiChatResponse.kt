package org.ldv.davinci_bot.dto.gemini

data class GeminiChatResponse(
    val candidates: List<GeminiCandidate>? = null
)
