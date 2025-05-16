package com.crowcode.fitnessy.user

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.crowcode.fitnessy.R
import com.crowcode.fitnessy.databinding.ActivityEditBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var prefsManager: ProfilePrefsManager
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        prefsManager = ProfilePrefsManager(this)
        setupViews()
        loadProfileData()
    }

    private fun setupViews() {
        // Back button
        binding.backButton.setOnClickListener {
            finish()
        }

        // Date picker
        binding.etDob.setOnClickListener {
            showDatePicker()
        }

        // Save button
        binding.btnSave.setOnClickListener {
            saveProfileData()
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                binding.etDob.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadProfileData() {
        val profile = prefsManager.getProfile()
        binding.etName.setText(profile.name)
        binding.etEmail.setText(profile.email)
        binding.etDob.setText(profile.dob)
        binding.etCountry.setText(profile.country)
    }

    private fun saveProfileData() {
        val profile = ProfilePrefsManager.Profile(
            name = binding.etName.text.toString().trim(),
            email = binding.etEmail.text.toString().trim(),
            dob = binding.etDob.text.toString().trim(),
            country = binding.etCountry.text.toString().trim()
        )

        when {
            profile.name.isEmpty() -> binding.etName.error = "Name required"
            profile.email.isEmpty() -> binding.etEmail.error = "Email required"
            !isValidEmail(profile.email) -> binding.etEmail.error = "Invalid email"
            profile.dob.isEmpty() -> binding.etDob.error = "Date of birth required"
            profile.country.isEmpty() -> binding.etCountry.error = "Country required"
            else -> {
                prefsManager.saveProfile(profile)
                Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}