package org.ldv.davinci_bot.model.dao

import org.ldv.davinci_bot.model.entity.Conversation
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationDAO : JpaRepository<Conversation, Long>
