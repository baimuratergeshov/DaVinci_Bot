package org.ldv.davinci_bot.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class ModeleIA(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    var nom: String,
    var identifiantTechnique: String,
    var urlApi: String,
    var description: String,
    var cheminLogo: String,
    @ManyToOne(optional = false)
    var fournisseurIA: FournisseurIA
)
