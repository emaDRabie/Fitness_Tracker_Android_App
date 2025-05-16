package com.crowcode.fitnessy.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.crowcode.fitnessy.R
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import com.crowcode.fitnessy.auth.LoginActivity
import com.crowcode.fitnessy.databinding.FragmentProfileBinding
import androidx.core.content.edit

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var notificationsSwitch: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawerLayout = binding.drawerLayout
        notificationsSwitch = drawerLayout.findViewById(R.id.notifications_switch)
        binding.settingsIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.root.findViewById<View>(R.id.menu_logout)?.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        setupDrawerClickListeners()
        setupNotificationsSwitch()
    }

    private fun setupNotificationsSwitch() {
        // Initialize switch state (you might want to load this from SharedPreferences)
        val prefs = requireContext().getSharedPreferences("AppPrefs", 0)
        val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
        notificationsSwitch.isChecked = notificationsEnabled

        // Handle switch changes
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save the preference
            prefs.edit() { putBoolean("notifications_enabled", isChecked) }


            if (isChecked) {
                // Enable notifications
                Toast.makeText(requireContext(), "Notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                // Disable notifications
                Toast.makeText(requireContext(), "Notifications disabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Perform logout and navigate to login activity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setupDrawerClickListeners() {
        // Edit Profile
        drawerLayout.findViewById<View>(R.id.menu_edit_profile)?.setOnClickListener {
            startActivity(Intent(requireContext(), EditActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Privacy & Security
        drawerLayout.findViewById<View>(R.id.menu_privacy)?.setOnClickListener {
            startActivity(Intent(requireContext(), PrivacyActivity::class.java))
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        drawerLayout.findViewById<View>(R.id.menu_notifications)?.setOnClickListener {
            notificationsSwitch.toggle()
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
