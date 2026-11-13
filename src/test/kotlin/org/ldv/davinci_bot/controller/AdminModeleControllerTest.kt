package org.ldv.davinci_bot.controller

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AdminModeleController::class)
class AdminModeleControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `la liste des modeles s'affiche`() {
        mockMvc.perform(get("/admin/modeles"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("5 modèles affichés")))
    }

    @Test
    fun `le detail, la creation et la modification s'affichent`() {
        mockMvc.perform(get("/admin/modeles/1")).andExpect(status().isOk)
            .andExpect(content().string(containsString("Supprimer ce modèle")))
        mockMvc.perform(get("/admin/modeles/nouveau")).andExpect(status().isOk)
            .andExpect(content().string(containsString("Créer le modèle")))
        mockMvc.perform(get("/admin/modeles/1/modifier")).andExpect(status().isOk)
            .andExpect(content().string(containsString("Modifier Gemini Flash")))
    }
}
