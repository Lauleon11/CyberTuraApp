package me.lauraleon.cybertura.data.content

data class ClassificationItem(
    val text: String,
    val safe: Boolean
)

data class WorldContent(
    val worldId: Int,
    val arrivalDialogues: List<String>,
    val lessonTitle: String,
    val lessonParagraphs: List<String>,
    val mediaList: List<String>,
    val example: String,
    val signals: List<String>,
    val actions: List<String>,
    val classificationItems: List<ClassificationItem>,
    val bossTitle: String,
    val bossIntro: String,
    val bossVictoryLine: String,
    val badgeTitle: String
)
