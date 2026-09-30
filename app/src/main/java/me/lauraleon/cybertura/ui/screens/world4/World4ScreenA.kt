package me.lauraleon.cybertura.ui.screens.world4

import androidx.compose.runtime.Composable

@Composable
fun World4ScreenA(onNextClick: () -> Unit) {
    World4VideoPlayer(
        videoFileName = "mundo4_a",
        onNextClick = onNextClick,
    )
}
