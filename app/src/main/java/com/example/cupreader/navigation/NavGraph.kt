package com.example.cupreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cupreader.ui.login.LoginScreen
import com.example.cupreader.ui.home.SplashScreen
import com.example.cupreader.ui.userinfo.UserInfoScreen
import com.example.cupreader.ui.home.CupReadingScreen
import java.util.Locale

sealed class Screen(val route: String) {
    object Splash     : Screen("splash")
    object Login      : Screen("login")
    object UserInfo   : Screen("userInfo")
    object CupReading : Screen("cupReading")
}

@Composable
fun NavGraph(navController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                availableLocales = listOf(
                    Locale.ENGLISH,
                    Locale("he")
                ),
                onLocaleChange = { locale ->
                    // Apply locale change logic here
                }
            )
        }
        composable(Screen.UserInfo.route) {
            UserInfoScreen()
        }
        composable(Screen.CupReading.route) {
            CupReadingScreen()
        }
    }
}
