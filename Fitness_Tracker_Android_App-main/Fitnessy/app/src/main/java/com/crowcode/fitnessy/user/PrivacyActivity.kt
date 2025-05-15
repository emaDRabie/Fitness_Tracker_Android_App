package com.crowcode.fitnessy.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.crowcode.fitnessy.R
import com.crowcode.fitnessy.auth.LoginActivity
import com.crowcode.fitnessy.databinding.ActivityPrivacy2Binding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class PrivacyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacy2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    // Modern permission request launcher
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startLocationUpdates()
        } else {
            binding.locationSwitch.isChecked = false
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPrivacy2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLocationUpdates()
        setupClickListeners()

    }

    private fun setupLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // Handle location updates if needed
            }
        }

        binding.locationSwitch.isChecked = hasLocationPermission()
    }

    private fun setupClickListeners() {
        binding.changePasswordSection.setOnClickListener { showChangePasswordDialog() }

        binding.locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkLocationPermission()
            } else {
                stopLocationUpdates()
            }
        }

        binding.deleteAccountSection.setOnClickListener { showDeleteAccountDialog() }
        binding.backButton.setOnClickListener {
            finish()
        }


    }

    // ==================== Change Password ====================
    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)

        MaterialAlertDialogBuilder(this)
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change") { dialog, _ ->
                val currentPassword = dialogView.findViewById<EditText>(R.id.currentPassword).text.toString()
                val newPassword = dialogView.findViewById<EditText>(R.id.newPassword).text.toString()
                val confirmPassword = dialogView.findViewById<EditText>(R.id.confirmPassword).text.toString()

                if (validatePasswordInputs(currentPassword, newPassword, confirmPassword)) {
                    changePassword(currentPassword, newPassword)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun validatePasswordInputs(current: String, new: String, confirm: String): Boolean {
        return when {
            current.isEmpty() -> {
                Toast.makeText(this, "Current password required", Toast.LENGTH_SHORT).show()
                false
            }
            new.isEmpty() -> {
                Toast.makeText(this, "New password required", Toast.LENGTH_SHORT).show()
                false
            }
            new.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                false
            }
            new != confirm -> {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun changePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser ?: return
        binding.progressBar.visibility = View.VISIBLE

        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            binding.progressBar.visibility = View.GONE
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Error changing password", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // ==================== Location Services ====================
    private fun checkLocationPermission() {
        when {
            hasLocationPermission() -> {
                startLocationUpdates()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showLocationPermissionRationale()
            }
            else -> {
                requestLocationPermission()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000
        ).apply {
            setMinUpdateIntervalMillis(5_000)
            setWaitForAccurateLocation(true)
        }.build()

        try {
            if (hasLocationPermission()) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                ).addOnSuccessListener {
                    Toast.makeText(this, "Location updates started", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    binding.locationSwitch.isChecked = false
                    Log.e("Location", "Failed to start updates: ${e.message}")
                }
            } else {
                binding.locationSwitch.isChecked = false
            }
        } catch (e: SecurityException) {
            binding.locationSwitch.isChecked = false
            Log.e("Location", "Security Exception: ${e.message}")
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Toast.makeText(this, "Location disabled", Toast.LENGTH_SHORT).show()
    }

    private fun showLocationPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Location Permission Needed")
            .setMessage("This app needs location permission to provide location-based services")
            .setPositiveButton("OK") { _, _ -> requestLocationPermission() }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                binding.locationSwitch.isChecked = false
            }
            .show()
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // ==================== Delete Account ====================
    private fun showDeleteAccountDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ -> requestPasswordForDeletion() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun requestPasswordForDeletion() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            hint = "Enter your password"
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Deletion")
            .setMessage("For security, please enter your password to confirm account deletion")
            .setView(input)
            .setPositiveButton("Confirm") { _, _ ->
                val password = input.text.toString()
                if (password.isNotEmpty()) {
                    deleteAccount(password)
                } else {
                    Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount(password: String) {
        val user = auth.currentUser ?: return
        binding.progressBar.visibility = View.VISIBLE

        val credential = EmailAuthProvider.getCredential(user.email!!, password)

        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.delete()
                        .addOnCompleteListener { deleteTask ->
                            binding.progressBar.visibility = View.GONE
                            if (deleteTask.isSuccessful) {
                                Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finishAffinity()
                            } else {
                                Toast.makeText(this, "Error deleting account", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}