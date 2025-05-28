package com.example.cupreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cupreader.ui.login.LoginScreen
import com.example.cupreader.ui.splash.SplashScreen
import com.example.cupreader.ui.userinfo.UserInfoScreen
import com.example.cupreader.ui.home.CupReadingScreen
import java.util.Locale

sealed class Screen(val route: String) {
    object Splash     : Screen("splash")
    object Login      : Screen("login")
    object UserInfo   : Screen("userInfo")
    object Main       : Screen("cupReading") // 💡 זה המסך הראשי שלך בפועל
}

@Composable
fun NavGraph(navController: NavHostController) {
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
                    Locale("he"),
                    Locale("ar") // ✅ ערבית נוספה
                ),
                onLocaleChange = { locale ->
                    // TODO: Apply locale change logic if needed
                }
            )
        }
        composable(Screen.UserInfo.route) {
            UserInfoScreen(navController)
        }
        composable(Screen.Main.route) {
            CupReadingScreen()
        }
    }
}
