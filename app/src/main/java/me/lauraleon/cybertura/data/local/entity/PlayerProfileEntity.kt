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
    val hasPlayedIntroNarrative: Boolean = false,
    val score: Int = 0,
    val ratingStars: Int = 0,
    val ratingComments: String = "",
    val hasRated: Boolean = false,
    val gamesPlayedCount: Int = 1,
    val attemptsW1E: Int = 0,
    val attemptsW1F: Int = 0,
    val attemptsW1G: Int = 0,
    val attemptsW2E: Int = 0,
    val attemptsW2F: Int = 0,
    val attemptsW2G: Int = 0,
    val attemptsW3E: Int = 0,
    val attemptsW3F: Int = 0,
    val attemptsW3G: Int = 0,
    val attemptsW4E: Int = 0,
    val attemptsW4F: Int = 0,
    val attemptsW4G: Int = 0,
    val attemptsW5E: Int = 0,
    val attemptsW5F: Int = 0,
    val attemptsW5G: Int = 0
)
