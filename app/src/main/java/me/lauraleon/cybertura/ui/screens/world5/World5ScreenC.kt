package me.lauraleon.cybertura.ui.screens.world5

import androidx.compose.runtime.Composable

@Composable
fun World5ScreenC(onNextClick: () -> Unit) {
    World5VideoPlayer(
        videoFileName = "mundo5_c",
        onNextClick = onNextClick,
    )
}
