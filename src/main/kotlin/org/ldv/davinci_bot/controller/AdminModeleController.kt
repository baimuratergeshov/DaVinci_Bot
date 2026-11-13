package org.ldv.davinci_bot.controller

import org.ldv.davinci_bot.model.vue.DonneesExemple
import org.ldv.davinci_bot.model.vue.Lien
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

// Écrans d'administration des modèles d'IA (affichage uniquement, données d'exemple)
@Controller
class AdminModeleController {

    private val racine = Lien("Administration")
    private val liste = Lien("Modèles IA", "/admin/modeles")

    @GetMapping("/admin")
    fun admin(): String = "redirect:/admin/modeles"

    @GetMapping("/admin/modeles")
    fun liste(model: Model): String {
        model.addAttribute("modeles", DonneesExemple.modeles)
        model.addAttribute("fil", listOf(racine, Lien("Modèles IA")))
        return "admin/modeles-liste"
    }

    @GetMapping("/admin/modeles/nouveau")
    fun nouveau(model: Model): String {
        model.addAttribute("fil", listOf(racine, liste, Lien("Nouveau modèle")))
        model.addAttribute("fournisseurs", DonneesExemple.fournisseurs)
        model.addAttribute("titre", "Nouveau modèle d’IA")
        model.addAttribute("sousTitre", "Ajoute un modèle disponible pour les conversations.")
        model.addAttribute("bouton", "Créer le modèle")
        return "admin/modele-formulaire"
    }

    @GetMapping("/admin/modeles/{id}")
    fun detail(@PathVariable id: Long, model: Model): String {
        val modele = DonneesExemple.modeles.firstOrNull { it.id == id } ?: return "redirect:/admin/modeles"
        model.addAttribute("modele", modele)
        model.addAttribute("fil", listOf(racine, liste, Lien(modele.nom)))
        return "admin/modele-detail"
    }

    @GetMapping("/admin/modeles/{id}/modifier")
    fun modifier(@PathVariable id: Long, model: Model): String {
        val modele = DonneesExemple.modeles.firstOrNull { it.id == id } ?: return "redirect:/admin/modeles"
        model.addAttribute("modele", modele)
        model.addAttribute("fournisseurs", DonneesExemple.fournisseurs)
        model.addAttribute("fil", listOf(racine, liste, Lien(modele.nom, "/admin/modeles/${modele.id}"), Lien("Modifier")))
        model.addAttribute("titre", "Modifier ${modele.nom}")
        model.addAttribute("sousTitre", "Change les informations du modèle.")
        model.addAttribute("bouton", "Enregistrer les modifications")
        return "admin/modele-formulaire"
    }
}
