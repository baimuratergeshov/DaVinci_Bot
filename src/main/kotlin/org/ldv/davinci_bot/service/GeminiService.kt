package org.ldv.davinci_bot.service

import org.ldv.davinci_bot.dto.gemini.GeminiChatRequest
import org.ldv.davinci_bot.dto.gemini.GeminiChatResponse
import org.ldv.davinci_bot.dto.gemini.GeminiContent
import org.ldv.davinci_bot.dto.gemini.GeminiPart
import org.ldv.davinci_bot.dto.mistral.IAService
import org.ldv.davinci_bot.model.entity.ModeleIA
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class GeminiService(
    private val restClientBuilder: RestClient.Builder,

    @Value("\${gemini.api.key}")
    private val apiKey: String

) : IAService {
    override fun getFournisseur(): String = "Gemini"

    override fun envoyerPrompt(prompt: String, modele: ModeleIA): String {

        // Étape 1 : Création du contenu de l'utilisateur
        val contenu = GeminiContent(
            role = "user",
            parts = listOf(GeminiPart(text = prompt))
        )

        // Étape 2 : Préparation de la requête pour Gemini
        val request = GeminiChatRequest(
            contents = listOf(contenu)
        )

        // Étape 3 : Envoi de la requête HTTP POST à l'API
        val client = restClientBuilder.build()

        val response = client.post()
            .uri(modele.urlApi)
            .header("x-goog-api-key", apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(GeminiChatResponse::class.java)

        // Étape 4 : Récupération du texte de la réponse
        val candidat = response?.candidates?.firstOrNull()
        val reponse = candidat?.content?.parts
            ?.mapNotNull { it.text }
            ?.joinToString("")
            ?.takeIf { it.isNotBlank() }

        if (reponse != null) {
            return reponse
        } else {
            return "Aucune réponse reçue de Gemini."
        }
    }
}
