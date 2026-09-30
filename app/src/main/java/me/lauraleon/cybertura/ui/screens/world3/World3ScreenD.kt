package me.lauraleon.cybertura.ui.screens.world3

import androidx.compose.runtime.Composable

@Composable
fun World3ScreenD(onNextClick: () -> Unit) {
    World3VideoPlayer(
        videoFileName = "mundo3_d",
        onNextClick = onNextClick,
    )
}
