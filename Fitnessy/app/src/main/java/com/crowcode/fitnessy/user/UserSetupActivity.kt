package com.crowcode.fitnessy.user

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.crowcode.fitnessy.R
import com.crowcode.fitnessy.databinding.ActivityUserSetupBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UserSetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // date picker
        val calendar = Calendar.getInstance()
        binding.dobEt.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                binding.dobEt.setText(selectedDate)
            }, year, month, day)

            datePicker.show()
        }


        binding.confirmButton.setOnClickListener {
            binding.progress.isVisible = true

            val name = binding.displayNameEt.text.toString()
            val dobStr = binding.dobEt.text.toString()
            val weightStr = binding.weightEt.text.toString()
            val heightStr = binding.heightEt.text.toString()
            val waterStr = binding.waterEt.text.toString()
            val cal = binding.calEt.text.toString()
            val tWeight = binding.tWeightEt.text.toString()
            val selectedGenderId = binding.toggleGender.checkedRadioButtonId
            val selectedGender = findViewById<RadioButton>(selectedGenderId)?.text.toString()

            if (name.isBlank() || dobStr.isBlank() || weightStr.isBlank() ||
                heightStr.isBlank() || waterStr.isBlank() || cal.isBlank() || tWeight.isBlank()
            ) {
                Toast.makeText(this, "Empty Fields", Toast.LENGTH_SHORT).show()
                binding.progress.isVisible = false
                return@setOnClickListener
            }

            try {
                val dob = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dobStr)
                val trainee = Trainee(
                    name = name,
                    dob = dob ?: Date(),
                    weight = weightStr.toFloat(),
                    height = heightStr.toFloat(),
                    gender = selectedGender,
                    waterGoal = waterStr.toFloat(),
                    calGoal = cal,
                    weightGoal = tWeight,
                )

                val db = Firebase.firestore
                db.collection("users")
                    .add(trainee)
                    .addOnSuccessListener {
                        it.update("id", it.id)
                        Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                        binding.progress.isVisible = false
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        binding.progress.isVisible = false
                    }

            } catch (e: Exception) {
                Toast.makeText(this, "Invalid input: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progress.isVisible = false
            }
        }

    }
}
