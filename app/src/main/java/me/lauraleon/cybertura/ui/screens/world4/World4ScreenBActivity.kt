package me.lauraleon.cybertura.ui.screens.world4

import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

@Composable
fun World4ScreenBActivity(
    score: Int = 0,
    onCorrectAnswer: () -> Unit = {},
    onIncorrectAttempt: () -> Unit = {},
    onComplete: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onNextClick: () -> Unit = {}
) {
    val startTime = remember { System.currentTimeMillis() }
    var totalAttempts by remember { mutableStateOf(0) }
    var hasRewarded by remember { mutableStateOf(false) }

    var popupMessage by remember { mutableStateOf<String?>(null) }
    var isPopupCorrect by remember { mutableStateOf(true) }
    val context = LocalContext.current

    fun triggerPopup(correct: Boolean) {
        isPopupCorrect = correct
        popupMessage = if (correct) "+10 PUNTOS" else "-5 PUNTOS"
        try {
            val soundRes = if (correct) R.raw.fecto_de_sonido_correcto else R.raw.respuesta_incorrecta
            val mp = MediaPlayer.create(context, soundRes)
            mp.start()
            mp.setOnCompletionListener { it.release() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(popupMessage) {
        if (popupMessage != null) {
            delay(1300)
            popupMessage = null
        }
    }

    val targetWord = "PRIVACIDAD"
    val hint = "Es proteger tu información personal para que no todos puedan verla."

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

    LaunchedEffect(isWordSolved) {
        if (isWordSolved && !hasRewarded) {
            hasRewarded = true
            onCorrectAnswer()
            triggerPopup(true)
            val elapsedSeconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            onComplete(elapsedSeconds, incorrectCount, totalAttempts)
        }
    }

    fun restartCurrentWord() {
        guessedLetters.clear()
    }

    fun handleLetterClick(letter: Char) {
        if (letter !in guessedLetters && !isWordSolved && !isGameOver) {
            totalAttempts++
            guessedLetters.add(letter)
            if (letter !in targetWord) {
                onIncorrectAttempt()
                triggerPopup(false)
            }
        }
    }

    val alphabetRows = listOf(
        "ABCDEFG".toList(),
        "HIJKLMN".toList(),
        "OPQRSTU".toList(),
        "VWXYZ".toList()
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_world4),
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
            Spacer(modifier = Modifier.height(16.dp))

            // Dedicated Top Bar Row with Score Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    border = BorderStroke(1.5.dp, CyberCyan)
                ) {
                    Text(
                        text = "⭐ $score Pts",
                        color = CyberCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Header Title
            Text(
                text = "🪓 AHORCADO: LA PALABRA SECRETA",
                color = CyberCyan,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Guide Cat Message Box
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

            Spacer(modifier = Modifier.height(12.dp))

            // Attempts Indicator Bar
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, if (remainingAttempts <= 2) CyberDanger else CyberCyan)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Intentos:",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        repeat(maxAttempts) { index ->
                            val isHeartActive = index < remainingAttempts
                            Text(
                                text = if (isHeartActive) "❤️" else "🖤",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Word Spaces Display
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                targetWord.forEach { letter ->
                    val isRevealed = letter in guessedLetters
                    Box(
                        modifier = Modifier
                            .size(width = 28.dp, height = 40.dp)
                            .background(
                                if (isRevealed) CyberSuccess.copy(alpha = 0.2f) else Color(0xFF1E1035),
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                2.dp,
                                if (isRevealed) CyberSuccess else CyberViolet,
                                RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isRevealed) letter.toString() else "_",
                            color = if (isRevealed) CyberSuccess else CyberCyan,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Keyboard
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
                                    .size(width = 38.dp, height = 44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(buttonBg)
                                    .border(
                                        1.dp,
                                        if (isUsed) Color.Transparent else CyberCyan.copy(alpha = 0.6f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable(enabled = !isUsed && !isWordSolved && !isGameOver) {
                                        handleLetterClick(letter)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter.toString(),
                                    color = textColor,
                                    fontSize = 15.sp,
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
                            text = "🎉 ¡DESAFÍO COMPLETADO! Has descubierto la palabra secreta.",
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

        // Floating Score Popup Overlay
        if (popupMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isPopupCorrect) {
                    ConfettiOverlay()
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CyberBackground,
                    border = BorderStroke(2.dp, if (isPopupCorrect) CyberSuccess else CyberDanger)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isPopupCorrect) "🎉 ¡CORRECTO!" else "❌ ¡INCORRECTO!",
                            color = if (isPopupCorrect) CyberSuccess else CyberDanger,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = popupMessage!!,
                            color = if (isPopupCorrect) Color.Yellow else Color.Red,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
