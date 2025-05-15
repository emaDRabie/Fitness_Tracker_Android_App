package com.crowcode.fitnessy.user

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.crowcode.fitnessy.R
import com.crowcode.fitnessy.databinding.ActivityEditBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var prefsManager: ProfilePrefsManager
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedImageUri: Uri? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showImagePickerOptions()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.profileImage.setImageURI(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            binding.profileImage.setImageBitmap(it)
            // Note: For permanent storage, you'd need to save the bitmap to a file
        }
    }
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
        binding.profileImage.setOnClickListener {
            checkPermissionAndPickImage()
        }
        binding.root.findViewById<ImageView>(R.id.camera_icon).setOnClickListener {
            checkPermissionAndPickImage()
        }
    }

    private fun checkPermissionAndPickImage() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                showImagePickerOptions()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                Toast.makeText(
                    this,
                    "Storage permission is required to select profile picture",
                    Toast.LENGTH_SHORT
                ).show()
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> cameraLauncher.launch(null)
                1 -> galleryLauncher.launch("image/*")
                2 -> dialog.dismiss()
            }
        }
        builder.show()
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
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val profile = prefsManager.getProfile()

        binding.etName.setText(profile.name)
        binding.etDob.setText(profile.dob)
        binding.etCountry.setText(profile.country)
        binding.etEmail.setText(currentUser.email) // Load FirebaseAuth email
        binding.etEmail.isEnabled = false // Make email not editable
    }

    private fun saveProfileData() {
        val name = binding.etName.text.toString().trim()
        val dob = binding.etDob.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()

        when {
            name.isEmpty() -> binding.etName.error = "Name required"
            dob.isEmpty() -> binding.etDob.error = "Date of birth required"
            country.isEmpty() -> binding.etCountry.error = "Country required"
            else -> {
                val profile = ProfilePrefsManager.Profile(
                    name = name,
                    email = Firebase.auth.currentUser?.email ?: "", // use only for saving
                    dob = dob,
                    country = country
                )
                prefsManager.saveProfile(profile)
                Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }



}