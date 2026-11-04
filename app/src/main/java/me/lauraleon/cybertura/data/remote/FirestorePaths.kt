package me.lauraleon.cybertura.data.remote

import javax.inject.Inject
import javax.inject.Singleton

/** Firestore locations owned by this installation. Room keeps its own local key (`id = 1`). */
@Singleton
class FirestorePaths @Inject constructor(
    private val installationId: InstallationIdProvider
) {
    val playerId: String get() = installationId.id
    val player: String get() = "players/$playerId"
    val survey: String get() = "surveys/$playerId"
    val playerSurvey: String get() = "$player/survey/latest"

    fun world(worldId: Int): String = "$player/worlds/$worldId"
}
