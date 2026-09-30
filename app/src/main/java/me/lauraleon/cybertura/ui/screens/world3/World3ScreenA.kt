package me.lauraleon.cybertura.ui.screens.world3

import androidx.compose.runtime.Composable

@Composable
fun World3ScreenA(onNextClick: () -> Unit) {
    World3VideoPlayer(
        videoFileName = "mundo3_a",
        onNextClick = onNextClick,
    )
}
