package me.lauraleon.cybertura.ui.screens.world4

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

private data class ClassificationCard(
    val id: Int,
    val title: String,
    val description: String,
    val correctCategory: Int, // 1: Carpeta Protegida, 2: Se puede compartir
    val feedback: String
)

@Composable
fun World4ScreenCActivity(
    onNextClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Look for world4_puzzle_secret_folder resource in drawable, fallback to mundo4
    val imageResId = remember {
        val resId = context.resources.getIdentifier("puzzle4", "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.puzzle4
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

    // Phase 2 Classification Cards
    val classificationCards = remember {
        listOf(
            ClassificationCard(1, "Contraseña personal", "Una contraseña que permite entrar a tu cuenta.", 1, "Las contraseñas deben mantenerse en secreto. No las compartas con amigos ni con desconocidos."),
            ClassificationCard(2, "Dirección de casa", "La dirección del lugar donde vives.", 1, "Tu dirección permite localizarte. No la publiques ni la compartas con personas desconocidas."),
            ClassificationCard(3, "Número de teléfono", "Tu número de contacto personal.", 1, "Tu número es un dato personal. Antes de compartirlo, consulta con un adulto de confianza."),
            ClassificationCard(4, "Fotografía personal", "Una foto donde se ve claramente tu rostro.", 1, "Antes de publicar una fotografía, piensa quién podrá verla y consulta con un adulto de confianza."),
            ClassificationCard(5, "Ubicación en tiempo real", "Tu ubicación exacta en este momento.", 1, "Compartir tu ubicación exacta puede revelar dónde estás. Evita publicarla con personas desconocidas."),
            ClassificationCard(6, "Afición general", "Me gusta dibujar y jugar videojuegos (sin datos personales).", 2, "Compartir gustos generales está bien si no revelas información personal ni comprometes tu seguridad."),
            ClassificationCard(7, "Consejo de seguridad", "No abras enlaces sospechosos.", 2, "Los consejos de seguridad ayudan a otras personas a navegar de forma más segura."),
            ClassificationCard(8, "Mascota ficticia", "Mi mascota imaginaria se llama Copito.", 2, "Los ejemplos ficticios pueden compartirse si no revelan datos reales ni respuestas de seguridad.")
        )
    }

    val classifiedCardMap = remember { mutableStateMapOf<Int, Int>() } // CardId -> Category (1 or 2)
    var activeCardId by remember { mutableStateOf<Int?>(null) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var isFeedbackPositive by remember { mutableStateOf(true) }

    val isAllCardsClassified by remember {
        derivedStateOf {
            classificationCards.all { card -> classifiedCardMap[card.id] == card.correctCategory }
        }
    }

    val isFullyCompleted by remember {
        derivedStateOf {
            isPuzzleSolved && isAllCardsClassified
        }
    }

    fun restartPuzzle() {
        currentGrid.clear()
        currentGrid.addAll(listOf(2, 5, 0, 7, 4, 1, 8, 3, 6))
        selectedIndex = null
        classifiedCardMap.clear()
        activeCardId = null
        feedbackMessage = null
    }

    fun handlePieceClick(clickedGridIndex: Int) {
        if (isPuzzleSolved) return

        val selected = selectedIndex
        if (selected == null) {
            selectedIndex = clickedGridIndex
        } else if (selected == clickedGridIndex) {
            selectedIndex = null
        } else {
            val temp = currentGrid[selected]
            currentGrid[selected] = currentGrid[clickedGridIndex]
            currentGrid[clickedGridIndex] = temp
            selectedIndex = null
        }
    }

    fun handleCategorySelect(targetCategory: Int) {
        val cardId = activeCardId ?: return
        val card = classificationCards.firstOrNull { it.id == cardId } ?: return

        if (card.correctCategory == targetCategory) {
            classifiedCardMap[card.id] = targetCategory
            feedbackMessage = "✅ ${card.feedback}"
            isFeedbackPositive = true
            activeCardId = null
        } else {
            feedbackMessage = "⚠️ Esta información debe clasificarse con más cuidado. Inténtalo de nuevo."
            isFeedbackPositive = false
        }
    }

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
            Spacer(modifier = Modifier.height(36.dp))

            // Header Title
            Text(
                text = "📁 CIERRA EL ARCHIVO SECRETO",
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
                        text = if (!isPuzzleSolved) "¡Arma la carpeta del archivo secreto intercambiando las piezas!" else "¡Ahora clasifica cada tarjeta en la zona correcta!",
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
                    text = "🔄 REINICIAR ACTIVIDAD",
                    color = CyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3x3 Puzzle Grid Container (Phase 1)
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
                        contentDescription = "Carpeta Secreta Completa",
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

            // Phase 2: Classification Activity
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
                            text = "“¡La carpeta está lista, Guardián! Ahora vamos a descubrir qué información debemos mantener protegida.”",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress Indicator
                        val correctClassifiedCount = classificationCards.count { classifiedCardMap[it.id] == it.correctCategory }
                        Text(
                            text = "Clasificadas correctamente: $correctClassifiedCount de ${classificationCards.size}",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Destination Category Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { handleCategorySelect(1) },
                                enabled = activeCardId != null,
                                colors = ButtonDefaults.buttonColors(containerColor = CyberViolet),
                                border = BorderStroke(1.dp, CyberCyan),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text(
                                    text = "📂 Carpeta Protegida",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Button(
                                onClick = { handleCategorySelect(2) },
                                enabled = activeCardId != null,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1035)),
                                border = BorderStroke(1.dp, CyberSuccess),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text(
                                    text = "🌐 Se puede compartir",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Feedback message
                        feedbackMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isFeedbackPositive) CyberSuccess.copy(alpha = 0.15f) else CyberDanger.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, if (isFeedbackPositive) CyberSuccess else CyberDanger),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = msg,
                                    color = if (isFeedbackPositive) CyberSuccess else CyberDanger,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Cards Selection List
                        classificationCards.forEach { card ->
                            val currentCategory = classifiedCardMap[card.id]
                            val isClassified = currentCategory == card.correctCategory
                            val isActive = activeCardId == card.id

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when {
                                    isClassified -> CyberSuccess.copy(alpha = 0.15f)
                                    isActive -> CyberMagenta.copy(alpha = 0.3f)
                                    else -> Color(0xFF1E1035)
                                },
                                border = BorderStroke(
                                    1.5.dp,
                                    when {
                                        isClassified -> CyberSuccess
                                        isActive -> CyberMagenta
                                        else -> CyberViolet
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = !isClassified) {
                                        activeCardId = card.id
                                        feedbackMessage = "Toca un botón de arriba (📂 Carpeta Protegida / 🌐 Se puede compartir) para clasificar esta tarjeta."
                                        isFeedbackPositive = true
                                    }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = when {
                                                isClassified && currentCategory == 1 -> "📂 ${card.title} (Protegido)"
                                                isClassified && currentCategory == 2 -> "🌐 ${card.title} (Compartible)"
                                                isActive -> "👉 ${card.title} (Seleccionada)"
                                                else -> "🎴 ${card.title}"
                                            },
                                            color = when {
                                                isClassified -> CyberSuccess
                                                isActive -> CyberMagenta
                                                else -> CyberCyan
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = card.description,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp
                                    )
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
                                text = "🔒 ¡ARCHIVO SECRETO Y DATOS PROTEGIDOS!",
                                color = CyberSuccess,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "“¡Lo lograste, Guardián! No toda la información debe compartirse con cualquiera. Protege tus contraseñas, dirección, teléfono, fotografías personales y ubicación. Antes de publicar algo, piensa quién podrá verlo y pide ayuda a un adulto de confianza si tienes dudas.”",
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
    }
}
