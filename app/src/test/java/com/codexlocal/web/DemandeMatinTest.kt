package com.codexlocal.web

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DemandeMatinTest {

    @Test
    fun reconnaitLesDemandesDediees() {
        listOf("Bon matin !", "bon matin ☀️", "Nouvelles du matin", "les nouvelles du matin stp",
            "Donne-moi mon briefing", "Résumé du matin", "Bon matin tout le monde").forEach {
            assertTrue(it, RechercheRiche.demandeMatin(it))
        }
    }

    @Test
    fun laisseLesVraiesQuestionsAuChat() {
        listOf("Bon matin, explique les fractions", "bon matin! c'est quoi la photosynthèse ?",
            "Mon matin était difficile", "Raconte-moi les nouvelles du matin à Paris en 1900").forEach {
            assertFalse(it, RechercheRiche.demandeMatin(it))
        }
    }

    @Test
    fun respecteSansInternet() {
        assertFalse(RechercheRiche.demandeMatin("Bon matin sans internet"))
        assertFalse(RechercheRiche.demandeMatin("nouvelles du matin, hors ligne"))
    }
}
