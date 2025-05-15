package com.crowcode.fitnessy.user

import Trainee
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object UserSession {
    var userData: Trainee? = null

    fun load(context: Context) {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("name", null) ?: return
        val dobStr = prefs.getString("dob", null)
        val weight = prefs.getFloat("weight", 0f)
        val height = prefs.getFloat("height", 0f)
        val waterGoal = prefs.getFloat("waterGoal", 0f)
        val calGoal = prefs.getString("calGoal", "0") ?: "0"
        val weightGoal = prefs.getString("weightGoal", "0") ?: "0"
        val gender = prefs.getString("gender", "Unknown") ?: "Unknown"

        val dob = dobStr?.let {
            try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it)
            } catch (e: Exception) {
                null
            }
        } ?: Date()

        userData = Trainee(
            name = name,
            dob = dob,
            weight = weight,
            height = height,
            gender = gender,
            waterGoal = waterGoal,
            calGoal = calGoal,
            weightGoal = weightGoal
        )
    }

}
