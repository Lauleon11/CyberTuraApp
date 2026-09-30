package me.lauraleon.cybertura.ui.screens.world4

import androidx.compose.runtime.Composable

@Composable
fun World4ScreenC(onNextClick: () -> Unit) {
    World4VideoPlayer(
        videoFileName = "mundo4_c",
        onNextClick = onNextClick,
    )
}
