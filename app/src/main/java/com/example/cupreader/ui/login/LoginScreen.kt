package com.example.cupreader.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider



@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current as Activity
    val auth = remember { FirebaseAuth.getInstance() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("YOUR_WEB_CLIENT_ID")
        .requestEmail()
        .build()
    val googleClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { res ->
                if (res.isSuccessful) navController.navigate("userInfo") } }
        catch (e: ApiException) { /* handle error */ }
    }

    Surface(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { if (it.isSuccessful) navController.navigate("userInfo") }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { launcher.launch(googleClient.signInIntent) }, modifier = Modifier.fillMaxWidth()) {
                Text("Sign in with Google")
            }
        }
    }
}
