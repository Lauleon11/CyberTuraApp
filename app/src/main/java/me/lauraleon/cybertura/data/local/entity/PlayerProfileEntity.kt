package me.lauraleon.cybertura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val nickname: String,
    val age: Int,
    val costumeId: String,
    val gender: String,
    val hasPlayedIntroNarrative: Boolean = false
)
