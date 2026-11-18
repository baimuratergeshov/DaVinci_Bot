package org.ldv.davinci_bot.model.dao

import org.ldv.davinci_bot.model.entity.Conversation
import org.ldv.davinci_bot.model.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageDAO : JpaRepository<Message, Long> {
    // Historique complet d'une conversation, du plus ancien au plus récent
    fun findByConversationOrderByIdAsc(conversation: Conversation): List<Message>
}
