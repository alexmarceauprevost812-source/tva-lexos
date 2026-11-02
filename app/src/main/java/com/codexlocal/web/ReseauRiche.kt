package com.codexlocal.web

import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.Locale

/**
 * Téléchargements de la recherche riche (APK perso) : seulement en https, seulement vers les
 * services gratuits et sans clé de la liste [HOTES], réponses limitées en taille.
 */
internal object ReseauRiche {

    val HOTES = setOf(
        "www.bing.com",
        "html.duckduckgo.com",
        "fr.wikipedia.org",
        "en.wikipedia.org",
        "news.google.com",
        "www.youtube.com",
        "geocoding-api.open-meteo.com",
        "api.open-meteo.com",
        "api.coingecko.com",
        "api.github.com",
    )

    fun autorisee(url: String): Boolean = runCatching {
        val uri = URI(url)
        uri.scheme == "https" && uri.userInfo == null && uri.port in listOf(-1, 443) &&
            uri.host?.lowercase(Locale.ROOT) in HOTES
    }.getOrDefault(false)

    fun charger(url: String, maxOctets: Int = 1_500_000): String {
        var courant = url
        repeat(4) {
            require(autorisee(courant)) { "Destination Internet non autorisée" }
            val connexion = URL(courant).openConnection() as HttpURLConnection
            try {
                connexion.connectTimeout = 10_000
                connexion.readTimeout = 12_000
                connexion.instanceFollowRedirects = false
                // Un navigateur mobile ordinaire : certains sites refusent les robots.
                connexion.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Linux; Android 15) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0 Mobile Safari/537.36"
                )
                connexion.setRequestProperty("Accept-Language", "fr-CA,fr;q=0.9,en;q=0.6")
                if (courant.contains("youtube.com")) connexion.setRequestProperty("Cookie", "CONSENT=YES+1; SOCS=CAI")
                val code = connexion.responseCode
                if (code in 300..399) {
                    val location = connexion.getHeaderField("Location") ?: error("Redirection vide")
                    courant = URI(courant).resolve(location).toString()
                    return@repeat
                }
                require(code in 200..299) { "HTTP $code" }
                return connexion.inputStream.use { entree ->
                    val sortie = ByteArrayOutputStream()
                    val tampon = ByteArray(8192)
                    while (true) {
                        val n = entree.read(tampon)
                        if (n < 0) break
                        require(sortie.size() + n <= maxOctets) { "Réponse trop volumineuse" }
                        sortie.write(tampon, 0, n)
                    }
                    sortie.toString("UTF-8")
                }
            } finally {
                connexion.disconnect()
            }
        }
        error("Trop de redirections")
    }

    /**
     * Page web dont l'UTILISATEUR a donné le lien (jamais un lien proposé par l'IA) :
     * n'importe quel site public en https, mais pas le réseau local ni le téléphone lui-même.
     * Retourne (adresse finale, type de contenu, texte brut).
     */
    fun chargerPage(url: String, maxOctets: Int = 1_500_000): Triple<String, String, String> {
        var courant = url
        repeat(5) {
            require(pagePublique(courant)) { "Lien non permis (https public seulement)" }
            val connexion = URL(courant).openConnection() as HttpURLConnection
            try {
                connexion.connectTimeout = 10_000
                connexion.readTimeout = 15_000
                connexion.instanceFollowRedirects = false
                connexion.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Linux; Android 15) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0 Mobile Safari/537.36"
                )
                connexion.setRequestProperty("Accept-Language", "fr-CA,fr;q=0.9,en;q=0.6")
                val code = connexion.responseCode
                if (code in 300..399) {
                    val location = connexion.getHeaderField("Location") ?: error("Redirection vide")
                    courant = URI(courant).resolve(location).toString()
                    return@repeat
                }
                require(code in 200..299) { "HTTP $code" }
                val type = connexion.contentType.orEmpty().lowercase(Locale.ROOT)
                require(type.isEmpty() || type.startsWith("text/") || type.contains("json") || type.contains("xml")) {
                    "Ce lien est un fichier ($type), pas une page"
                }
                val texte = connexion.inputStream.use { entree ->
                    val sortie = ByteArrayOutputStream()
                    val tampon = ByteArray(8192)
                    while (true) {
                        val n = entree.read(tampon)
                        if (n < 0 || sortie.size() + n > maxOctets) break
                        sortie.write(tampon, 0, n)
                    }
                    sortie.toString("UTF-8")
                }
                return Triple(courant, type, texte)
            } finally {
                connexion.disconnect()
            }
        }
        error("Trop de redirections")
    }

    /** https vers un nom de domaine public (pas localhost, pas une adresse IP privée). */
    fun pagePublique(url: String): Boolean = runCatching {
        val uri = URI(url)
        val hote = uri.host?.lowercase(Locale.ROOT)?.trim('[', ']') ?: return false
        uri.scheme == "https" && uri.userInfo == null && uri.port in listOf(-1, 443) &&
            hote.contains('.') && !hote.endsWith(".local") && !hote.endsWith(".internal") &&
            hote != "localhost" && !hote.contains(':') && !Regex("^[0-9.]+$").matches(hote)
    }.getOrDefault(false)
}
