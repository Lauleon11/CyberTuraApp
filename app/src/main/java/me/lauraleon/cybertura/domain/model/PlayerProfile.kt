package me.lauraleon.cybertura.domain.model

data class PlayerProfile(
    val id: Int = 1,
    val nickname: String,
    val age: Int,
    val costumeId: String,
    val gender: String,
    val hasPlayedIntroNarrative: Boolean = false,
    val score: Int = 0
)
