package me.lauraleon.cybertura.ui.screens.world5

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
fun World5ScreenB(onNextClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "MI IDENTIDAD DIGITAL = LO QUE REPRESENTO EN INTERNET",
            color = CyberCyan,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        World5VideoPlayer(
            videoFileName = "mundo5_b",
            onNextClick = onNextClick,
            modifier = Modifier.weight(1f),
        )
    }
}
