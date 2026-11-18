package org.ldv.davinci_bot.dto.mistral

import org.ldv.davinci_bot.model.entity.Message
import org.ldv.davinci_bot.model.entity.ModeleIA

interface IAService {

    // Identifie le fournisseur pris en charge
    fun getFournisseur(): String

    // Envoie l'historique de la conversation (le dernier message est celui de l'utilisateur)
    // au modèle choisi, précédé du prompt système du modèle.
    fun envoyerPrompt(historique: List<Message>, modele: ModeleIA): String
}
