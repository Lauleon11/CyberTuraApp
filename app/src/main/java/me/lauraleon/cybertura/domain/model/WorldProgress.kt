package me.lauraleon.cybertura.domain.model

data class WorldProgress(
    val worldId: Int,
    val status: WorldStatus,
    val completedAt: Long? = null
)

data class WorldState(
    val world: World,
    val progress: WorldProgress
)
