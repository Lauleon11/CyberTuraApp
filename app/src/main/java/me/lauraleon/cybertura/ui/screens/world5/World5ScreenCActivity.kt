package me.lauraleon.cybertura.ui.screens.world5

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

private data class PostItem(
    val id: Int,
    val title: String,
    val text: String,
    val correctCategory: Int, // 1: Conviene pensarlo mejor, 2: Publicación apropiada
    val feedback: String
)

@Composable
fun World5ScreenCActivity(
    onNextClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Look for world5_puzzle_digital_footprint resource in drawable, fallback to mundo5
    val imageResId = remember {
        val resId = context.resources.getIdentifier("world5_puzzle_digital_footprint", "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.mundo5
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

    // Phase 2 Social Posts to analyze
    val postsList = remember {
        listOf(
            PostItem(
                1,
                "Publicación 1 — Ubicación exacta",
                "“¡Estoy solo en casa! Esta es mi dirección y pueden venir cuando quieran.”",
                1,
                "Publicar tu dirección y decir que estás solo puede poner en riesgo tu seguridad. Evita compartir tu ubicación exacta públicamente."
            ),
            PostItem(
                2,
                "Publicación 2 — Compartir contraseña",
                "“Esta es mi contraseña para que todos puedan entrar a mi cuenta.”",
                1,
                "Las contraseñas son privadas. Nunca las publiques ni las compartas con otras personas."
            ),
            PostItem(
                3,
                "Publicación 3 — Foto de otra persona sin permiso",
                "“Voy a publicar esta foto de mi compañero para que todos se rían.”",
                1,
                "Antes de publicar una fotografía de otra persona, respeta su privacidad y pide su permiso. No compartas contenido para burlarte de alguien."
            ),
            PostItem(
                4,
                "Publicación 4 — Afición general",
                "“Me gusta dibujar, pintar y jugar videojuegos.”",
                2,
                "Compartir tus gustos puede estar bien si no incluyes datos personales que permitan localizarte o identificarte de forma insegura."
            ),
            PostItem(
                5,
                "Publicación 5 — Consejo de seguridad",
                "“Recuerda verificar los perfiles antes de confiar en ellos.”",
                2,
                "Compartir consejos de seguridad puede ayudar a otras personas a protegerse en internet."
            ),
            PostItem(
                6,
                "Publicación 6 — Logro creativo",
                "“¡Terminé un dibujo que me tomó toda la tarde! Me encanta cómo quedó.”",
                2,
                "Compartir tus creaciones puede ser positivo. Antes de publicar una imagen, revisa que no muestre tu dirección, documentos, ubicación u otros datos privados."
            )
        )
    }

    val classifiedPostMap = remember { mutableStateMapOf<Int, Int>() } // PostId -> SelectedCategory
    val feedbackMap = remember { mutableStateMapOf<Int, String>() }

    val isAllPostsClassified by remember {
        derivedStateOf {
            postsList.all { post -> classifiedPostMap[post.id] == post.correctCategory }
        }
    }

    val isFullyCompleted by remember {
        derivedStateOf {
            isPuzzleSolved && isAllPostsClassified
        }
    }

    fun restartPuzzle() {
        currentGrid.clear()
        currentGrid.addAll(listOf(2, 5, 0, 7, 4, 1, 8, 3, 6))
        selectedIndex = null
        classifiedPostMap.clear()
        feedbackMap.clear()
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

    fun handleClassifyPost(postId: Int, categoryChosen: Int) {
        val post = postsList.firstOrNull { it.id == postId } ?: return
        if (post.correctCategory == categoryChosen) {
            classifiedPostMap[postId] = categoryChosen
            feedbackMap[postId] = "✅ ${post.feedback}"
        } else {
            feedbackMap[postId] = "⚠️ Esta publicación debe evaluarse con más cuidado. Lee de nuevo el mensaje e inténtalo."
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_world5),
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
                text = "🧩 RECONSTRUYE TU HUELLA DIGITAL",
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
                        text = if (!isPuzzleSolved) "¡Arma el perfil seguro intercambiando las piezas para proteger la huella digital!" else "¡Ahora analiza las 6 publicaciones para evaluar la huella digital que dejamos!",
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
                        contentDescription = "Perfil Seguro Completo",
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

            // Phase 2: Post Analysis Activity
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
                            text = "“¡Muy bien, Guardián! Has reconstruido un perfil protegido. Ahora vamos a descubrir qué publicaciones conviene pensar dos veces antes de compartir.”",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress Indicator
                        val correctCount = postsList.count { classifiedPostMap[it.id] == it.correctCategory }
                        Text(
                            text = "Publicaciones analizadas correctamente: $correctCount de ${postsList.size}",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 6 Posts Evaluation Cards
                        postsList.forEach { post ->
                            val currentChoice = classifiedPostMap[post.id]
                            val isCorrect = currentChoice == post.correctCategory
                            val feedback = feedbackMap[post.id]

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isCorrect) CyberSuccess.copy(alpha = 0.15f) else Color(0xFF1E1035),
                                border = BorderStroke(
                                    1.5.dp,
                                    if (isCorrect) CyberSuccess else CyberViolet
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (isCorrect) "✅ ${post.title}" else "📱 ${post.title}",
                                        color = if (isCorrect) CyberSuccess else CyberCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = post.text,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Option Buttons
                                    if (!isCorrect) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Button(
                                                onClick = { handleClassifyPost(post.id, 1) },
                                                colors = ButtonDefaults.buttonColors(containerColor = CyberViolet),
                                                border = BorderStroke(1.dp, CyberMagenta),
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                                                modifier = Modifier.weight(1f).height(38.dp)
                                            ) {
                                                Text(
                                                    text = "⚠️ Pensarlo mejor",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Button(
                                                onClick = { handleClassifyPost(post.id, 2) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1035)),
                                                border = BorderStroke(1.dp, CyberSuccess),
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                                                modifier = Modifier.weight(1f).height(38.dp)
                                            ) {
                                                Text(
                                                    text = "✅ Publicación apropiada",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    // Feedback message
                                    feedback?.let { msg ->
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = msg,
                                            color = if (isCorrect) CyberSuccess else CyberDanger,
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

            // Completion Banner & Final Educational Message
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
                                text = "✨ ¡HUELLA DIGITAL TOTALMENTE PROTEGIDA!",
                                color = CyberSuccess,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "“¡Lo lograste, Guardián! Todo lo que publicamos puede dejar una huella digital. Antes de compartir una foto, un comentario o información personal, piensa quién podrá verlo y cómo podría afectar tu privacidad o la de otras personas. Recuerda que el contenido puede permanecer en internet incluso después de eliminarlo. ¡Publica con responsabilidad!”",
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
