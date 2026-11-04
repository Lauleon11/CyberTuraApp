package me.lauraleon.cybertura.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import me.lauraleon.cybertura.data.local.dao.PlayerDao
import me.lauraleon.cybertura.data.local.entity.PlayerProfileEntity
import me.lauraleon.cybertura.data.remote.FirestorePaths
import me.lauraleon.cybertura.domain.model.PlayerProfile
import me.lauraleon.cybertura.domain.repository.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao,
    private val firestore: FirebaseFirestore,
    private val paths: FirestorePaths
) : PlayerRepository {

    override fun observePlayer(): Flow<PlayerProfile?> =
        playerDao.observePlayer().map { it?.toDomain() }

    override suspend fun savePlayer(profile: PlayerProfile) {
        playerDao.upsertPlayer(profile.toEntity())
        try {
            firestore.document(paths.player)
                .set(profile)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveSurvey(
        stars: Int,
        comments: String,
        surveyAnswers: Map<Int, Int>
    ) {
        try {
            val surveyData = hashMapOf(
                "playerId" to paths.playerId,
                "timestamp" to System.currentTimeMillis(),
                "averageStars" to stars,
                "openComment" to comments,
                "questionAnswers" to surveyAnswers.mapKeys { "question_${it.key + 1}" }
            )
            firestore.document(paths.survey)
                .set(surveyData)
                .await()

            firestore.document(paths.playerSurvey)
                .set(surveyData)
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
    timeW1A = timeW1A, errorsW1A = errorsW1A, attemptsW1A = attemptsW1A,
    timeW1B = timeW1B, errorsW1B = errorsW1B, attemptsW1B = attemptsW1B,
    timeW1C = timeW1C, movesW1C = movesW1C,
    timeW1E = timeW1E, errorsW1E = errorsW1E, attemptsW1E = attemptsW1E,
    timeW1F = timeW1F, errorsW1F = errorsW1F, attemptsW1F = attemptsW1F,
    timeW1G = timeW1G, errorsW1G = errorsW1G, attemptsW1G = attemptsW1G,

    timeW2A = timeW2A, errorsW2A = errorsW2A, attemptsW2A = attemptsW2A,
    timeW2B = timeW2B, errorsW2B = errorsW2B, attemptsW2B = attemptsW2B,
    timeW2C = timeW2C, movesW2C = movesW2C,
    timeW2E = timeW2E, errorsW2E = errorsW2E, attemptsW2E = attemptsW2E,
    timeW2F = timeW2F, errorsW2F = errorsW2F, attemptsW2F = attemptsW2F,
    timeW2G = timeW2G, errorsW2G = errorsW2G, attemptsW2G = attemptsW2G,

    timeW3A = timeW3A, errorsW3A = errorsW3A, attemptsW3A = attemptsW3A,
    timeW3B = timeW3B, errorsW3B = errorsW3B, attemptsW3B = attemptsW3B,
    timeW3C = timeW3C, movesW3C = movesW3C,
    timeW3E = timeW3E, errorsW3E = errorsW3E, attemptsW3E = attemptsW3E,
    timeW3F = timeW3F, errorsW3F = errorsW3F, attemptsW3F = attemptsW3F,
    timeW3G = timeW3G, errorsW3G = errorsW3G, attemptsW3G = attemptsW3G,

    timeW4A = timeW4A, errorsW4A = errorsW4A, attemptsW4A = attemptsW4A,
    timeW4B = timeW4B, errorsW4B = errorsW4B, attemptsW4B = attemptsW4B,
    timeW4C = timeW4C, movesW4C = movesW4C,
    timeW4E = timeW4E, errorsW4E = errorsW4E, attemptsW4E = attemptsW4E,
    timeW4F = timeW4F, errorsW4F = errorsW4F, attemptsW4F = attemptsW4F,
    timeW4G = timeW4G, errorsW4G = errorsW4G, attemptsW4G = attemptsW4G,

    timeW5A = timeW5A, errorsW5A = errorsW5A, attemptsW5A = attemptsW5A,
    timeW5B = timeW5B, errorsW5B = errorsW5B, attemptsW5B = attemptsW5B,
    timeW5C = timeW5C, movesW5C = movesW5C,
    timeW5E = timeW5E, errorsW5E = errorsW5E, attemptsW5E = attemptsW5E,
    timeW5F = timeW5F, errorsW5F = errorsW5F, attemptsW5F = attemptsW5F,
    timeW5G = timeW5G, errorsW5G = errorsW5G, attemptsW5G = attemptsW5G
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
    timeW1A = timeW1A, errorsW1A = errorsW1A, attemptsW1A = attemptsW1A,
    timeW1B = timeW1B, errorsW1B = errorsW1B, attemptsW1B = attemptsW1B,
    timeW1C = timeW1C, movesW1C = movesW1C,
    timeW1E = timeW1E, errorsW1E = errorsW1E, attemptsW1E = attemptsW1E,
    timeW1F = timeW1F, errorsW1F = errorsW1F, attemptsW1F = attemptsW1F,
    timeW1G = timeW1G, errorsW1G = errorsW1G, attemptsW1G = attemptsW1G,

    timeW2A = timeW2A, errorsW2A = errorsW2A, attemptsW2A = attemptsW2A,
    timeW2B = timeW2B, errorsW2B = errorsW2B, attemptsW2B = attemptsW2B,
    timeW2C = timeW2C, movesW2C = movesW2C,
    timeW2E = timeW2E, errorsW2E = errorsW2E, attemptsW2E = attemptsW2E,
    timeW2F = timeW2F, errorsW2F = errorsW2F, attemptsW2F = attemptsW2F,
    timeW2G = timeW2G, errorsW2G = errorsW2G, attemptsW2G = attemptsW2G,

    timeW3A = timeW3A, errorsW3A = errorsW3A, attemptsW3A = attemptsW3A,
    timeW3B = timeW3B, errorsW3B = errorsW3B, attemptsW3B = attemptsW3B,
    timeW3C = timeW3C, movesW3C = movesW3C,
    timeW3E = timeW3E, errorsW3E = errorsW3E, attemptsW3E = attemptsW3E,
    timeW3F = timeW3F, errorsW3F = errorsW3F, attemptsW3F = attemptsW3F,
    timeW3G = timeW3G, errorsW3G = errorsW3G, attemptsW3G = attemptsW3G,

    timeW4A = timeW4A, errorsW4A = errorsW4A, attemptsW4A = attemptsW4A,
    timeW4B = timeW4B, errorsW4B = errorsW4B, attemptsW4B = attemptsW4B,
    timeW4C = timeW4C, movesW4C = movesW4C,
    timeW4E = timeW4E, errorsW4E = errorsW4E, attemptsW4E = attemptsW4E,
    timeW4F = timeW4F, errorsW4F = errorsW4F, attemptsW4F = attemptsW4F,
    timeW4G = timeW4G, errorsW4G = errorsW4G, attemptsW4G = attemptsW4G,

    timeW5A = timeW5A, errorsW5A = errorsW5A, attemptsW5A = attemptsW5A,
    timeW5B = timeW5B, errorsW5B = errorsW5B, attemptsW5B = attemptsW5B,
    timeW5C = timeW5C, movesW5C = movesW5C,
    timeW5E = timeW5E, errorsW5E = errorsW5E, attemptsW5E = attemptsW5E,
    timeW5F = timeW5F, errorsW5F = errorsW5F, attemptsW5F = attemptsW5F,
    timeW5G = timeW5G, errorsW5G = errorsW5G, attemptsW5G = attemptsW5G
)
