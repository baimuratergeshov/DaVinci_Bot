package org.ldv.davinci_bot.controller

import org.ldv.davinci_bot.model.vue.DonneesExemple
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ChatBotController {

    @GetMapping("/")
    fun accueil(): String = "accueil"

    // Formulaire de connexion : affichage uniquement, pas encore fonctionnel
    @GetMapping("/login")
    fun login(): String = "auth/login"

    @GetMapping("/chat")
    fun chat(): String = "redirect:/chat/${DonneesExemple.conversations.first().id}"

    @GetMapping("/chat/{id}")
    fun conversation(@PathVariable id: Long, model: Model): String {
        val conversation = DonneesExemple.conversations.firstOrNull { it.id == id }
            ?: return "redirect:/chat"
        model.addAttribute("conversations", DonneesExemple.conversations)
        model.addAttribute("conversationId", id)
        model.addAttribute("conversation", conversation)
        model.addAttribute("messages", DonneesExemple.messages)
        return "chat/conversation"
    }
}
