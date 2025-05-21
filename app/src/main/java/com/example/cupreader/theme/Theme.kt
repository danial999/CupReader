package com.example.cupreader.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary   = androidx.compose.ui.graphics.Color(0xFF8D6E63),
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    secondary = androidx.compose.ui.graphics.Color(0xFFD7CCC8),
    background= androidx.compose.ui.graphics.Color(0xFFF5F5F5)
)

@Composable
fun CupReaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = androidx.compose.material3.Typography(),
        shapes      = androidx.compose.material3.Shapes(),
        content     = content
    )
}
