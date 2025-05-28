package com.example.cupreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.cupreader.navigation.NavGraph
import com.example.cupreader.ui.theme.CupReaderTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CupReaderTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
