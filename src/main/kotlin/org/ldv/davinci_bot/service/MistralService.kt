package org.ldv.davinci_bot.service

import org.ldv.davinci_bot.dto.mistral.IAService
import org.ldv.davinci_bot.dto.mistral.MistralChatRequest
import org.ldv.davinci_bot.dto.mistral.MistralChatResponse
import org.ldv.davinci_bot.dto.mistral.MistralMessage
import org.ldv.davinci_bot.model.entity.Message
import org.ldv.davinci_bot.model.entity.ModeleIA
import org.ldv.davinci_bot.model.entity.TypeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class MistralService(
    private val restClientBuilder: RestClient.Builder,

    @Value("\${mistral.api.key}")
    private val apiKey: String

) : IAService {
    override fun getFournisseur(): String = "Mistral"

    override fun envoyerPrompt(historique: List<Message>, modele: ModeleIA): String {

        // Étape 1 : Prompt système du modèle, puis historique de la conversation
        val messages = mutableListOf<MistralMessage>()
        modele.prompt?.takeIf { it.isNotBlank() }
            ?.let { messages.add(MistralMessage(role = "system", content = it)) }
        historique.forEach {
            messages.add(
                MistralMessage(
                    role = if (it.typeMessage == TypeMessage.ASSISTANT) "assistant" else "user",
                    content = it.contenu
                )
            )
        }

        // Étape 2 : Préparation de la requête pour Mistral
        val request = MistralChatRequest(
            model = modele.identifiantTechnique,
            messages = messages
        )

        // Étape 3 : Envoi de la requête HTTP POST à l'API
        val client = restClientBuilder.build()

        val response = client.post()
            .uri(modele.urlApi)
            .header("Authorization", "Bearer $apiKey")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(MistralChatResponse::class.java)

        // Étape 4 : Récupération du texte de la réponse
        val choix = response?.choices?.firstOrNull()
        val reponse = choix?.message?.content

        if (reponse != null) {
            return reponse
        } else {
            return "Aucune réponse reçue de Mistral."
        }
    }

}
