package me.lauraleon.cybertura.domain.repository

import kotlinx.coroutines.flow.Flow
import me.lauraleon.cybertura.domain.model.WorldState

interface WorldRepository {
    fun observeWorlds(): Flow<List<WorldState>>
    suspend fun completeWorld(worldId: Int)
}
