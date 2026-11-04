package me.lauraleon.cybertura.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.lauraleon.cybertura.data.local.dao.WorldProgressDao
import me.lauraleon.cybertura.data.local.entity.WorldProgressEntity
import me.lauraleon.cybertura.data.remote.FirestorePaths
import me.lauraleon.cybertura.data.remote.queueRemoteWrite
import me.lauraleon.cybertura.domain.model.World
import me.lauraleon.cybertura.domain.model.WorldProgress
import me.lauraleon.cybertura.domain.model.WorldState
import me.lauraleon.cybertura.domain.model.WorldStatus
import me.lauraleon.cybertura.domain.repository.WorldRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldRepositoryImpl @Inject constructor(
    private val worldProgressDao: WorldProgressDao,
    private val firestore: FirebaseFirestore,
    private val paths: FirestorePaths
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
                val progress = entity?.toDomain() ?: WorldProgress(
                    worldId = world.id,
                    status = status,
                    completedAt = null,
                    currentScreen = "A"
                )
                WorldState(world, progress.copy(status = status))
            }
        }

    override suspend fun completeWorld(worldId: Int) {
        val existing = worldProgressDao.getByWorld(worldId)
        val completedEntity = WorldProgressEntity(
            worldId = worldId,
            status = WorldStatus.COMPLETED.name,
            completedAt = System.currentTimeMillis(),
            currentScreen = existing?.currentScreen ?: "H"
        )
        worldProgressDao.upsert(completedEntity)

        val nextWorld = World.ALL.firstOrNull { it.id == worldId + 1 }
        val unlockedEntity = nextWorld?.let {
            WorldProgressEntity(
                worldId = it.id,
                status = WorldStatus.UNLOCKED.name,
                completedAt = null,
                currentScreen = "A"
            )
        }

        if (unlockedEntity != null) {
            worldProgressDao.upsert(unlockedEntity)
        }

        queueRemoteWrite("completed world $worldId") {
            val batch = firestore.batch()

            batch.set(firestore.document(paths.world(worldId)), completedEntity)
            if (unlockedEntity != null && nextWorld != null) {
                batch.set(firestore.document(paths.world(nextWorld.id)), unlockedEntity)
            }
            batch.commit()
        }
    }

    override suspend fun updateWorldScreen(worldId: Int, screen: String) {
        val existing = worldProgressDao.getByWorld(worldId)
        val updatedEntity = if (existing != null) {
            existing.copy(currentScreen = screen)
        } else {
            WorldProgressEntity(
                worldId = worldId,
                status = WorldStatus.UNLOCKED.name,
                completedAt = null,
                currentScreen = screen
            )
        }
        worldProgressDao.upsert(updatedEntity)

        queueRemoteWrite("world $worldId progress") {
            firestore.document(paths.world(worldId)).set(updatedEntity)
        }
    }

    override suspend fun resetWorlds() {
        worldProgressDao.deleteAll()
        val world1Entity = WorldProgressEntity(
            worldId = 1,
            status = WorldStatus.UNLOCKED.name,
            completedAt = null,
            currentScreen = "A"
        )
        worldProgressDao.upsert(world1Entity)

        queueRemoteWrite("reset worlds") {
            val batch = firestore.batch()
            batch.set(firestore.document(paths.world(1)), world1Entity)
            for (id in 2..5) {
                val lockedEntity = WorldProgressEntity(
                    worldId = id,
                    status = WorldStatus.LOCKED.name,
                    completedAt = null,
                    currentScreen = "A"
                )
                batch.set(firestore.document(paths.world(id)), lockedEntity)
            }
            batch.commit()
        }
    }

    override suspend fun clearLocalWorlds() {
        worldProgressDao.deleteAll()
    }
}

private fun WorldProgressEntity.toDomain() = WorldProgress(
    worldId = worldId,
    status = WorldStatus.valueOf(status),
    completedAt = completedAt,
    currentScreen = currentScreen
)
