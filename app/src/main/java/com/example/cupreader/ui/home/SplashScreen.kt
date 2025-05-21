package com.example.cupreader.ui.home

import android.app.Activity
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.example.cupreader.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Hide status & navigation bars
    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 400)
    )
    // Animate gradient shift
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Trigger animations and navigation
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1800)
        navController.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        // Animated radial gradient background
        val colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = colors,
                        center = Offset(offsetAnim, offsetAnim / 2),
                        radius = offsetAnim + 300f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Logo with scale & fade
            Image(
                painter = painterResource(R.drawable.loading_splash),
                contentDescription = null,
                modifier = Modifier
                    .size(240.dp)
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = false
                    ),
                contentScale = ContentScale.Crop
            )
            // Loader with matching color
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp)
                    .scale(scaleAnim)
                    .alpha(alphaAnim),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}