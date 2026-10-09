package me.lauraleon.cybertura.ui.screens.screen1

import android.net.Uri
import android.view.ViewGroup
import android.widget.VideoView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import me.lauraleon.cybertura.ui.theme.CyberTuraTheme

@Composable
fun Screen1(
    onStartAdventure: () -> Unit
) {
    val context = LocalContext.current
    val videoUri = remember(context) {
        val resId = context.resources.getIdentifier("screen_1", "raw", context.packageName)
        if (resId != 0) {
            Uri.parse("android.resource://${context.packageName}/$resId")
        } else {
            null
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (videoUri != null) {
            AndroidView(
                factory = { ctx ->
                    object : VideoView(ctx) {
                        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                            val width = MeasureSpec.getSize(widthMeasureSpec)
                            val height = MeasureSpec.getSize(heightMeasureSpec)
                            setMeasuredDimension(width, height)
                        }
                    }.apply {
                        setVideoURI(videoUri)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setOnPreparedListener { mp ->
                            mp.isLooping = false
                            mp.start()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 60.dp, vertical = 80.dp)
                .fillMaxWidth()
                .height(72.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = Color(0xFF91FEFF),
                    spotColor = Color(0xFF91FEFF)
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFF91FEFF),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            Button(
                onClick = onStartAdventure,
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF490BF4)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "INICIAR AVENTURA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Screen1Preview() {
    CyberTuraTheme {
        Screen1(onStartAdventure = {})
    }
}

@Preview(showBackground = true, device = "spec:width=800dp,height=1280dp,dpi=240")
@Composable
fun Screen1TabletPreview() {
    CyberTuraTheme {
        Screen1(onStartAdventure = {})
    }
}
