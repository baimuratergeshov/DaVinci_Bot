package org.ldv.davinci_bot.dto.claude

data class ClaudeChatResponse(
    val content: List<ClaudeContent>? = null
)

data class ClaudeContent(
    val type: String? = null,
    val text: String? = null
)
