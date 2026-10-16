package me.lauraleon.cybertura.ui.screens.screen3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.domain.model.World
import me.lauraleon.cybertura.ui.theme.CyberCyan
import me.lauraleon.cybertura.ui.theme.CyberSuccess
import me.lauraleon.cybertura.ui.theme.CyberTuraTheme
import me.lauraleon.cybertura.ui.theme.CyberViolet

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.media.MediaPlayer
import kotlinx.coroutines.delay

@Composable
fun Screen3(
    score: Int = 0,
    unlockedWorldMax: Int = 1,
    completedWorldIds: Set<Int> = emptySet(),
    hasPlayedIntroNarrative: Boolean = false,
    onNarrativePlayed: () -> Unit = {},
    onWorldClick: (World) -> Unit = {},
    onBadgesClick: () -> Unit = {}
) {
    var showNarrative by remember { mutableStateOf(!hasPlayedIntroNarrative) }
    val context = LocalContext.current

    LaunchedEffect(hasPlayedIntroNarrative) {
        if (hasPlayedIntroNarrative) {
            showNarrative = false
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPlayedIntroNarrative) {
            try {
                val resId = context.resources.getIdentifier("narrativa_inicio", "raw", context.packageName)
                if (resId != 0) {
                    val mediaPlayer = MediaPlayer.create(context, resId)
                    mediaPlayer.start()
                    var audioFinished = false
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                        audioFinished = true
                    }
                    
                    while (!audioFinished) {
                        delay(500)
                    }
                    
                    delay(2000)
                    showNarrative = false
                    onNarrativePlayed()
                } else {
                    delay(15000)
                    showNarrative = false
                    onNarrativePlayed()
                }
            } catch (e: Exception) {
                delay(5000)
                showNarrative = false
                onNarrativePlayed()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Top Bar with Title and Badge Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MUNDOS DIGITALES",
                    color = CyberCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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

                    IconButton(
                        onClick = onBadgesClick,
                        modifier = Modifier
                            .size(48.dp)
                            .background(CyberViolet.copy(alpha = 0.5f), CircleShape)
                            .border(2.dp, CyberCyan, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.mundo1),
                            contentDescription = "Insignias",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Worlds Grid
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Row 1: Worlds 1 & 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val w1Completed = 1 in completedWorldIds || 1 < unlockedWorldMax
                    val w1Locked = 1 > unlockedWorldMax && !w1Completed
                    WorldButton(
                        world = World.ALL[0],
                        isLocked = w1Locked,
                        isCompleted = w1Completed,
                        onClick = { onWorldClick(World.ALL[0]) },
                        modifier = Modifier.weight(1f)
                    )

                    val w2Completed = 2 in completedWorldIds || 2 < unlockedWorldMax
                    val w2Locked = 2 > unlockedWorldMax && !w2Completed
                    WorldButton(
                        world = World.ALL[1],
                        isLocked = w2Locked,
                        isCompleted = w2Completed,
                        onClick = { onWorldClick(World.ALL[1]) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Worlds 3 & 4
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val w3Completed = 3 in completedWorldIds || 3 < unlockedWorldMax
                    val w3Locked = 3 > unlockedWorldMax && !w3Completed
                    WorldButton(
                        world = World.ALL[2],
                        isLocked = w3Locked,
                        isCompleted = w3Completed,
                        onClick = { onWorldClick(World.ALL[2]) },
                        modifier = Modifier.weight(1f)
                    )

                    val w4Completed = 4 in completedWorldIds || 4 < unlockedWorldMax
                    val w4Locked = 4 > unlockedWorldMax && !w4Completed
                    WorldButton(
                        world = World.ALL[3],
                        isLocked = w4Locked,
                        isCompleted = w4Completed,
                        onClick = { onWorldClick(World.ALL[3]) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: World 5 centered
                val w5Completed = 5 in completedWorldIds
                val w5Locked = 5 > unlockedWorldMax && !w5Completed
                WorldButton(
                    world = World.ALL[4],
                    isLocked = w5Locked,
                    isCompleted = w5Completed,
                    onClick = { onWorldClick(World.ALL[4]) },
                    modifier = Modifier
                        .width(160.dp)
                        .aspectRatio(1f)
                )
            }
        }

        // Narrative Box
        AnimatedVisibility(
            visible = showNarrative,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            NarrativeBox(
                text = "¡Ya estamos listos! Cinco enemigos han tomado el control de diferentes mundos digitales. Para detenerlos tendremos que viajar de mundo en mundo. Pero existe una regla: un portal solo se abrirá cuando hayas completado la misión del mundo anterior. Tendrás que superar desafíos, aprender de tus errores y utilizar tus conocimientos para debilitar al enemigo. Cuando completes una misión, recibirás una insignia y el portal hacia el siguiente mundo se desbloqueará. Nuestro primer destino nos espera. ¡Vamos!"
            )
        }
    }
}

@Composable
fun WorldButton(
    world: World,
    isLocked: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageRes = when (world.id) {
        1 -> R.drawable.mundo1
        2 -> R.drawable.mundo2
        3 -> R.drawable.mundo3
        4 -> R.drawable.mundo4
        5 -> R.drawable.mundo5
        else -> R.drawable.mundo1
    }

    val isClickable = !isLocked && !isCompleted

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(enabled = isClickable) { onClick() }
            .border(
                width = 2.dp,
                color = when {
                    isCompleted -> CyberSuccess
                    isLocked -> Color.Gray.copy(alpha = 0.5f)
                    else -> CyberViolet
                },
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isClickable) 8.dp else 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = world.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = if (isClickable) 1f else 0.4f
            )

            // Locked Overlay
            if (isLocked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Completed Overlay
            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "✅",
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberSuccess.copy(alpha = 0.85f)
                        ) {
                            Text(
                                text = "COMPLETADO",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Text overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
            )

            Text(
                text = world.id.toString(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun NarrativeBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
            .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cat Avatar
            Image(
                painter = painterResource(id = R.drawable.gato_guia),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, CyberCyan, CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = text,
                color = Color.White,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Screen3Preview() {
    CyberTuraTheme {
        Screen3()
    }
}
