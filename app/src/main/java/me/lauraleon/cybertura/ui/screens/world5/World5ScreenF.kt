package me.lauraleon.cybertura.ui.screens.world5

import android.media.MediaPlayer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.R
import me.lauraleon.cybertura.ui.theme.*

@Composable
fun World5ScreenF(
    gender: String = "HOMBRE",
    costumeId: String = "spider",
    onSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }

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
            "spider" -> if (isMale) "m5_f_spidergato" else "m5_f_spidergata"
            "bat" -> if (isMale) "m5_f_batigato" else "m5_f_batigata"
            "super" -> if (isMale) "m5_f_supergato" else "m5_f_supergata"
            else -> if (isMale) "m5_f_spidergato" else "m5_f_spidergata"
        }
        val resId = context.resources.getIdentifier(bgName, "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.bg
    }

    val isCustomBgPresent = bgBattleRes != R.drawable.bg

    DisposableEffect(showErrorDialog) {
        var mp: MediaPlayer? = null
        if (showErrorDialog) {
            try {
                var resId = context.resources.getIdentifier("m5_error_f", "raw", context.packageName)
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
                        painter = painterResource(id = characterRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, CyberCyan, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = "Una cuenta desconocida quiere agregarte y tiene información extraña o que no coincide.",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!isCustomBgPresent) {
                Image(
                    painter = painterResource(id = characterRes),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )
                Spacer(modifier = Modifier.weight(0.5f))
            }

            Text(
                text = "¿Qué deberías hacer?",
                color = CyberCyan,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton5(
                        text = "A. Aceptarla inmediatamente.",
                        onClick = { showErrorDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    OptionButton5(
                        text = "B. Compartirle información personal.",
                        onClick = { showErrorDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton5(
                        text = "C. Verificar la cuenta antes de aceptarla.",
                        onClick = onSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    OptionButton5(
                        text = "D. Enviarle fotografías.",
                        onClick = { showErrorDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
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
                        text = "No debemos aceptar solicitudes de personas desconocidas sin verificar quién está detrás de la cuenta.",
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
