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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*
import kotlin.math.abs

private data class WordPosition(
    val word: String,
    val cells: List<Pair<Int, Int>>
)

@Composable
fun World5ScreenAActivity(
    onNextClick: () -> Unit = {}
) {
    val targetWords = remember { listOf("IDENTIDAD", "HUELLA", "PERFIL", "RESPETO", "SEGURIDAD") }

    // Pre-calculated deterministic grid to ensure 100% accuracy and solvability
    val targetPositions = remember {
        listOf(
            WordPosition("IDENTIDAD", (1..9).map { col -> Pair(0, col) }),
            WordPosition("HUELLA", (1..6).map { row -> Pair(row, 0) }),
            WordPosition("SEGURIDAD", (1..9).map { idx -> Pair(idx, idx) }),
            WordPosition("PERFIL", (1..6).map { col -> Pair(8, col) }),
            WordPosition("RESPETO", (2..8).map { row -> Pair(row, 9) })
        )
    }

    // Grid letters array 10x10
    val grid = remember {
        val rawGrid = Array(10) { CharArray(10) { ' ' } }

        // Distractor characters bank
        val distractors = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var seed = 149
        for (r in 0 until 10) {
            for (c in 0 until 10) {
                seed = (seed * 47 + 31) % 256
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
                    foundWords.add(matchedWord)
                }
            }

            startCell = null
            currentSelection = emptyList()
        }
    }

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
                text = "🎭 SOPA DE LETRAS: RASTREANDO LA IDENTIDAD",
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
                        text = "¡El Suplantador usa máscaras falsas! Encuentra las 5 palabras para encender las huellas digitales e identificar el perfil real.",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footprint Illumination Progress Indicator
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, if (isAllFound) CyberSuccess else CyberCyan)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isAllFound) "👣 Huella Digital Totalmente Iluminada" else "👣 Huella Digital: ${foundWords.size}/5",
                        color = if (isAllFound) CyberSuccess else CyberCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
                                text = "👣 ¡HUELLA DIGITAL RASTREADA Y SEGURA!",
                                color = CyberSuccess,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "“¡Muy bien, Guardián! Lo que publicamos en internet puede dejar un rastro. Por eso, antes de compartir una foto, un comentario o información personal, debemos pensar quién podrá verlo”.",
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
                            text = "CONTINUAR A LA PANTALLA B",
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
