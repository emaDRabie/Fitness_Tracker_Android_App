package com.crowcode.fitnessy.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.crowcode.fitnessy.MainActivity
import com.crowcode.fitnessy.R
import com.crowcode.fitnessy.user.DashboardActivity
import com.crowcode.fitnessy.user.UserSetupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val emailEt: EditText = findViewById(R.id.email_et)
        val passEt: EditText = findViewById(R.id.pass_et)
        val loginBtn: Button = findViewById(R.id.login_btn)
        progress = findViewById(R.id.progress)
        val notUserTv: TextView = findViewById(R.id.not_user_tv)
        val forgotPassTv: TextView = findViewById(R.id.forgot_pass_tv)
        val rememberMeCb: CheckBox = findViewById(R.id.remember_me_cb)

        // Load saved credentials if Remember Me was checked
        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
        emailEt.setText(prefs.getString("email", ""))
        passEt.setText(prefs.getString("pass", ""))

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val pass = passEt.text.toString().trim()

            // Save or clear credentials based on checkbox
            val editor = prefs.edit()
            if (rememberMeCb.isChecked) {
                editor.putString("email", email)
                editor.putString("pass", pass)
            } else {
                editor.putString("email", "")
                editor.putString("pass", "")
            }
            editor.apply()

            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Empty field/s", Toast.LENGTH_SHORT).show()
            } else {
                progress.isVisible = true
                login(email, pass)
            }
        }

        notUserTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        forgotPassTv.setOnClickListener {
            val email = emailEt.text.toString().trim()
            if (email.isNotBlank()) {
                progress.isVisible = true
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        progress.isVisible = false
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        val arrowBack = findViewById<ImageButton>(R.id.arrowBack)
        arrowBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Check if user completed setup
                        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        val isSetupComplete = prefs.getBoolean("setup_complete_${auth.currentUser?.uid}", false)

                        if (isSetupComplete) {
                            startActivity(Intent(this, DashboardActivity::class.java))
                        } else {
                            startActivity(Intent(this, UserSetupActivity::class.java))
                        }

                        finish() // Close login screen
                    } else {
                        Toast.makeText(this, "CHECK YOUR EMAIL!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
                progress.isVisible = false
            }
    }

    @Deprecated("This method has been deprecated in favor of using the OnBackPressedDispatcher.")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Close entire task
    }
}
