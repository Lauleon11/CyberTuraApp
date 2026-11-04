package me.lauraleon.cybertura.data.remote

import javax.inject.Inject
import javax.inject.Singleton

/** Firestore locations of the current player. Room keeps its own local key (`id = 1`). */
@Singleton
class FirestorePaths @Inject constructor(
    private val playerIdProvider: PlayerIdProvider
) {
    val playerId: String get() = playerIdProvider.id
    val player: String get() = "players/$playerId"
    val survey: String get() = "surveys/$playerId"
    val playerSurvey: String get() = "$player/survey/latest"

    fun world(worldId: Int): String = "$player/worlds/$worldId"

    /** Later writes go to new documents; the ones already written stay untouched. */
    fun startNewPlayer() = playerIdProvider.startNew()
}
