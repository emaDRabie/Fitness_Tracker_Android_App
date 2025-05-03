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
import com.crowcode.fitnessy.Dashboard.DashboardActivity
import com.crowcode.fitnessy.Dashboard.UserSetupActivity
import com.crowcode.fitnessy.MainActivity
import com.crowcode.fitnessy.R
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

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val pass = passEt.text.toString()
            val editor = getSharedPreferences("user_data", MODE_PRIVATE).edit()
            if (rememberMeCb.isChecked) {
                //save data
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
                // login code
                login(email, pass)

            }
        }
        notUserTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        forgotPassTv.setOnClickListener {
            progress.isVisible = true
            val email = emailEt.text.toString()
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
        val e = prefs.getString("email", null)
        val p = prefs.getString("pass", null)
        emailEt.setText(e)
        passEt.setText(p)

        val arrowBack = findViewById<ImageButton>(R.id.arrowBack)
        arrowBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // This closes all activities in the task
    }

    private fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to categories activity
                    if (auth.currentUser!!.isEmailVerified) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        // Start CategoryActivity after successful login
                        startActivity(Intent(this, UserSetupActivity::class.java))
                        finish() // Close the login screen
                    } else
                        Toast.makeText(this, "CHECK YOUR EMAIL!", Toast.LENGTH_SHORT).show()

                } else
                // If sign in fails, display a message to the user.
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                progress.isVisible = false

            }
    }
}