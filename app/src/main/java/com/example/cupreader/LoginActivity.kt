
package com.example.cupreader

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var db: FirebaseFirestore

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var googleButton: Button
    private lateinit var progressBar: ProgressBar

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleGoogleSignInResult(task)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailInput = findViewById(R.id.email_edit_text)
        passwordInput = findViewById(R.id.password_edit_text)
        emailLayout = findViewById(R.id.email_input_layout)
        passwordLayout = findViewById(R.id.password_input_layout)
        loginButton = findViewById(R.id.login_button)
        googleButton = findViewById(R.id.google_sign_in_button)
        progressBar = findViewById(R.id.progress_bar)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginButton.setOnClickListener { signInWithEmail() }
        googleButton.setOnClickListener { launchGoogleSignIn() }

        auth.currentUser?.uid?.let {
            FirebaseCrashlytics.getInstance().setUserId(it)
        }
    }

    private fun signInWithEmail() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        emailLayout.error = null
        passwordLayout.error = null

        if (email.isEmpty()) {
            emailLayout.error = "Please enter email"
            return
        }
        if (password.isEmpty()) {
            passwordLayout.error = "Please enter password"
            return
        }

        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    goToNextStep()
                } else {
                    FirebaseCrashlytics.getInstance().recordException(task.exception ?: Exception("Unknown sign-in error"))
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun launchGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleGoogleSignInResult(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            progressBar.visibility = View.VISIBLE

            auth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    progressBar.visibility = View.GONE
                    if (authTask.isSuccessful) {
                        goToNextStep()
                    } else {
                        FirebaseCrashlytics.getInstance().recordException(authTask.exception ?: Exception("Google sign-in failed"))
                        Toast.makeText(this, "Google Sign-In failed: ${authTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        } catch (e: ApiException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Toast.makeText(this, "Google Sign-In error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToNextStep() {
        val uid = auth.currentUser?.uid ?: return

        val intent = Intent(this, MainActivity::class.java)
        db.collection("UserInfo").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    intent.putExtra("startDestination", "main")
                } else {
                    intent.putExtra("startDestination", "userinfo")
                }
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                intent.putExtra("startDestination", "userinfo")
                startActivity(intent)
                finish()
            }
    }
}
