package me.lauraleon.cybertura.domain.model

data class WorldProgress(
    val worldId: Int,
    val status: WorldStatus,
    val completedAt: Long? = null,
    val currentScreen: String = "A"
)

data class WorldState(
    val world: World,
    val progress: WorldProgress
)
