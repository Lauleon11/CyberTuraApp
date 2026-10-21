package me.lauraleon.cybertura.ui.screens.world3

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
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

@Composable
fun World3ScreenBActivity(
    onNextClick: () -> Unit = {}
) {
    val targetWord = "ACOSO"
    val hint = "Si alguien te insulta, amenaza o molesta repetidamente por internet, busca ayuda."
    val explanation = "Si alguien te insulta, amenaza o molesta repetidamente por internet, no tienes que enfrentarlo a solas. Busca ayuda."

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
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_world3),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )
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
                text = "🛡️ AHORCADO: DESCUBRE CÓMO AYUDAR",
                color = CyberCyan,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Guide Cat Message Box & Word Hint
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
                            text = "💡 Pista: $hint",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Escudo Protector Progress Indicator
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, if (isWordSolved) CyberSuccess else CyberViolet)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isWordSolved) "🛡️✨ ESCUDO PROTECTOR COMPLETO" else "🛡️ Construyendo Escudo...",
                        color = if (isWordSolved) CyberSuccess else CyberCyan,
                        fontSize = 12.sp,
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

            // Explanation box when word is solved
            if (isWordSolved) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberSuccess.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, CyberSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💡 $explanation",
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
                            .size(width = 34.dp, height = 46.dp)
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
                visible = isWordSolved,
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
                            text = "🎉 ¡DESAFÍO COMPLETADO! Pedir ayuda y apoyar a tus amigos es una acción valiente.",
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
                            text = "CONTINUAR",
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
