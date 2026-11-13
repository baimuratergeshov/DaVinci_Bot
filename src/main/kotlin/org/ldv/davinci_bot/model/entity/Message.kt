package org.ldv.davinci_bot.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    @Column(columnDefinition = "TEXT", nullable = false)
    var contenu: String,
    var dateCreation: LocalDateTime,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var typeMessage: TypeMessage,
    @ManyToOne(optional = false)
    var conversation: Conversation
)
