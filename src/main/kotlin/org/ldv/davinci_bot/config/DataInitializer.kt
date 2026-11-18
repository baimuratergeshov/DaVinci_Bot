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

        val anthropic = fournisseurIADAO.findByNom("Anthropic")
            ?: fournisseurIADAO.save(
                FournisseurIA(
                    nom = "Anthropic",
                    cheminLogo = "/img/logos/claude.svg",
                    description = "Modèles d'intelligence artificielle Claude."
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
                fournisseurIA = gemini,
                prompt = PROMPT_PAR_DEFAUT
            ),
            ModeleIA(
                nom = "gemini-3.5-flash-lite",
                identifiantTechnique = "gemini-3.5-flash-lite",
                urlApi = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent",
                description = "Modèle rapide pour les tâches simples.",
                cheminLogo = "/img/logos/gemini.svg",
                fournisseurIA = gemini,
                prompt = PROMPT_PAR_DEFAUT
            ),
            ModeleIA(
                nom = "mistral-small-latest",
                identifiantTechnique = "mistral-small-latest",
                urlApi = "https://api.mistral.ai/v1/chat/completions",
                description = "Modèle léger pour les conversations et les tâches courantes.",
                cheminLogo = "/img/logos/mistral.svg",
                fournisseurIA = mistral,
                prompt = PROMPT_PAR_DEFAUT
            ),
            ModeleIA(
                nom = "mistral-medium-latest",
                identifiantTechnique = "mistral-medium-latest",
                urlApi = "https://api.mistral.ai/v1/chat/completions",
                description = "Modèle plus avancé pour les tâches complexes.",
                cheminLogo = "/img/logos/mistral.svg",
                fournisseurIA = mistral,
                prompt = PROMPT_PAR_DEFAUT
            ),
            ModeleIA(
                nom = "claude-haiku-5-5",
                identifiantTechnique = "claude-haiku-5-5",
                urlApi = "https://api.anthropic.com/v1/messages",
                description = "Modèle rapide et économique d'Anthropic.",
                cheminLogo = "/img/logos/claude.svg",
                fournisseurIA = anthropic,
                prompt = PROMPT_PAR_DEFAUT
            )
        )

        // Évite de recréer les modèles à chaque démarrage
        modeles
            .filterNot { modeleIADAO.existsByNom(it.nom) }
            .forEach { modeleIADAO.save(it) }

        // Renseigne le prompt des modèles déjà en base qui n'en ont pas encore
        modeleIADAO.findAll().filter { it.prompt.isNullOrBlank() }.forEach {
            it.prompt = PROMPT_PAR_DEFAUT
            modeleIADAO.save(it)
        }

        // Corrige les logos des modèles déjà en base (anciens chemins /img/*.png)
        val existants = modeleIADAO.findAll().associateBy { it.nom }
        modeles.forEach { seed ->
            existants[seed.nom]
                ?.takeIf { it.cheminLogo != seed.cheminLogo }
                ?.let {
                    it.cheminLogo = seed.cheminLogo
                    modeleIADAO.save(it)
                }
        }
    }

    companion object {
        const val PROMPT_PAR_DEFAUT =
            "Tu es DaVinci Bot, l'assistant des élèves du lycée. Réponds en français, de façon claire et pédagogique."
    }
}
