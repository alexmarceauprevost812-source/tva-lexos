package com.codexlocal.chat

import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 🔎 Petite animation pendant une recherche web (comme la Jasette du Ti-Labo) :
 * la loupe bascule et une barre avance doucement dans la ligne d'état. Sobre, sans clignoter.
 */
object AnimRecherche {

    private val loupes = arrayOf("🔍", "🔎")
    private const val CASES = 5

    /** Image [i] de l'animation pour [libelle] (testable sans Android). */
    internal fun image(libelle: String, i: Int): String {
        val pos = i % (CASES * 2 - 2)
        val actif = if (pos < CASES) pos else CASES * 2 - 2 - pos
        val barre = (0 until CASES).joinToString("") { if (it == actif) "▰" else "▱" }
        return "${loupes[(i / 3) % loupes.size]} $libelle  $barre"
    }

    /** Démarre l'animation dans [vue] ; annuler le Job l'arrête et remet la vue à neuf. */
    fun demarrer(scope: CoroutineScope, vue: TextView, libelle: String): Job = scope.launch {
        var i = 0
        try {
            while (isActive) {
                vue.text = image(libelle, i++)
                vue.alpha = 0.75f + 0.25f * kotlin.math.abs(((i % 8) - 4) / 4f)
                delay(180)
            }
        } finally {
            vue.alpha = 1f
        }
    }
}
