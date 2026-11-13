package org.ldv.davinci_bot.model.vue

/** Élément du fil d'Ariane ; `url` vaut null pour la page courante. */
data class Lien(val libelle: String, val url: String? = null)

/** Modèle d'IA tel qu'affiché dans l'administration (données d'exemple en attendant la base). */
data class ModeleVue(
    val id: Long,
    val nom: String,
    val fournisseur: String,
    val identifiant: String,
    val urlApi: String,
    val description: String,
    val actif: Boolean,
    val ajouteLe: String,
    val logo: String
)

data class ConversationVue(val id: Long, val titre: String, val modele: String, val fournisseur: String)

data class MessageVue(val utilisateur: Boolean, val contenu: String = "", val lignes: List<String> = emptyList())

/** Données d'exemple reprises de la maquette, à remplacer par les dépôts JPA. */
object DonneesExemple {
    val fournisseurs = listOf("Google", "Mistral AI", "OpenAI", "Anthropic", "Local")

    val modeles = listOf(
        ModeleVue(1, "Gemini Flash", "Google", "gemini-flash", "https://generativelanguage.googleapis.com/v1beta",
            "Modèle rapide, adapté aux questions courantes et aux explications de cours.", true, "10/11/2026", "/img/logos/gemini.svg"),
        ModeleVue(2, "Mistral Small", "Mistral AI", "mistral-small", "https://api.mistral.ai/v1",
            "Modèle européen léger, bon pour les résumés.", true, "10/11/2026", "/img/logos/mistral.svg"),
        ModeleVue(3, "GPT Mini", "OpenAI", "gpt-mini", "https://api.openai.com/v1",
            "Modèle polyvalent et économique.", true, "10/11/2026", "/img/logos/openai.svg"),
        ModeleVue(4, "Claude Haiku", "Anthropic", "claude-haiku", "https://api.anthropic.com/v1",
            "Modèle rapide pour les échanges courts.", true, "10/11/2026", "/img/logos/claude.svg"),
        ModeleVue(5, "Llama (serveur local)", "Local", "llama-local", "http://localhost:11434",
            "Modèle hébergé sur le serveur du lycée.", false, "10/11/2026", "/img/logos/meta-llama.svg")
    )

    val conversations = listOf(
        ConversationVue(1, "Aide pour le TP SLAM", "Gemini Flash", "Google"),
        ConversationVue(2, "Résumé du cours de réseaux", "Mistral Small", "Mistral AI"),
        ConversationVue(3, "Correction SQL jointures", "GPT Mini", "OpenAI"),
        ConversationVue(4, "Idées de nom de projet", "Gemini Flash", "Google"),
        ConversationVue(5, "Explication du MVC", "Mistral Small", "Mistral AI")
    )

    val messages = listOf(
        MessageVue(true, "Peux-tu m’expliquer les étapes du TP pour créer l’appli ?"),
        MessageVue(false, lignes = listOf(
            "Voici les étapes principales du projet :",
            "<strong>1. Analyse :</strong> acteurs, fonctionnalités et diagramme de cas d’utilisation.",
            "<strong>2. Conception :</strong> diagramme de classes puis ERD de la base.",
            "<strong>3. Maquette :</strong> charte graphique, écrans Figma et sitemap.",
            "Dis-moi si tu veux que je détaille une étape."
        ))
    )
}
