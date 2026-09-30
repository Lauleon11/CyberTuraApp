package me.lauraleon.cybertura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import me.lauraleon.cybertura.ui.navigation.CyberturaNavGraph
import me.lauraleon.cybertura.ui.theme.CyberTuraTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CyberTuraTheme {
                CyberturaNavGraph()
            }
        }
    }
}
