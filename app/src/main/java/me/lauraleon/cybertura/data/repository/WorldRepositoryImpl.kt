package me.lauraleon.cybertura.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.lauraleon.cybertura.data.local.dao.WorldProgressDao
import me.lauraleon.cybertura.data.local.entity.WorldProgressEntity
import me.lauraleon.cybertura.domain.model.World
import me.lauraleon.cybertura.domain.model.WorldProgress
import me.lauraleon.cybertura.domain.model.WorldState
import me.lauraleon.cybertura.domain.model.WorldStatus
import me.lauraleon.cybertura.domain.repository.WorldRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldRepositoryImpl @Inject constructor(
    private val worldProgressDao: WorldProgressDao
) : WorldRepository {

    override fun observeWorlds(): Flow<List<WorldState>> =
        worldProgressDao.observeAll().map { progressList ->
            val progressById = progressList.associateBy { it.worldId }
            World.ALL.map { world ->
                val entity = progressById[world.id]
                val status = when {
                    entity?.status == WorldStatus.COMPLETED.name -> WorldStatus.COMPLETED
                    entity?.status == WorldStatus.UNLOCKED.name -> WorldStatus.UNLOCKED
                    world.id == 1 -> WorldStatus.UNLOCKED
                    else -> WorldStatus.LOCKED
                }
                val progress = entity?.toDomain() ?: WorldProgress(world.id, status)
                WorldState(world, progress.copy(status = status))
            }
        }

    override suspend fun completeWorld(worldId: Int) {
        worldProgressDao.upsert(
            WorldProgressEntity(
                worldId = worldId,
                status = WorldStatus.COMPLETED.name,
                completedAt = System.currentTimeMillis()
            )
        )
        val nextWorld = World.ALL.firstOrNull { it.id == worldId + 1 } ?: return
        worldProgressDao.upsert(
            WorldProgressEntity(
                worldId = nextWorld.id,
                status = WorldStatus.UNLOCKED.name,
                completedAt = null
            )
        )
    }
}

private fun WorldProgressEntity.toDomain() = WorldProgress(
    worldId = worldId,
    status = WorldStatus.valueOf(status),
    completedAt = completedAt
)
