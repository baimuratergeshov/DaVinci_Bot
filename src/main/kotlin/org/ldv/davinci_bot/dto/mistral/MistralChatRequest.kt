package org.ldv.davinci_bot.dto.mistral

data class MistralChatRequest(
    val model: String,
    val messages: List<MistralMessage>
)
