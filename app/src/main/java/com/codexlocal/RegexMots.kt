package com.codexlocal

/**
 * Expressions régulières avec des limites de mots qui comprennent les accents (« météo », « où »…).
 *
 * Le drapeau `(?U)` (UNICODE_CHARACTER_CLASS) marche sur un PC, mais Android ne le connaît PAS :
 * il lance une erreur dès la création du motif, et l'appli se ferme. Ici, `\b` et `\w` sont
 * remplacés par des équivalents qui marchent partout (Android comme JVM des tests).
 */
private const val MOT = "[\\p{L}\\p{N}_]"
private const val BORNE = "(?:(?<!$MOT)(?=$MOT)|(?<=$MOT)(?!$MOT))"

fun regexMots(motif: String, ignorerCasse: Boolean = false): Regex {
    val portable = motif.replace("\\b", BORNE).replace("\\w", MOT)
    return if (ignorerCasse) Regex(portable, RegexOption.IGNORE_CASE) else Regex(portable)
}
