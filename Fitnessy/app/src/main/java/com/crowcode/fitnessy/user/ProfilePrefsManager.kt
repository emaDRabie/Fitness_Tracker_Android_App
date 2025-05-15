package com.crowcode.fitnessy.user

import android.content.Context
import android.content.SharedPreferences

class ProfilePrefsManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "ProfilePrefs"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_DOB = "dob"
        private const val KEY_COUNTRY = "country"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveProfile(profile: Profile) {
        sharedPref.edit().apply {
            putString(KEY_NAME, profile.name)
            putString(KEY_EMAIL, profile.email)
            putString(KEY_DOB, profile.dob)
            putString(KEY_COUNTRY, profile.country)
            apply()
        }
    }

    fun getProfile(): Profile {
        return Profile(
            name = sharedPref.getString(KEY_NAME, "") ?: "",
            email = sharedPref.getString(KEY_EMAIL, "") ?: "",
            dob = sharedPref.getString(KEY_DOB, "") ?: "",
            country = sharedPref.getString(KEY_COUNTRY, "") ?: ""
        )
    }

    fun clearProfile() {
        sharedPref.edit().clear().apply()
    }

    data class Profile(
        val name: String,
        val email: String,
        val dob: String,
        val country: String
    )
}