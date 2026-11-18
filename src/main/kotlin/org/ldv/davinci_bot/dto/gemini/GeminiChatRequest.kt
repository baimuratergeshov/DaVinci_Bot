package org.ldv.davinci_bot.dto.gemini

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GeminiChatRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null,
    // Outils, ex. la recherche Google
    val tools: List<Map<String, Any>>? = null
)
