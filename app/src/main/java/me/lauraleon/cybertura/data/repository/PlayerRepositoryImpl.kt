package me.lauraleon.cybertura.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.lauraleon.cybertura.data.local.dao.PlayerDao
import me.lauraleon.cybertura.data.local.entity.PlayerProfileEntity
import me.lauraleon.cybertura.domain.model.PlayerProfile
import me.lauraleon.cybertura.domain.repository.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
) : PlayerRepository {

    override fun observePlayer(): Flow<PlayerProfile?> =
        playerDao.observePlayer().map { it?.toDomain() }

    override suspend fun savePlayer(profile: PlayerProfile) {
        playerDao.upsertPlayer(profile.toEntity())
    }
}

private fun PlayerProfileEntity.toDomain() = PlayerProfile(
    id = id,
    nickname = nickname,
    age = age,
    costumeId = costumeId,
    gender = gender
)

private fun PlayerProfile.toEntity() = PlayerProfileEntity(
    id = id,
    nickname = nickname,
    age = age,
    costumeId = costumeId,
    gender = gender
)
