package com.example.cupreader.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cupreader.R
import com.example.cupreader.navigation.Screen

@Composable
fun SplashScreen(navController: NavController) {
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }, 2500)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF321E1E), Color(0xFF6D3B3B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "CupReader",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your future starts with a sip",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
        }
    }
}
