package me.lauraleon.cybertura.domain.model

data class PlayerProfile(
    val id: Int = 1,
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
    val timeW1A: Int = 0, val errorsW1A: Int = 0, val attemptsW1A: Int = 0,
    val timeW1B: Int = 0, val errorsW1B: Int = 0, val attemptsW1B: Int = 0,
    val timeW1C: Int = 0, val movesW1C: Int = 0,
    val timeW1E: Int = 0, val errorsW1E: Int = 0, val attemptsW1E: Int = 0,
    val timeW1F: Int = 0, val errorsW1F: Int = 0, val attemptsW1F: Int = 0,
    val timeW1G: Int = 0, val errorsW1G: Int = 0, val attemptsW1G: Int = 0,

    val timeW2A: Int = 0, val errorsW2A: Int = 0, val attemptsW2A: Int = 0,
    val timeW2B: Int = 0, val errorsW2B: Int = 0, val attemptsW2B: Int = 0,
    val timeW2C: Int = 0, val movesW2C: Int = 0,
    val timeW2E: Int = 0, val errorsW2E: Int = 0, val attemptsW2E: Int = 0,
    val timeW2F: Int = 0, val errorsW2F: Int = 0, val attemptsW2F: Int = 0,
    val timeW2G: Int = 0, val errorsW2G: Int = 0, val attemptsW2G: Int = 0,

    val timeW3A: Int = 0, val errorsW3A: Int = 0, val attemptsW3A: Int = 0,
    val timeW3B: Int = 0, val errorsW3B: Int = 0, val attemptsW3B: Int = 0,
    val timeW3C: Int = 0, val movesW3C: Int = 0,
    val timeW3E: Int = 0, val errorsW3E: Int = 0, val attemptsW3E: Int = 0,
    val timeW3F: Int = 0, val errorsW3F: Int = 0, val attemptsW3F: Int = 0,
    val timeW3G: Int = 0, val errorsW3G: Int = 0, val attemptsW3G: Int = 0,

    val timeW4A: Int = 0, val errorsW4A: Int = 0, val attemptsW4A: Int = 0,
    val timeW4B: Int = 0, val errorsW4B: Int = 0, val attemptsW4B: Int = 0,
    val timeW4C: Int = 0, val movesW4C: Int = 0,
    val timeW4E: Int = 0, val errorsW4E: Int = 0, val attemptsW4E: Int = 0,
    val timeW4F: Int = 0, val errorsW4F: Int = 0, val attemptsW4F: Int = 0,
    val timeW4G: Int = 0, val errorsW4G: Int = 0, val attemptsW4G: Int = 0,

    val timeW5A: Int = 0, val errorsW5A: Int = 0, val attemptsW5A: Int = 0,
    val timeW5B: Int = 0, val errorsW5B: Int = 0, val attemptsW5B: Int = 0,
    val timeW5C: Int = 0, val movesW5C: Int = 0,
    val timeW5E: Int = 0, val errorsW5E: Int = 0, val attemptsW5E: Int = 0,
    val timeW5F: Int = 0, val errorsW5F: Int = 0, val attemptsW5F: Int = 0,
    val timeW5G: Int = 0, val errorsW5G: Int = 0, val attemptsW5G: Int = 0
)
