package me.lauraleon.cybertura.domain.repository

import kotlinx.coroutines.flow.Flow
import me.lauraleon.cybertura.domain.model.PlayerProfile

interface PlayerRepository {
    fun observePlayer(): Flow<PlayerProfile?>
    suspend fun savePlayer(profile: PlayerProfile)
    suspend fun saveSurvey(stars: Int, comments: String, surveyAnswers: Map<Int, Int>)
}
