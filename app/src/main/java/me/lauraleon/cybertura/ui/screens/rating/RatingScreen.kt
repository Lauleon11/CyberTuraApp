package me.lauraleon.cybertura.ui.screens.rating

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

@Composable
fun RatingScreen(
    onRatingSubmitted: (Int, String, Map<Int, Int>) -> Unit = { _, _, _ -> },
    onNavigateToBadges: () -> Unit = {}
) {
    val questions = remember {
        listOf(
            "1. ¿Qué tanto te gustó la aplicación Cybertura?",
            "2. ¿Fue fácil navegar por la aplicación y pasar de una pantalla a otra?",
            "3. ¿Te resultó fácil entender cómo jugar las actividades?",
            "4. ¿Los botones y las instrucciones funcionaron correctamente?",
            "5. ¿Te gustaron los personajes, los mundos y los escenarios del juego?",
            "6. ¿Te parecieron divertidos los juegos de ahorcado, sopa de letras y rompecabezas?",
            "7. ¿Te gustó desbloquear mundos y ganar insignias?",
            "8. ¿Los videos fueron claros y fáciles de entender?",
            "9. ¿Las actividades te ayudaron a comprender mejor los temas de ciberseguridad?",
            "10. ¿Te gustaría volver a jugar Cybertura?"
        )
    }

    val answers = remember { mutableStateMapOf<Int, Int>().apply { for (i in questions.indices) this[i] = 5 } }
    var openComment by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Text(
                text = "⭐ ¡Cuéntanos tu aventura en Cybertura! ⭐",
                color = CyberCyan,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Instructions
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.Black.copy(alpha = 0.75f),
                border = BorderStroke(1.dp, CyberCyan)
            ) {
                Text(
                    text = "¡Queremos conocer tu opinión! Marca de 1 a 5 estrellas según lo que te pareció la aplicación. No hay respuestas buenas ni malas.",
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scale Legend Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CyberViolet.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, CyberViolet)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Escala de valoración:",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⭐ 1: Muy poco  |  ⭐⭐ 2: Poco  |  ⭐⭐⭐ 3: Regular\n⭐⭐⭐⭐ 4: Mucho  |  ⭐⭐⭐⭐⭐ 5: Muchísimo",
                        color = Color.White,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isSubmitted) {
                // Questions list
                questions.forEachIndexed { index, question ->
                    val currentRating = answers[index] ?: 5

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.8f),
                        border = BorderStroke(1.5.dp, CyberViolet),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = question,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (star in 1..5) {
                                    Text(
                                        text = if (star <= currentRating) "⭐" else "☆",
                                        fontSize = 28.sp,
                                        modifier = Modifier
                                            .clickable { answers[index] = star }
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question 11: Open-ended comment
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.8f),
                    border = BorderStroke(1.5.dp, CyberCyan),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "11. ¿Qué fue lo que más te gustó o qué sugerencia nos darías?",
                            color = CyberCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = openComment,
                            onValueChange = { openComment = it },
                            placeholder = { Text("Escribe tu opinión aquí (opcional)...", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberViolet,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = CyberCyan
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val avgRating = answers.values.average().toInt()
                        onRatingSubmitted(avgRating, openComment, answers)
                        isSubmitted = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(2.dp, CyberCyan, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberViolet),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "ENVIAR CALIFICACIÓN",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.85f),
                    border = BorderStroke(2.dp, CyberSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 ¡MUCHAS GRACIAS POR TUS RESPUESTAS!",
                            color = CyberSuccess,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Tu opinión es muy valiosa para nosotros. ¡Has completado tu gran aventura en Cybertura!",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = onNavigateToBadges,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(2.dp, CyberCyan, RoundedCornerShape(14.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberBlue),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "🌀 VER SALÓN DE LAS INSIGNIAS",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingScreenPreview() {
    CyberTuraTheme {
        RatingScreen()
    }
}
