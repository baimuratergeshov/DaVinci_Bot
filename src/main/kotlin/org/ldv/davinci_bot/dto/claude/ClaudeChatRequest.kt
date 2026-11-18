package org.ldv.davinci_bot.dto.claude

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClaudeChatRequest(
    val model: String,
    val max_tokens: Int,
    val system: String? = null,
    val messages: List<ClaudeMessage>,
    // Outils côté serveur, ex. la recherche web
    val tools: List<Map<String, Any>>? = null
)
