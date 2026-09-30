package me.lauraleon.cybertura.domain.model

data class PlayerProfile(
    val id: Int = 1,
    val nickname: String,
    val age: Int,
    val costumeId: Int,
    val gender: Int
)
