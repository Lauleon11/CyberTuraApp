package me.lauraleon.cybertura.ui.screens.world5

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

private data class HangmanWord(
    val word: String,
    val hint: String,
    val educationalClue: String
)

@Composable
fun World5ScreenBActivity(
    onNextClick: () -> Unit = {}
) {
    val wordsList = remember {
        listOf(
            HangmanWord(
                "PERFIL",
                "Es la página de una persona dentro de una red social.",
                "Una foto bonita o un nombre conocido no demuestran por sí solos que una cuenta sea auténtica."
            ),
            HangmanWord(
                "IDENTIDAD",
                "Es lo que permite distinguir a una persona de otra.",
                "Antes de confiar en una cuenta, busca señales adicionales para comprobar quién está detrás."
            ),
            HangmanWord(
                "SUPLANTAR",
                "Es hacerse pasar por otra persona sin su autorización.",
                "Si sospechas que una cuenta se hace pasar por alguien, no compartas información privada. Consulta con un adulto de confianza y reporta el perfil en la plataforma."
            )
        )
    }

    var currentWordIndex by remember { mutableIntStateOf(0) }
    val currentHangmanWord = wordsList.getOrNull(currentWordIndex) ?: wordsList.last()
    val targetWord = currentHangmanWord.word

    val guessedLetters = remember { mutableStateListOf<Char>() }

    val incorrectCount by remember {
        derivedStateOf {
            guessedLetters.count { it !in targetWord }
        }
    }

    val maxAttempts = 6
    val remainingAttempts = (maxAttempts - incorrectCount).coerceAtLeast(0)

    val isWordSolved by remember {
        derivedStateOf {
            targetWord.all { it in guessedLetters }
        }
    }

    val isGameOver by remember {
        derivedStateOf {
            remainingAttempts == 0 && !isWordSolved
        }
    }

    val isAllCompleted by remember {
        derivedStateOf {
            currentWordIndex >= wordsList.size - 1 && isWordSolved
        }
    }

    // Auto-advance to next word when word is solved
    LaunchedEffect(isWordSolved, currentWordIndex) {
        if (isWordSolved && currentWordIndex < wordsList.size - 1) {
            delay(1800)
            currentWordIndex += 1
            guessedLetters.clear()
        }
    }

    fun restartCurrentWord() {
        guessedLetters.clear()
    }

    fun handleLetterClick(letter: Char) {
        if (letter !in guessedLetters && !isWordSolved && !isGameOver) {
            guessedLetters.add(letter)
        }
    }

    val alphabetRows = listOf(
        "ABCDEFG".toList(),
        "HIJKLMN".toList(),
        "OPQRSTU".toList(),
        listOf('V', 'W', 'X', 'Y', 'Z', 'Ñ')
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Header Title
            Text(
                text = "🎭 AHORCADO: ¿QUIÉN ESTÁ DETRÁS DEL PERFIL?",
                color = CyberCyan,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Guide Cat Message Box & Hint
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, CyberCyan)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gato_guia),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(2.dp, CyberCyan, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Palabra ${currentWordIndex + 1} de ${wordsList.size}",
                            color = CyberMagenta,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "💡 Pista: ${currentHangmanWord.hint}",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // El Suplantador Unmasking Indicator
            val masksRemovedCount = if (isWordSolved) currentWordIndex + 1 else currentWordIndex
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, if (masksRemovedCount > 0) CyberSuccess else CyberViolet)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = when (masksRemovedCount) {
                            1 -> "🎭 Máscara 1 Retirada: Nombre Verificado"
                            2 -> "🎭 Máscara 2 Retirada: Señales de Identidad"
                            3 -> "🎭✨ EL SUPLANTADOR DESENMASCARADO"
                            else -> "🎭 El Suplantador oculto tras máscaras..."
                        },
                        color = if (masksRemovedCount > 0) CyberSuccess else CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        repeat(maxAttempts) { index ->
                            val isHeartActive = index < remainingAttempts
                            Text(
                                text = if (isHeartActive) "❤️" else "🖤",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(horizontal = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Educational Clue Box when word is solved
            if (isWordSolved) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberSuccess.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, CyberSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔍 ${currentHangmanWord.educationalClue}",
                        color = CyberSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Word Spaces Display
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                targetWord.forEach { letter ->
                    val isRevealed = letter in guessedLetters

                    Box(
                        modifier = Modifier
                            .size(width = 32.dp, height = 44.dp)
                            .background(
                                if (isRevealed) CyberSuccess.copy(alpha = 0.2f) else Color(0xFF1E1035),
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                2.dp,
                                if (isRevealed) CyberSuccess else CyberViolet,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isRevealed) letter.toString() else "_",
                            color = if (isRevealed) CyberSuccess else CyberCyan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Alphabet Keyboard
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                alphabetRows.forEach { rowLetters ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowLetters.forEach { letter ->
                            val isUsed = letter in guessedLetters
                            val isCorrect = isUsed && letter in targetWord

                            val buttonBg = when {
                                !isUsed -> Color(0xFF2A164D)
                                isCorrect -> CyberSuccess
                                else -> CyberDanger
                            }

                            val textColor = when {
                                !isUsed -> Color.White
                                else -> Color.Black
                            }

                            Box(
                                modifier = Modifier
                                    .size(width = 42.dp, height = 46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(buttonBg)
                                    .border(
                                        1.dp,
                                        if (isUsed) Color.Transparent else CyberCyan.copy(alpha = 0.6f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable(enabled = !isUsed && !isWordSolved && !isGameOver) {
                                        handleLetterClick(letter)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter.toString(),
                                    color = textColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Game Over / Restart Banner
            if (isGameOver) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CyberDanger.copy(alpha = 0.2f),
                        border = BorderStroke(1.5.dp, CyberDanger),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "❌ ¡Te quedaste sin intentos! La palabra era: $targetWord",
                            color = CyberDanger,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Button(
                        onClick = { restartCurrentWord() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberDanger),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "🔄 REINTENTAR PALABRA",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Completion Banner and Continuation Button
            AnimatedVisibility(
                visible = isAllCompleted,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CyberSuccess.copy(alpha = 0.2f),
                        border = BorderStroke(1.5.dp, CyberSuccess),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "🎉 ¡EXCELENTE, GUARDIÁN! No siempre podemos saber quién está detrás de una cuenta solo por su nombre o su foto. Verifica antes de confiar y pide ayuda si algo te parece sospechoso.",
                            color = CyberSuccess,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Button(
                        onClick = onNextClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(2.dp, CyberCyan, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberViolet),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "CONTINUAR A LA PANTALLA C",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
