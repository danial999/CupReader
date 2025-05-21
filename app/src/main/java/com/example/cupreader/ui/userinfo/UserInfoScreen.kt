package com.example.cupreader.ui.userinfo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cupreader.MainActivity
import com.example.cupreader.R
import com.example.cupreader.util.LocalStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("StringFormatInvalid")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore
    val uid = auth.currentUser?.uid

    // Snackbar host for error/success messages
    val snackbarHostState = remember { SnackbarHostState() }

    // Check authentication
    LaunchedEffect(uid) {
        if (uid == null) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.error_not_authenticated),
                duration = SnackbarDuration.Long
            )
        }
    }

    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    // DatePickerDialog
    val calendar = remember { Calendar.getInstance() }
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            dob = "%04d-%02d-%02d".format(year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Load from Firestore on first composition
    LaunchedEffect(uid) {
        uid?.let { id ->
            firestore.collection("UserInfo")
                .document(id)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        doc.getString("name")?.let { name = it }
                        doc.getString("dob")?.let { dob = it }
                        doc.getString("gender")?.let { gender = it }
                        // Cache locally
                        scope.launch {
                            LocalStorage.saveUserName(context, name)
                            LocalStorage.saveDob(context, dob)
                            LocalStorage.saveGender(context, gender)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_fetching_user_info, e.message ?: ""),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.profile_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = dob,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.label_dob)) },
                    trailingIcon = {
                        IconButton(onClick = { datePicker.show() }) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = stringResource(R.string.label_dob)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePicker.show() }
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text(stringResource(R.string.label_gender)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))

                val isFormValid = name.isNotBlank() && dob.isNotBlank() && gender.isNotBlank()
                Button(
                    onClick = {
                        if (uid == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.error_not_authenticated),
                                    duration = SnackbarDuration.Long
                                )
                            }
                            return@Button
                        }
                        // Save locally
                        scope.launch {
                            LocalStorage.saveUserName(context, name)
                            LocalStorage.saveDob(context, dob)
                            LocalStorage.saveGender(context, gender)
                        }
                        // Save to Firestore
                        val userInfo = mapOf(
                            "name" to name,
                            "dob" to dob,
                            "gender" to gender
                        )
                        firestore.collection("UserInfo")
                            .document(uid)
                            .set(userInfo)
                            .addOnSuccessListener {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.success_saving_user_info),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                            .addOnFailureListener { e ->
                                scope.launch {
                                    val errorMsg = if (e.message?.contains("PERMISSION_DENIED") == true)
                                        context.getString(R.string.error_permission_denied)
                                    else
                                        context.getString(R.string.error_saving_user_info, e.message ?: "")
                                    snackbarHostState.showSnackbar(
                                        message = errorMsg,
                                        duration = SnackbarDuration.Long
                                    )
                                }
                            }
                        // Navigate to main
                        activity?.apply {
                            Intent(this, MainActivity::class.java).apply {
                                putExtra("startDestination", "main")
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }.also(::startActivity)
                            finish()
                        }
                    },
                    enabled = isFormValid && uid != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.continue_to_login))
                }
            }
        }
    }
}
