package com.example.cupreader.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cupreader.util.LocalStorage
import com.example.cupreader.viewmodel.CupReadingViewModel
import com.example.cupreader.UiState
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupReadingScreen(
    viewModel: CupReadingViewModel = viewModel()
) {
    val context = LocalContext.current
    // Safely map nullable flows to Strings
    val userName by LocalStorage.getUserName(context)
        .map { it.orEmpty() }
        .collectAsState(initial = "")
    val dob by LocalStorage.getDob(context)
        .map { it.orEmpty() }
        .collectAsState(initial = "")

    val uiState by viewModel.uiState.collectAsState()
    val promptText = "Provide a personalized fortune for $userName born on $dob."

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fortune Teller") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            LabelValueRow(label = "Name", value = userName)
            LabelValueRow(label = "Birthday", value = dob)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.sendPrompt(promptText) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Fortune")
            }
            Spacer(modifier = Modifier.height(24.dp))
            when (uiState) {
                is UiState.Initial -> { /* no-op */ }
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> Text((uiState as UiState.Success).outputText)
                is UiState.Error -> Text("Error: ${(uiState as UiState.Error).errorMessage}")
            }
        }
    }
}

@Composable
private fun LabelValueRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
