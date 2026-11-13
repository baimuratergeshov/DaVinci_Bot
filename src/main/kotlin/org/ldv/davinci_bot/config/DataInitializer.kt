package org.ldv.davinci_bot.config

import org.ldv.davinci_bot.model.dao.FournisseurIADAO
import org.ldv.davinci_bot.model.dao.ModeleIADAO
import org.ldv.davinci_bot.model.entity.FournisseurIA
import org.ldv.davinci_bot.model.entity.ModeleIA
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer(
    private val fournisseurIADAO: FournisseurIADAO,
    private val modeleIADAO: ModeleIADAO
) {

    @Bean
    fun initData() = CommandLineRunner {

        // Crée le fournisseur seulement s'il n'existe pas déjà
        val mistral = fournisseurIADAO.findByNom("Mistral")
            ?: fournisseurIADAO.save(
                FournisseurIA(
                    nom = "Mistral",
                    cheminLogo = "/img/logos/mistral.svg",
                    description = "Fournisseur français de modèles d'intelligence artificielle."
                )
            )
        val gemini = fournisseurIADAO.findByNom("Gemini")
            ?: fournisseurIADAO.save(
                FournisseurIA(
                    nom = "Gemini",
                    cheminLogo = "/img/logos/gemini.svg",
                    description = "Modèles d'intelligence artificielle de Google."
                )
            )

        // Modèles à initialiser
        val modeles = listOf(
            ModeleIA(
                nom = "gemini-3.8-flash",
                identifiantTechnique = "gemini-3.8-flash",
                urlApi = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent",
                description = "Modèle rapide et polyvalent.",
                cheminLogo = "/img/logos/gemini.svg",
                fournisseurIA = gemini
            ),
            ModeleIA(
                nom = "gemini-3.5-flash-lite",
                identifiantTechnique = "gemini-3.5-flash-lite",
                urlApi = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent",
                description = "Modèle rapide pour les tâches simples.",
                cheminLogo = "/img/logos/gemini.svg",
                fournisseurIA = gemini
            ),
            ModeleIA(
                nom = "mistral-small-latest",
                identifiantTechnique = "mistral-small-latest",
                urlApi = "https://api.mistral.ai/v1/chat/completions",
                description = "Modèle léger pour les conversations et les tâches courantes.",
                cheminLogo = "/img/logos/mistral.svg",
                fournisseurIA = mistral
            ),
            ModeleIA(
                nom = "mistral-medium-latest",
                identifiantTechnique = "mistral-medium-latest",
                urlApi = "https://api.mistral.ai/v1/chat/completions",
                description = "Modèle plus avancé pour les tâches complexes.",
                cheminLogo = "/img/logos/mistral.svg",
                fournisseurIA = mistral
            )
        )

        // Évite de recréer les modèles à chaque démarrage
        modeles
            .filterNot { modeleIADAO.existsByNom(it.nom) }
            .forEach { modeleIADAO.save(it) }
    }
}
