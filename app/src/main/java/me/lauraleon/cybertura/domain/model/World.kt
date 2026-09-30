package me.lauraleon.cybertura.domain.model

data class World(
    val id: Int,
    val name: String,
    val risk: String,
    val riskEmoji: String,
    val enemy: String,
    val enemyEmoji: String,
    val badge: String
) {
    companion object {
        val ALL = listOf(
            World(
                id = 1,
                name = "El Mar de las Cadenas",
                risk = "Phishing",
                riskEmoji = "\uD83C\uDF0A",
                enemy = "Capitán Phish",
                enemyEmoji = "\uD83C\uDFF4\u200D☠️",
                badge = "Detective Anti-Phishing"
            ),
            World(
                id = 2,
                name = "La Fortaleza de las Cerraduras",
                risk = "Cuentas y contraseñas",
                riskEmoji = "\uD83D\uDD10",
                enemy = "El Rompeclaves",
                enemyEmoji = "\uD83D\uDD28",
                badge = "Guardián de las Claves"
            ),
            World(
                id = 3,
                name = "El Valle de las Sombras",
                risk = "Ciberacoso",
                riskEmoji = "\uD83C\uDF11",
                enemy = "El Troll",
                enemyEmoji = "\uD83D\uDC79",
                badge = "Defensor Digital"
            ),
            World(
                id = 4,
                name = "El Archivo de los Secretos",
                risk = "Privacidad y datos personales",
                riskEmoji = "\uD83D\uDCC1",
                enemy = "El Espía",
                enemyEmoji = "\uD83D\uDD75\uFE0F",
                badge = "Guardián de los Datos"
            ),
            World(
                id = 5,
                name = "El Reino de las Máscaras",
                risk = "Identidad y huella digital",
                riskEmoji = "\uD83C\uDFAD",
                enemy = "El Suplantador",
                enemyEmoji = "\uD83D\uDC64",
                badge = "Guardián de la Identidad"
            )
        )

        fun byId(id: Int): World? = ALL.firstOrNull { it.id == id }
    }
}
