package me.lauraleon.cybertura.ui.screens.world3

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

@Composable
fun World3ScreenE(
    gender: String = "HOMBRE",
    costumeId: String = "spider",
    score: Int = 0,
    onCorrectAnswer: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }
    var hasAnsweredCorrectly by remember { mutableStateOf(false) }
    var showCorrectPopup by remember { mutableStateOf(false) }

    LaunchedEffect(showCorrectPopup) {
        if (showCorrectPopup) {
            delay(1500)
            showCorrectPopup = false
            onSuccess()
        }
    }

    val characterRes = remember(gender, costumeId) {
        val isMale = gender.equals("HOMBRE", ignoreCase = true) || gender.contains("HOMBRE", ignoreCase = true)
        when (costumeId.lowercase()) {
            "spider" -> if (isMale) R.drawable.spidergato else R.drawable.spidergata
            "bat" -> if (isMale) R.drawable.batigato else R.drawable.batigata
            "super" -> if (isMale) R.drawable.supergato else R.drawable.supergata
            else -> if (isMale) R.drawable.spidergato else R.drawable.spidergata
        }
    }

    val bgBattleRes = remember(gender, costumeId) {
        val isMale = gender.equals("HOMBRE", ignoreCase = true) || gender.contains("HOMBRE", ignoreCase = true)
        val bgName = when (costumeId.lowercase()) {
            "spider" -> if (isMale) "m3_e_spidergato" else "m3_e_spidergata"
            "bat" -> if (isMale) "m3_e_batigato" else "m3_e_batigata"
            "super" -> if (isMale) "m3_e_supergato" else "m3_e_supergata"
            else -> if (isMale) "m3_e_spidergato" else "m3_e_spidergata"
        }
        val resId = context.resources.getIdentifier(bgName, "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.bg
    }

    val isCustomBgPresent = bgBattleRes != R.drawable.bg

    DisposableEffect(showErrorDialog) {
        var mp: MediaPlayer? = null
        if (showErrorDialog) {
            try {
                var resId = context.resources.getIdentifier("m3_error_e", "raw", context.packageName)
                if (resId == 0) {
                    resId = context.resources.getIdentifier("phishing_error", "raw", context.packageName)
                }
                if (resId != 0) {
                    mp = MediaPlayer.create(context, resId)?.apply {
                        start()
                        setOnCompletionListener {
                            it.release()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        onDispose {
            try {
                mp?.stop()
                mp?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = bgBattleRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            if (!isCustomBgPresent) {
                Image(
                    painter = painterResource(id = characterRes),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )
                Spacer(modifier = Modifier.weight(0.5f))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Situation Text Box (Above question)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
                    .border(2.dp, CyberMagenta, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gato_guia),
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, CyberCyan, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = "Un compañero recibe constantemente mensajes ofensivos en un grupo.",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question Text wrapped in Box with black background and CyberCyan border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(16.dp))
                    .border(2.dp, CyberCyan, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¿Qué debería hacer?",
                    color = CyberCyan,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton3(
                        text = "A. Responder con insultos.",
                        onClick = { showErrorDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    OptionButton3(
                        text = "B. Compartir los mensajes para burlarse también.",
                        onClick = { showErrorDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton3(
                        text = "C. Guardar evidencia, bloquear y reportar, y pedir ayuda a un adulto de confianza.",
                        onClick = {
                            if (!hasAnsweredCorrectly) {
                                hasAnsweredCorrectly = true
                                showCorrectPopup = true
                                onCorrectAnswer()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    OptionButton3(
                        text = "D. Crear otro grupo para burlarse de la persona.",
                        onClick = { showErrorDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Top-right points badge
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
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

        // Correct Answer Popup Overlay
        if (showCorrectPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(CyberBackground, RoundedCornerShape(20.dp))
                        .border(2.dp, CyberSuccess, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Text(
                        text = "🎉 ¡CORRECTO!",
                        color = CyberSuccess,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "+10 PUNTOS",
                        color = Color.Yellow,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (showErrorDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(CyberBackground, RoundedCornerShape(20.dp))
                        .border(2.dp, CyberDanger, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Text(
                        text = "¡Cuidado, Guardián!",
                        color = CyberDanger,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "La respuesta correcta es C.",
                        color = CyberCyan,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Responder con más agresiones puede empeorar la situación. Es mejor guardar evidencia, bloquear, reportar y buscar ayuda.",
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { showErrorDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberDanger),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "ENTENDIDO",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OptionButton3(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(85.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.Black.copy(alpha = 0.65f),
        border = BorderStroke(1.5.dp, CyberViolet)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
