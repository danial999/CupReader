package com.example.cupreader.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cupreader.LoginActivity
import com.example.cupreader.ui.home.CupReadingScreen
import com.example.cupreader.ui.userinfo.UserInfoScreen
import com.example.cupreader.ui.welcome.WelcomeScreen
import com.example.cupreader.ui.home.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = "splash"
) {
    // Map the incoming extra ("main" / "userInfo" / otherwise) into our graph routes
    val initial = when (startDestination) {
        "main"     -> "home"
        "userInfo" -> "userInfo"
        else       -> "splash"
    }

    NavHost(navController = navController, startDestination = initial) {
        // 1) Splash
        composable("splash") {
            SplashScreen(navController)
        }

        // 2) Welcome (language pick)
        composable("welcome") {
            WelcomeScreen()
        }

        // 3) Login (launch the Activity, then pop back to Compose)
        composable("login") {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                context.startActivity(Intent(context, LoginActivity::class.java))
                // Once the native Activity is launched, pop "login" so back won't return here
                navController.popBackStack()
            }
        }

        // 4) UserInfo (profile)
        composable("userInfo") {
            UserInfoScreen()
        }

        // 5) Home (cup-reading)
        composable("home") {
            CupReadingScreen()
        }
    }
}
