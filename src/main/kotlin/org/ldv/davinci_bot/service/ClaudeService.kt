package org.ldv.davinci_bot.service

import org.ldv.davinci_bot.dto.claude.ClaudeChatRequest
import org.ldv.davinci_bot.dto.claude.ClaudeChatResponse
import org.ldv.davinci_bot.dto.claude.ClaudeMessage
import org.ldv.davinci_bot.dto.mistral.IAService
import org.ldv.davinci_bot.model.entity.Message
import org.ldv.davinci_bot.model.entity.ModeleIA
import org.ldv.davinci_bot.model.entity.TypeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class ClaudeService(
    private val restClientBuilder: RestClient.Builder,

    @Value("\${claude.api.key}")
    private val apiKey: String

) : IAService {
    override fun getFournisseur(): String = "Anthropic"

    override fun envoyerPrompt(historique: List<Message>, modele: ModeleIA): String {

        // Étape 1 : Historique de la conversation (le prompt système est un champ à part)
        val messages = historique.map {
            ClaudeMessage(
                role = if (it.typeMessage == TypeMessage.ASSISTANT) "assistant" else "user",
                content = it.contenu
            )
        }

        // Étape 2 : Préparation de la requête
        val request = ClaudeChatRequest(
            model = modele.identifiantTechnique,
            max_tokens = 2048,
            system = modele.prompt?.takeIf { it.isNotBlank() },
            messages = messages,
            // Recherche web côté serveur : Claude cherche seul quand c'est utile
            tools = listOf(mapOf(
                "type" to "web_search_20250305",
                "name" to "web_search",
                "max_uses" to 3
            ))
        )

        // Étape 3 : Envoi de la requête HTTP POST à l'API
        val response = restClientBuilder.build().post()
            .uri(modele.urlApi)
            .header("x-api-key", apiKey)
            .header("anthropic-version", "2023-06-01")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(ClaudeChatResponse::class.java)

        // Étape 4 : Récupération du texte de la réponse
        return response?.content
            ?.mapNotNull { it.text }
            ?.joinToString("")
            ?.takeIf { it.isNotBlank() }
            ?: "Aucune réponse reçue de Claude."
    }
}
