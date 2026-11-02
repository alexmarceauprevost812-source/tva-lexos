# TVA LexOS

Assistant IA personnel pour Android, **Produit du Québec** ⚜️ — même look que CodexLocal (yeux de démon orange, fond noir), mais centré sur le **chat**, sans Codex ni Studio.

## Ce que fait l'appli

- 💬 **Chat avec IA locale** (modèles .gguf gratuits à télécharger, partagés avec CodexLocal) ou **clé API** (Claude, Gemini, OpenAI, OpenRouter…).
- 🔎 **Recherche riche** gratuite, sans compte : web, fiche Wikipédia, 📰 actualités, 🌤️ météo, ▶️ vidéos, 💰 cryptos avec mini-graphiques (avec ton accord au premier lancement).
- 🎬 **Schémas animés** style vidéo dans le chat.
- ✨ **Écriture fluide** avec effet projection (texte or lumineux qui redevient blanc) et bulles animées.
- 📅 **Agenda** avec rappels ; l'IA peut ajouter ou proposer des rendez-vous (« ➕ Ajouter en 1 clic ») ; Partager → Agenda depuis n'importe quelle appli.
- 🔔 **Notifications** des rendez-vous et des nouvelles.
- 🧰 **Outils** : minuteur / pomodoro, chronomètre, taxes du Québec, liste de tâches, convertisseur.
- 🔊 **Voix québécoise** qui lit les réponses et les nouvelles.
- 👀 **Yeux** : méchant, gentil ou lunettes turbo cool, plusieurs couleurs et arc-en-ciel.
- 👋 L'IA t'appelle par ton nom — pas besoin de compte.
- 🔄 **Mise à jour en un clic** depuis les releases de ce dépôt.

## Installer

Télécharge le dernier `TVA-LexOS-2.0.x.apk` dans les **Releases** et ouvre-le sur ton téléphone (Android 13 ou plus, arm64).

## Compiler

GitHub Actions compile et signe l'APK à chaque push sur `main` et publie une release `v2.0.x`.
En local : `./gradlew :app:assembleDebug` (Android SDK, NDK et CMake requis).
