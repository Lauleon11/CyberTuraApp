package me.lauraleon.cybertura

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import me.lauraleon.cybertura.ui.navigation.CyberturaNavGraph
import me.lauraleon.cybertura.ui.theme.CyberAppScaffold
import me.lauraleon.cybertura.ui.theme.CyberTuraTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The app is always dark, so the system bar icons must stay light in any system theme.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            CyberTuraTheme {
                CyberAppScaffold {
                    CyberturaNavGraph()
                }
            }
        }
    }
}
