package org.ldv.davinci_bot.model.dao

import org.ldv.davinci_bot.model.entity.ModeleIA
import org.springframework.data.jpa.repository.JpaRepository

interface ModeleIADAO : JpaRepository<ModeleIA, Long> {
    fun existsByNom(nom: String): Boolean
}
