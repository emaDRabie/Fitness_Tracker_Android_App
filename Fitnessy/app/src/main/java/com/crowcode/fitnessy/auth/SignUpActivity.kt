package com.crowcode.fitnessy.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val emailEt: EditText = findViewById(R.id.email_et)
        val passEt: EditText = findViewById(R.id.pass_et)
        val conPassEt: EditText = findViewById(R.id.con_pass_et)
        val signUpBtn: Button = findViewById(R.id.sign_up_btn)
        progress = findViewById(R.id.progress)
        val alreadyUserTv: TextView = findViewById(R.id.already_user_tv)

        signUpBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val pass = passEt.text.toString()
            val conPassword = conPassEt.text.toString()

            if (email.isBlank() || pass.isBlank() || conPassword.isBlank()) {
                Toast.makeText(this, "Empty field/s", Toast.LENGTH_SHORT).show()
            } else if (pass.length < 6) {
                Toast.makeText(this, "Short password", Toast.LENGTH_SHORT).show()
            } else if (conPassword != pass) {
                Toast.makeText(this, "not matched", Toast.LENGTH_SHORT).show()
            } else {
                progress.isVisible = true
                // Sign up code
                addUser(email, pass)
            }
        }
        alreadyUserTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        val arrowBack = findViewById<ImageButton>(R.id.arrowBack)
        arrowBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun addUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyEmail()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    progress.isVisible = false
                }
            }
    }

    private fun verifyEmail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email!", Toast.LENGTH_SHORT).show()
                    progress.isVisible = false
                }
            }
    }
}