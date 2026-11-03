package me.lauraleon.cybertura.ui.screens.world2

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*
import kotlin.math.abs

private data class WordPosition(
    val word: String,
    val cells: List<Pair<Int, Int>>
)

@Composable
fun World2ScreenAActivity(
    score: Int = 0,
    onCorrectAnswer: () -> Unit = {},
    onIncorrectAttempt: () -> Unit = {},
    onComplete: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onNextClick: () -> Unit = {}
) {
    val startTime = remember { System.currentTimeMillis() }
    var errorCount by remember { mutableStateOf(0) }
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

    val targetWords = remember { listOf("SEGURA", "SIMBOLOS", "NUMEROS", "LONGITUD", "PRIVADA") }

    // Pre-calculated deterministic grid to ensure 100% accuracy and solvability
    val targetPositions = remember {
        listOf(
            WordPosition("SEGURA", (1..6).map { col -> Pair(0, col) }),
            WordPosition("SIMBOLOS", (1..8).map { row -> Pair(row, 0) }),
            WordPosition("NUMEROS", (2..8).map { col -> Pair(2, col) }),
            WordPosition("LONGITUD", (1..8).map { row -> Pair(row, 9) }),
            WordPosition("PRIVADA", (0..6).map { idx -> Pair(3 + idx, 1 + idx) })
        )
    }

    // Grid letters array 10x10
    val grid = remember {
        val rawGrid = Array(10) { CharArray(10) { ' ' } }

        // Distractor characters bank
        val distractors = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var seed = 73
        for (r in 0 until 10) {
            for (c in 0 until 10) {
                seed = (seed * 37 + 19) % 256
                rawGrid[r][c] = distractors[seed % distractors.length]
            }
        }

        // Place target words into grid
        targetPositions.forEach { pos ->
            pos.word.forEachIndexed { idx, ch ->
                val (r, c) = pos.cells[idx]
                rawGrid[r][c] = ch
            }
        }
        rawGrid
    }

    val foundWords = remember { mutableStateListOf<String>() }
    var startCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var currentSelection by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }

    val foundCoords = remember {
        derivedStateOf {
            targetPositions
                .filter { it.word in foundWords }
                .flatMap { it.cells }
                .toSet()
        }
    }

    val isAllFound = foundWords.size == targetWords.size

    LaunchedEffect(isAllFound) {
        if (isAllFound && !hasRewarded) {
            hasRewarded = true
            val elapsedSeconds = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            onComplete(elapsedSeconds, errorCount, totalAttempts)
        }
    }

    fun handleCellClick(row: Int, col: Int) {
        if (isAllFound) return

        val start = startCell
        if (start == null) {
            startCell = Pair(row, col)
            currentSelection = listOf(Pair(row, col))
        } else {
            val (r1, c1) = start
            val r2 = row
            val c2 = col

            val dr = r2 - r1
            val dc = c2 - c1

            val stepR = when {
                dr > 0 -> 1
                dr < 0 -> -1
                else -> 0
            }
            val stepC = when {
                dc > 0 -> 1
                dc < 0 -> -1
                else -> 0
            }

            val isHorizontal = dr == 0 && dc != 0
            val isVertical = dc == 0 && dr != 0
            val isDiagonal = abs(dr) == abs(dc) && dr != 0

            if (isHorizontal || isVertical || isDiagonal) {
                val steps = maxOf(abs(dr), abs(dc))
                val path = mutableListOf<Pair<Int, Int>>()
                var currR = r1
                var currC = c1
                for (step in 0..steps) {
                    path.add(Pair(currR, currC))
                    currR += stepR
                    currC += stepC
                }

                val selectedStr = path.map { (r, c) -> grid[r][c] }.joinToString("")
                val reverseStr = selectedStr.reversed()

                val matchedWord = targetWords.firstOrNull { word ->
                    (word == selectedStr || word == reverseStr) && word !in foundWords
                }

                if (matchedWord != null) {
                    totalAttempts++
                    foundWords.add(matchedWord)
                    onCorrectAnswer()
                    triggerPopup(true)
                } else {
                    errorCount++
                    totalAttempts++
                    onIncorrectAttempt()
                    triggerPopup(false)
                }
            } else {
                errorCount++
                totalAttempts++
                onIncorrectAttempt()
                triggerPopup(false)
            }

            startCell = null
            currentSelection = emptyList()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_world2),
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
                text = "🏰 LOS SECRETOS DE UNA BUENA CONTRASEÑA",
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
                    Text(
                        text = "¡Rompeclaves quiere adivinar las llaves de la fortaleza! Encuentra las 5 palabras secretas en la sopa de letras para reforzar los muros.",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, CyberCyan)
            ) {
                Text(
                    text = "${foundWords.size} de ${targetWords.size} palabras encontradas",
                    color = CyberCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Target Words Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                targetWords.forEach { word ->
                    val isFound = word in foundWords
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isFound) CyberSuccess.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.4f),
                        border = BorderStroke(
                            1.dp,
                            if (isFound) CyberSuccess else Color.Gray.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = word,
                            color = if (isFound) CyberSuccess else Color.LightGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (isFound) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 10x10 Word Search Grid
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                    .border(2.dp, CyberViolet, RoundedCornerShape(16.dp))
                    .padding(6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (r in 0 until 10) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (c in 0 until 10) {
                                val coord = Pair(r, c)
                                val isFoundCell = coord in foundCoords.value
                                val isStartCell = startCell == coord
                                val isSelectedCell = coord in currentSelection

                                val cellBg = when {
                                    isFoundCell -> CyberSuccess.copy(alpha = 0.85f)
                                    isStartCell -> CyberMagenta
                                    isSelectedCell -> CyberCyan.copy(alpha = 0.6f)
                                    else -> Color(0xFF1E1035)
                                }

                                val textColor = when {
                                    isFoundCell -> Color.Black
                                    isStartCell || isSelectedCell -> Color.White
                                    else -> CyberCyan
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(cellBg)
                                        .border(
                                            1.dp,
                                            if (isFoundCell) CyberSuccess else CyberViolet.copy(alpha = 0.5f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .clickable { handleCellClick(r, c) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = grid[r][c].toString(),
                                        color = textColor,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completion Banner and Continuation Button
            AnimatedVisibility(
                visible = isAllFound,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CyberSuccess.copy(alpha = 0.2f),
                        border = BorderStroke(1.5.dp, CyberSuccess),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔓 ¡CERRADURA DESBLOQUEADA!",
                                color = CyberSuccess,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "“¡Excelente trabajo, Guardián! Una contraseña segura debe ser difícil de adivinar. Recuerda mantenerla en secreto y no compartirla con otras personas”.",
                                color = Color.White,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
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
