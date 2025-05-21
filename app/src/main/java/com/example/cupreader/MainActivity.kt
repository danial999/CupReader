package com.example.cupreader

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.cupreader.navigation.NavGraph
import com.example.cupreader.ui.theme.CupReaderTheme
import com.example.cupreader.util.LocaleUtil
import com.example.cupreader.util.LocalStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        // Retrieve saved language code ('en', 'he', 'ar'), default to English if missing
        val langCode = runBlocking {
            LocalStorage.getLanguage(newBase).first() ?: "en"
        }
        // Apply locale
        val context = LocaleUtil.setLocale(newBase, langCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CupReaderTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val startDest = intent.getStringExtra("startDestination") ?: "welcome"
                    NavGraph(navController, startDest)
                }
            }
        }
    }
}
