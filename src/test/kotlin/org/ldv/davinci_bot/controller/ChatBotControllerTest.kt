package org.ldv.davinci_bot.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest(ChatBotController::class)
class ChatBotControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `la page d'accueil s'affiche avec header et footer`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("accueil"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Bienvenue sur DaVinci Bot")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("site-header")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")))
    }

    @Test
    fun `le formulaire de connexion s'affiche`() {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk)
            .andExpect(view().name("auth/login"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"password\"")))
    }

    @Test
    fun `le chat affiche la conversation`() {
        mockMvc.perform(get("/chat/1"))
            .andExpect(status().isOk)
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Aide pour le TP SLAM")))
    }
}
