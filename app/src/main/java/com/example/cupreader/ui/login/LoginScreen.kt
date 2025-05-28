package com.example.cupreader.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cupreader.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    availableLocales: List<Locale> = listOf(Locale.ENGLISH, Locale("he")),
    onLocaleChange: (Locale) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localeExpanded by remember { mutableStateOf(false) }
    var selectedLocale by remember { mutableStateOf(availableLocales.first()) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Language dropdown at top-right
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                ExposedDropdownMenuBox(
                    expanded = localeExpanded,
                    onExpandedChange = { localeExpanded = !localeExpanded },
                    modifier = Modifier.wrapContentSize()
                ) {
                    TextField(
                        value = selectedLocale.displayLanguage,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = localeExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .clickable { localeExpanded = true },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = localeExpanded,
                        onDismissRequest = { localeExpanded = false }
                    ) {
                        availableLocales.forEach { locale ->
                            DropdownMenuItem(
                                text = { Text(locale.displayLanguage) },
                                onClick = {
                                    selectedLocale = locale
                                    localeExpanded = false
                                    onLocaleChange(locale)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.label_email)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.label_password)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: handle authentication
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                enabled = email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login_button))
            }
        }
    }
}
