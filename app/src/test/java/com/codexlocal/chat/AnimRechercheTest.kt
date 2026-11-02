package com.codexlocal.chat

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnimRechercheTest {

    @Test
    fun laBarreFaitLAllerRetour() {
        val positions = (0 until 8).map { i -> AnimRecherche.image("Recherche", i).substringAfterLast(' ').indexOf('▰') }
        assertEquals(listOf(0, 1, 2, 3, 4, 3, 2, 1), positions)
    }

    @Test
    fun chaqueImageGardeLeLibelleEtUneLoupe() {
        repeat(12) { i ->
            val img = AnimRecherche.image("Recherche sur le web", i)
            assertTrue(img.contains("Recherche sur le web"))
            assertTrue(img.startsWith("🔍") || img.startsWith("🔎"))
            assertEquals(1, img.count { it == '▰' })
        }
    }
}
