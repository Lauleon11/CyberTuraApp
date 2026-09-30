package me.lauraleon.cybertura.ui.screens.world1

import androidx.compose.runtime.Composable

@Composable
fun World1ScreenD(onNextClick: () -> Unit) {
    World1VideoPlayer(
        videoFileName = "mundo1_d",
        onNextClick = onNextClick,
    )
}
