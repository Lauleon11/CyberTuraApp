package me.lauraleon.cybertura.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import java.util.Random

@Composable
fun ConfettiOverlay() {
    val particleCount = 50
    val random = remember { Random(42) }
    val colors = listOf(CyberCyan, CyberMagenta, Color.Yellow, CyberSuccess, CyberViolet, Color.Red, Color.Green, Color.Cyan)

    data class Particle(
        val x: Float,
        var y: Float,
        val speedY: Float,
        val speedX: Float,
        val size: Float,
        val color: Color,
        val shapeIsCircle: Boolean
    )

    val particles = remember {
        mutableStateListOf<Particle>().apply {
            for (i in 0 until particleCount) {
                add(
                    Particle(
                        x = random.nextFloat() * 1000f,
                        y = -random.nextFloat() * 500f,
                        speedY = 4f + random.nextFloat() * 8f,
                        speedX = -2f + random.nextFloat() * 4f,
                        size = 8f + random.nextFloat() * 12f,
                        color = colors[random.nextInt(colors.size)],
                        shapeIsCircle = random.nextBoolean()
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { _ ->
                for (i in particles.indices) {
                    val p = particles[i]
                    var newY = p.y + p.speedY
                    var newX = p.x + p.speedX
                    if (newY > 2000f) {
                        newY = -50f
                        newX = random.nextFloat() * 1000f
                    }
                    particles[i] = p.copy(y = newY, x = newX)
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        particles.forEach { p ->
            val drawX = (p.x / 1000f) * width
            val drawY = (p.y / 2000f) * height
            if (p.shapeIsCircle) {
                drawCircle(color = p.color, radius = p.size, center = Offset(drawX, drawY))
            } else {
                drawRect(color = p.color, topLeft = Offset(drawX, drawY), size = Size(p.size * 1.5f, p.size))
            }
        }
    }
}
