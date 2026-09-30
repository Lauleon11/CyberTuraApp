package me.lauraleon.cybertura.domain.model

data class Costume(
    val id: Int,
    val name: String,
    val emoji: String,
    val accent: String
) {
    companion object {
        val ALL = listOf(
            Costume(1, "Gato Arácnido", "\uD83D\uDD77\uFE0F", "\uD83D\uDD34"),
            Costume(2, "Gato Nocturno", "\uD83E\uDD87", "\uD83D\uDD35"),
            Costume(3, "Gato Solar", "\u2600\uFE0F", "\uD83D\uDFE1")
        )

        fun byId(id: Int): Costume? = ALL.firstOrNull { it.id == id }
    }
}
