package me.lauraleon.cybertura.data.content

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldContentProvider @Inject constructor() {

    fun getContent(worldId: Int): WorldContent? = WORLD_1_CONTENT

    private companion object {
        val WORLD_1_CONTENT = WorldContent(
            worldId = 1,
            arrivalDialogues = listOf(
                "\uD83C\uDF0A ¡Hemos llegado al Mar de las Cadenas!",
                "Este lugar está bajo el control de un pirata muy peligroso: Capitán Phish.",
                "Utiliza mensajes, enlaces y engaños para intentar atrapar a quienes navegan por Internet.",
                "Pero antes de enfrentarlo, necesitas conocer sus trucos.",
                "¡Vamos a aprender a reconocer sus anzuelos!"
            ),
            lessonTitle = "\uD83C\uDF0A ¿QUÉ ES EL PHISHING?",
            lessonParagraphs = listOf(
                "El phishing es un engaño que busca hacerte entregar información o realizar una acción que puede ponerte en peligro.",
                "¿Te parece demasiado bueno para ser verdad? ¡Cuidado! Podría ser una trampa del Capitán Phish."
            ),
            mediaList = listOf(
                "\uD83D\uDCE7 Correos electrónicos",
                "\uD83D\uDCAC Mensajes",
                "\uD83D\uDCF1 Redes sociales",
                "\uD83C\uDF10 Páginas web",
                "\uD83C\uDF81 Premios falsos"
            ),
            example = "\uD83C\uDF81 “¡FELICIDADES! Ganaste un teléfono. Haz clic aquí y escribe tus datos para reclamarlo.”",
            signals = listOf(
                "\uD83D\uDEA8 Te piden información personal.",
                "\uD83D\uDEA8 Te piden contraseñas.",
                "\uD83D\uDEA8 El mensaje genera urgencia.",
                "\uD83D\uDEA8 Promete premios increíbles.",
                "\uD83D\uDEA8 Tiene enlaces sospechosos.",
                "\uD83D\uDEA8 Proviene de alguien desconocido."
            ),
            actions = listOf(
                "No hagas clic.",
                "No respondas.",
                "No entregues información.",
                "Verifica quién lo envió.",
                "Pide ayuda a un adulto de confianza.",
                "Reporta o elimina el mensaje."
            ),
            classificationItems = listOf(
                ClassificationItem("Mensaje de tu profe: “Mañana no hay clase.”", safe = true),
                ClassificationItem("“Haz clic aquí para ganar 1.000.000 de monedas gratis.”", safe = false),
                ClassificationItem("“Urgente: escribe tu contraseña para no perder tu cuenta.”", safe = false),
                ClassificationItem("Mensaje de tus padres: “Te esperamos después de la escuela.”", safe = true),
                ClassificationItem("“¡Eres el ganador de un premio! Envía tus datos personales.”", safe = false),
                ClassificationItem("“Recuerda tu tarea: lee el capítulo 3.”", safe = true)
            ),
            bossTitle = "\uD83C\uDFF4\u200D☠️ CAPITÁN PHISH",
            bossIntro = "El Capitán Phish lanza sus anzuelos para intentar atraparte. Usa todo lo que aprendiste para vencerlo.",
            bossVictoryLine = "¡Lo lograste! Los anzuelos del Capitán Phish no te atraparon. Has aprendido a reconocer el phishing.",
            badgeTitle = "\uD83C\uDFC5 DETECTIVE ANTI-PHISHING"
        )
    }
}
