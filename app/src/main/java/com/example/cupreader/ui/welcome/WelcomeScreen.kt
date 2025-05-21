package com.example.cupreader.ui.welcome

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cupreader.LoginActivity
import com.example.cupreader.R
import com.example.cupreader.util.LocalStorage
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    // Define language options with display label and locale code
    val languageOptions = listOf(
        stringResource(R.string.language_english) to "en",
        stringResource(R.string.language_arabic) to "ar",
        stringResource(R.string.language_hebrew) to "he"
    )
    var selected by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Debug: observe stored language
    val storedLang by LocalStorage.getLanguage(context)
        .map { it.orEmpty() }
        .collectAsState(initial = "")

    Surface(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Prompt
            Text(
                text = stringResource(R.string.choose_language_prompt),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))

            // Radio list for languages
            languageOptions.forEach { (label, code) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selected = label to code }
                ) {
                    RadioButton(
                        selected = (selected?.first == label),
                        onClick = { selected = label to code }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = label)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Continue button
            Button(
                onClick = {
                    selected?.second?.let { langCode ->
                        scope.launch { LocalStorage.saveLanguage(context, langCode) }
                        activity?.run {
                            Intent(this, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }.also { startActivity(it) }
                            finish()
                        }
                    }
                },
                enabled = selected != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.continue_to_login))
            }

            Spacer(Modifier.height(16.dp))

            // Show stored language code for debugging
            Text(
                text = "▶ Stored lang: $storedLang",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
