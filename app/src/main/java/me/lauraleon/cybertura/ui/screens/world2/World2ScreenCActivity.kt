package me.lauraleon.cybertura.ui.screens.world2

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

@Composable
fun World2ScreenCActivity(
    onNextClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Look for world2_puzzle_lock resource in drawable, fallback to mundo2
    val imageResId = remember {
        val resId = context.resources.getIdentifier("world2_puzzle_lock", "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.mundo2
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

    val isSolved by remember {
        derivedStateOf {
            currentGrid.toList() == solvedList
        }
    }

    fun restartPuzzle() {
        currentGrid.clear()
        currentGrid.addAll(listOf(2, 5, 0, 7, 4, 1, 8, 3, 6))
        selectedIndex = null
    }

    fun handlePieceClick(clickedGridIndex: Int) {
        if (isSolved) return

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
                text = "🧩 CONSTRUYE LA CERRADURA SEGURA",
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
                        text = "¡Arma la cerradura combinando letras, números y símbolos! Toca dos piezas para intercambiar sus posiciones.",
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
                        if (isSolved) CyberSuccess else CyberViolet,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp)
            ) {
                if (isSolved) {
                    // Show Full Unbroken Complete Image when solved
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Cerradura Completa",
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

            // Completion Banner & Educational Message
            AnimatedVisibility(
                visible = isSolved,
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
                                text = "🔒 ¡CERRADURA SEGURA CONSTRUIDA!",
                                color = CyberSuccess,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "“¡Lo lograste, Guardián! Una contraseña segura debe ser difícil de adivinar. Evita utilizar tu nombre o tu fecha de cumpleaños, porque otras personas podrían conocer esos datos. Combina letras, números y símbolos, utiliza una longitud adecuada y no reutilices la misma contraseña en todas tus cuentas. ¡Y recuerda mantenerla en secreto!”",
                                color = Color.White,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Bad Examples Warning Chip
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CyberDanger.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, CyberDanger)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "⚠️ Evita contraseñas débiles como:",
                                        color = CyberDanger,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "❌ minombre123    ❌ fechadecumpleaños",
                                        color = Color.LightGray,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
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
