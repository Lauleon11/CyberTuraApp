package me.lauraleon.cybertura.ui.screens.world2

import androidx.compose.runtime.Composable

@Composable
fun World2ScreenC(onNextClick: () -> Unit) {
    World2VideoPlayer(
        videoFileName = "mundo2_c",
        onNextClick = onNextClick,
    )
}
