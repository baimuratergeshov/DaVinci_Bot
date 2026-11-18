package org.ldv.davinci_bot.controller

import org.ldv.davinci_bot.model.dao.ConversationDAO
import org.ldv.davinci_bot.model.dao.MessageDAO
import org.ldv.davinci_bot.model.dao.ModeleIADAO
import org.ldv.davinci_bot.service.IAServiceFactory
import org.ldv.davinci_bot.model.entity.Conversation
import org.ldv.davinci_bot.model.entity.Message
import org.ldv.davinci_bot.model.entity.TypeMessage
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Controller
class ChatController(
    private val iaServiceFactory: IAServiceFactory,
    private val conversationDAO: ConversationDAO,
    private val messageDAO: MessageDAO,
    private val modeleIADAO: ModeleIADAO
) {

    // Page d'accueil : liste des conversations
    @GetMapping("/")
    fun accueil(model: Model): String {

        // Retire la conversation courante
        model.addAttribute("conversation", null)

        model.addAttribute("modeles", modeleIADAO.findAll())

        model.addAttribute("messages", emptyList<Message>())

        model.addAttribute(
            "conversations",
            conversationDAO.findAllByOrderByDateDerniereModificationDesc()
        )

        return "utilisateur/chat"
    }

    // Création d'une nouvelle conversation
    @PostMapping("/conversations")
    fun creerConversation(
        @RequestParam prompt: String,
        @RequestParam idModele: Long
    ): String {

        // Récupération du modèle sélectionné
        val modele = modeleIADAO.findById(idModele)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Modèle IA introuvable"
                )
            }

        // Création de la conversation
        val conversation = Conversation(
            titre = prompt.take(50),
            dateCreation = LocalDateTime.now(),
            modeleIA = modele
        )

        val conversationEnregistree = conversationDAO.save(conversation)

        // Enregistrement du premier message
        val messageUtilisateur = Message(
            contenu = prompt,
            dateCreation = LocalDateTime.now(),
            typeMessage = TypeMessage.UTILISATEUR,
            conversation = conversationEnregistree
        )

        messageDAO.save(messageUtilisateur)

        repondre(conversationEnregistree)

        conversationEnregistree.dateDerniereModification = LocalDateTime.now()
        conversationDAO.save(conversationEnregistree)

        return "redirect:/conversations/${conversationEnregistree.id}"
    }

    // Affichage d'une conversation
    @GetMapping("/conversations/{id}")
    fun afficherConversation(
        @PathVariable id: Long,
        model: Model
    ): String {

        val conversation = conversationDAO.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        model.addAttribute(
            "conversations",
            conversationDAO.findAllByOrderByDateDerniereModificationDesc()
        )

        model.addAttribute("conversation", conversation)

        model.addAttribute(
            "messages",
            conversation.messages.sortedBy { it.dateCreation }
        )

        return "utilisateur/chat"
    }

    // Envoi d'un nouveau message dans une conversation existante
    @PostMapping("/conversations/{id}/messages")
    fun envoyerMessage(
        @PathVariable id: Long,
        @RequestParam prompt: String
    ): String {

        val conversation = conversationDAO.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        // Enregistrement du message utilisateur
        val messageUtilisateur = Message(
            contenu = prompt,
            dateCreation = LocalDateTime.now(),
            typeMessage = TypeMessage.UTILISATEUR,
            conversation = conversation
        )

        messageDAO.save(messageUtilisateur)

        repondre(conversation)

        conversation.dateDerniereModification = LocalDateTime.now()
        conversationDAO.save(conversation)

        return "redirect:/conversations/$id"
    }

    // Interroge l'IA avec la mémoire de la conversation et enregistre sa réponse
    private fun repondre(conversation: Conversation) {
        val modele = conversation.modeleIA
        val contenu = try {
            // Mémoire : les derniers messages de la conversation, pour que l'IA se souvienne
            val historique = messageDAO.findByConversationOrderByIdAsc(conversation)
                .filter { it.typeMessage != TypeMessage.SYSTEME }
                .takeLast(TAILLE_MEMOIRE)
            iaServiceFactory.getService(modele).envoyerPrompt(historique, modele)
        } catch (e: Exception) {
            println("Erreur lors de l'appel au modèle ${modele.nom} : ${e.message}")
            "⚠️ Le modèle n'a pas pu répondre (${e.message}). Réessayez."
        }
        messageDAO.save(
            Message(
                contenu = contenu,
                dateCreation = LocalDateTime.now(),
                typeMessage = TypeMessage.ASSISTANT,
                conversation = conversation
            )
        )
    }

    companion object {
        // Nombre maximum de messages renvoyés à l'IA à chaque échange
        private const val TAILLE_MEMOIRE = 30
    }
}
