package com.codexlocal.web

import com.codexlocal.regexMots
import org.junit.Assert.*
import org.junit.Test

class LiensTest {
    @Test fun garderLesParenthesesEquilibrees() {
        assertEquals(
            listOf("https://fr.wikipedia.org/wiki/Paris_(Texas)"),
            RechercheRiche.liensDuMessage("lis https://fr.wikipedia.org/wiki/Paris_(Texas) stp")
        )
        assertEquals(
            listOf("https://exemple.com/page"),
            RechercheRiche.liensDuMessage("(voir https://exemple.com/page).")
        )
    }

    @Test fun refuserLeReseauLocal() {
        assertTrue(RechercheRiche.liensDuMessage("https://localhost/x https://192.168.0.1/x http://exemple.com").isEmpty())
        assertFalse(ReseauRiche.pagePublique("https://10.0.0.2/"))
        assertTrue(ReseauRiche.pagePublique("https://exemple.com/"))
    }

    @Test fun limitesDeMotsAvecAccents() {
        val meteo = regexMots("\\b(météo|meteo)\\b")
        assertTrue(meteo.containsMatchIn("quelle est la météo à normandin"))
        assertFalse(meteo.containsMatchIn("mamétéo"))
        assertTrue(regexMots("\\b(où)\\b").containsMatchIn("où est le lac"))
    }
}
