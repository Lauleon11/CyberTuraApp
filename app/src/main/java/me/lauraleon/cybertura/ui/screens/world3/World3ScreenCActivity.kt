package me.lauraleon.cybertura.ui.screens.world3

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

private data class EducationalAction(
    val id: Int,
    val title: String,
    val description: String,
    val feedback: String
)

@Composable
fun World3ScreenCActivity(
    onNextClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Look for world3_puzzle_support resource in drawable, fallback to mundo3
    val imageResId = remember {
        val resId = context.resources.getIdentifier("world3_puzzle_support", "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.mundo3
    }

    // Crop source image into 9 real sub-bitmap pieces (3x3 grid)
    val pieceBitmaps = remember(imageResId) {
        try {
            val options = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
            val rawBitmap = BitmapFactory.decodeResource(context.resources, imageResId, options)
            if (rawBitmap != null) {
                val pieces = mutableListOf<ImageBitmap>()
                val pieceW = rawBitmap.width / 3
                val pieceH = rawBitmap.height / 3
                for (row in 0 until 3) {
                    for (col in 0 until 3) {
                        val sub = Bitmap.createBitmap(rawBitmap, col * pieceW, row * pieceH, pieceW, pieceH)
                        pieces.add(sub.asImageBitmap())
                    }
                }
                pieces
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Initial shuffled state guaranteed not solved
    val solvedList = remember { (0..8).toList() }
    val initialGrid = remember {
        listOf(2, 5, 0, 7, 4, 1, 8, 3, 6)
    }

    val currentGrid = remember { mutableStateListOf<Int>().apply { addAll(initialGrid) } }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val isPuzzleSolved by remember {
        derivedStateOf {
            currentGrid.toList() == solvedList
        }
    }

    // Educational actions post puzzle
    val educationalActions = remember {
        listOf(
            EducationalAction(
                1,
                "Opción 1 — Guardar pruebas",
                "Guardar capturas de los mensajes para mostrar lo ocurrido.",
                "Las pruebas pueden ayudar a explicar la situación a un adulto de confianza."
            ),
            EducationalAction(
                2,
                "Opción 2 — Bloquear y reportar",
                "Bloquear al agresor y reportar los mensajes en la plataforma.",
                "Bloquear y reportar puede ayudar a detener el contacto y avisar a la plataforma."
            ),
            EducationalAction(
                3,
                "Opción 3 — Contarle a un adulto de confianza",
                "Hablar con un familiar, profesor u otro adulto de confianza.",
                "No tienes que resolverlo a solas. Un adulto de confianza puede ayudarte a decidir qué hacer."
            )
        )
    }

    val selectedActionIds = remember { mutableStateListOf<Int>() }
    val isAllActionsCompleted by remember {
        derivedStateOf {
            selectedActionIds.size == educationalActions.size
        }
    }

    val isFullyCompleted by remember {
        derivedStateOf {
            isPuzzleSolved && isAllActionsCompleted
        }
    }

    fun restartPuzzle() {
        currentGrid.clear()
        currentGrid.addAll(listOf(2, 5, 0, 7, 4, 1, 8, 3, 6))
        selectedIndex = null
        selectedActionIds.clear()
    }

    fun handlePieceClick(clickedGridIndex: Int) {
        if (isPuzzleSolved) return

        val selected = selectedIndex
        if (selected == null) {
            selectedIndex = clickedGridIndex
        } else if (selected == clickedGridIndex) {
            selectedIndex = null
        } else {
            // Swap pieces in grid
            val temp = currentGrid[selected]
            currentGrid[selected] = currentGrid[clickedGridIndex]
            currentGrid[clickedGridIndex] = temp
            selectedIndex = null
        }
    }

    fun toggleAction(actionId: Int) {
        if (actionId !in selectedActionIds) {
            selectedActionIds.add(actionId)
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
                text = "🧩 ENCIENDE LA LUZ",
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
                        text = if (!isPuzzleSolved) "¡Reconstruye la escena intercambiando las piezas para llevar luz a quien lo necesita!" else "¡Ahora elige las 3 acciones para protegerte y apoyar!",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reset Button
            Button(
                onClick = { restartPuzzle() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray.copy(alpha = 0.7f)),
                border = BorderStroke(1.dp, CyberCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Text(
                    text = "🔄 REINICIAR ROMPECABEZAS",
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3x3 Puzzle Grid Container
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                    .border(
                        2.dp,
                        if (isPuzzleSolved) CyberSuccess else CyberViolet,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp)
            ) {
                if (isPuzzleSolved) {
                    // Show Full Unbroken Complete Image when solved
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Escena de Apoyo Completa",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (r in 0 until 3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (c in 0 until 3) {
                                    val gridIndex = r * 3 + c
                                    val originalPieceId = currentGrid[gridIndex]
                                    val isSelected = selectedIndex == gridIndex

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(3.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = if (isSelected) 3.dp else 1.dp,
                                                color = if (isSelected) CyberMagenta else CyberViolet.copy(alpha = 0.6f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { handlePieceClick(gridIndex) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (pieceBitmaps.size > originalPieceId) {
                                            Image(
                                                bitmap = pieceBitmaps[originalPieceId],
                                                contentDescription = "Pieza ${originalPieceId + 1}",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Text(
                                                text = "${originalPieceId + 1}",
                                                color = CyberCyan,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Post-puzzle Interactive Educational Activity
            if (isPuzzleSolved) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.85f),
                    border = BorderStroke(1.5.dp, CyberCyan),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "“¡Lo lograste, Guardián! Ahora puedes ayudar a alguien que está sufriendo por mensajes ofensivos. ¿Qué acciones podemos tomar para protegernos y ayudar?”",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress Indicator
                        Text(
                            text = "Acciones correctas: ${selectedActionIds.size} de ${educationalActions.size}",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3 Interactive Action Option Cards
                        educationalActions.forEach { action ->
                            val isSelected = action.id in selectedActionIds

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) CyberSuccess.copy(alpha = 0.2f) else Color(0xFF1E1035),
                                border = BorderStroke(
                                    1.5.dp,
                                    if (isSelected) CyberSuccess else CyberViolet
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { toggleAction(action.id) }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isSelected) "✅ ${action.title}" else "⚪ ${action.title}",
                                            color = if (isSelected) CyberSuccess else CyberCyan,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = action.description,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        lineHeight = 15.sp
                                    )

                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "💡 ${action.feedback}",
                                            color = CyberSuccess,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Completion Banner & Continuation Button
            AnimatedVisibility(
                visible = isFullyCompleted,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = CyberSuccess.copy(alpha = 0.15f),
                        border = BorderStroke(1.5.dp, CyberSuccess),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "✨ ¡ESCENA COMPLETAMENTE ILUMINADA!",
                                color = CyberSuccess,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "“¡Excelente, Guardián! Ayudar no significa pelear. Puedes guardar pruebas, bloquear y reportar, y contarle a un adulto de confianza. Tratar a los demás con respeto también es una forma de protegerlos”.",
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
                            text = "CONTINUAR A LA PANTALLA D",
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
