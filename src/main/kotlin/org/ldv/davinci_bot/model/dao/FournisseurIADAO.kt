package org.ldv.davinci_bot.model.dao

import org.ldv.davinci_bot.model.entity.FournisseurIA
import org.springframework.data.jpa.repository.JpaRepository

interface FournisseurIADAO : JpaRepository<FournisseurIA, Long> {
    fun findByNom(nom: String): FournisseurIA?
}
