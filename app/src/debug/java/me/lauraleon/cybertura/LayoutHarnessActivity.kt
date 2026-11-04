package me.lauraleon.cybertura

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import me.lauraleon.cybertura.ui.screens.badges.HallOfBadgesScreen
import me.lauraleon.cybertura.ui.screens.rating.RatingScreen
import me.lauraleon.cybertura.ui.screens.screen1.Screen1
import me.lauraleon.cybertura.ui.screens.screen2.Screen2
import me.lauraleon.cybertura.ui.screens.screen3.Screen3
import me.lauraleon.cybertura.ui.screens.world1.*
import me.lauraleon.cybertura.ui.screens.world2.*
import me.lauraleon.cybertura.ui.screens.world3.*
import me.lauraleon.cybertura.ui.screens.world4.*
import me.lauraleon.cybertura.ui.screens.world5.*
import me.lauraleon.cybertura.ui.theme.CyberAppScaffold
import me.lauraleon.cybertura.ui.theme.CyberTuraTheme

class LayoutHarnessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        val screen = intent.getStringExtra("screen") ?: "S3"
        setContent {
            CyberTuraTheme {
                CyberAppScaffold { Harness(screen) }
            }
        }
    }
}

@Composable
private fun Harness(screen: String) {
    val g = "MUJER"
    val c = "spider"
    val s = 380
    when (screen) {
        "S1" -> Screen1(onStartAdventure = {})
        "S2" -> Screen2()
        "S3" -> Screen3(score = 195, unlockedWorldMax = 3, completedWorldIds = setOf(1, 2), hasPlayedIntroNarrative = true)
        "S3N" -> Screen3(score = 0, unlockedWorldMax = 1, hasPlayedIntroNarrative = false)
        "BADGES" -> HallOfBadgesScreen(unlockedWorldMax = 5)
        "RATING" -> RatingScreen()

        "W1A" -> World1ScreenAActivity(score = s)
        "W1B" -> World1ScreenBActivity(score = s)
        "W1C" -> World1ScreenCActivity(score = s)
        "W1E" -> World1ScreenE(gender = g, costumeId = c, score = s)
        "W1F" -> World1ScreenF(gender = g, costumeId = c, score = s)
        "W1G" -> World1ScreenG(gender = g, costumeId = c, score = s)
        "W1V" -> World1ScreenB(onNextClick = {})
        "W1H" -> World1ScreenH()

        "W2A" -> World2ScreenAActivity(score = s)
        "W2B" -> World2ScreenBActivity(score = s)
        "W2C" -> World2ScreenCActivity(score = s)
        "W2E" -> World2ScreenE(gender = g, costumeId = c, score = s)
        "W2F" -> World2ScreenF(gender = g, costumeId = c, score = s)
        "W2G" -> World2ScreenG(gender = g, costumeId = c, score = s)

        "W3A" -> World3ScreenAActivity(score = s)
        "W3B" -> World3ScreenBActivity(score = s)
        "W3C" -> World3ScreenCActivity(score = s)
        "W3E" -> World3ScreenE(gender = g, costumeId = c, score = s)
        "W3F" -> World3ScreenF(gender = g, costumeId = c, score = s)
        "W3G" -> World3ScreenG(gender = g, costumeId = c, score = s)

        "W4A" -> World4ScreenAActivity(score = s)
        "W4B" -> World4ScreenBActivity(score = s)
        "W4C" -> World4ScreenCActivity(score = s)
        "W4E" -> World4ScreenE(gender = g, costumeId = c, score = s)
        "W4F" -> World4ScreenF(gender = g, costumeId = c, score = s)
        "W4G" -> World4ScreenG(gender = g, costumeId = c, score = s)

        "W5A" -> World5ScreenAActivity(score = s)
        "W5B" -> World5ScreenBActivity(score = s)
        "W5C" -> World5ScreenCActivity(score = s)
        "W5E" -> World5ScreenE(gender = g, costumeId = c, score = s)
        "W5F" -> World5ScreenF(gender = g, costumeId = c, score = s)
        "W5G" -> World5ScreenG(gender = g, costumeId = c, score = s)
    }
}
