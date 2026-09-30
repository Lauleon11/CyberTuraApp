package me.lauraleon.cybertura.ui.screens.world4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.lauraleon.cybertura.ui.theme.CyberCyan

@Composable
fun World4ScreenB(onNextClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "MIS DATOS = INFORMACIÓN QUE DEBO PROTEGER",
            color = CyberCyan,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        World4VideoPlayer(
            videoFileName = "mundo4_b",
            onNextClick = onNextClick,
            modifier = Modifier.weight(1f),
        )
    }
}
