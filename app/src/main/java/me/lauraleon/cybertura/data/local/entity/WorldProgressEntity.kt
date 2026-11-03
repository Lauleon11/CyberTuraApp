package me.lauraleon.cybertura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_progress")
data class WorldProgressEntity(
    @PrimaryKey val worldId: Int,
    val status: String,
    val completedAt: Long?,
    val currentScreen: String = "A"
)
