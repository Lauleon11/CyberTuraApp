package me.lauraleon.cybertura.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import me.lauraleon.cybertura.data.local.dao.PlayerDao
import me.lauraleon.cybertura.data.local.entity.PlayerProfileEntity
import me.lauraleon.cybertura.domain.model.PlayerProfile
import me.lauraleon.cybertura.domain.repository.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao,
    private val firestore: FirebaseFirestore
) : PlayerRepository {

    override fun observePlayer(): Flow<PlayerProfile?> =
        playerDao.observePlayer().map { it?.toDomain() }

    override suspend fun savePlayer(profile: PlayerProfile) {
        playerDao.upsertPlayer(profile.toEntity())
        try {
            firestore.collection("players")
                .document(profile.id.toString())
                .set(profile)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun PlayerProfileEntity.toDomain() = PlayerProfile(
    id = id,
    nickname = nickname,
    age = age,
    costumeId = costumeId,
    gender = gender,
    hasPlayedIntroNarrative = hasPlayedIntroNarrative,
    score = score,
    ratingStars = ratingStars,
    ratingComments = ratingComments,
    hasRated = hasRated,
    gamesPlayedCount = gamesPlayedCount,
    attemptsW1E = attemptsW1E,
    attemptsW1F = attemptsW1F,
    attemptsW1G = attemptsW1G,
    attemptsW2E = attemptsW2E,
    attemptsW2F = attemptsW2F,
    attemptsW2G = attemptsW2G,
    attemptsW3E = attemptsW3E,
    attemptsW3F = attemptsW3F,
    attemptsW3G = attemptsW3G,
    attemptsW4E = attemptsW4E,
    attemptsW4F = attemptsW4F,
    attemptsW4G = attemptsW4G,
    attemptsW5E = attemptsW5E,
    attemptsW5F = attemptsW5F,
    attemptsW5G = attemptsW5G
)

private fun PlayerProfile.toEntity() = PlayerProfileEntity(
    id = id,
    nickname = nickname,
    age = age,
    costumeId = costumeId,
    gender = gender,
    hasPlayedIntroNarrative = hasPlayedIntroNarrative,
    score = score,
    ratingStars = ratingStars,
    ratingComments = ratingComments,
    hasRated = hasRated,
    gamesPlayedCount = gamesPlayedCount,
    attemptsW1E = attemptsW1E,
    attemptsW1F = attemptsW1F,
    attemptsW1G = attemptsW1G,
    attemptsW2E = attemptsW2E,
    attemptsW2F = attemptsW2F,
    attemptsW2G = attemptsW2G,
    attemptsW3E = attemptsW3E,
    attemptsW3F = attemptsW3F,
    attemptsW3G = attemptsW3G,
    attemptsW4E = attemptsW4E,
    attemptsW4F = attemptsW4F,
    attemptsW4G = attemptsW4G,
    attemptsW5E = attemptsW5E,
    attemptsW5F = attemptsW5F,
    attemptsW5G = attemptsW5G
)
