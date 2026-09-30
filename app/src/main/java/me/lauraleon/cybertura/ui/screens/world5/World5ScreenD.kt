package me.lauraleon.cybertura.ui.screens.world5

import androidx.compose.runtime.Composable

@Composable
fun World5ScreenD(onNextClick: () -> Unit) {
    World5VideoPlayer(
        videoFileName = "mundo5_d",
        onNextClick = onNextClick,
    )
}
