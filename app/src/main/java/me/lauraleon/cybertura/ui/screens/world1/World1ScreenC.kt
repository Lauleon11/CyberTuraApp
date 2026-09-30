package me.lauraleon.cybertura.ui.screens.world1

import androidx.compose.runtime.Composable

@Composable
fun World1ScreenC(onNextClick: () -> Unit) {
    World1VideoPlayer(
        videoFileName = "mundo1_c",
        onNextClick = onNextClick,
    )
}
