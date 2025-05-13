package com.crowcode.fitnessy.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crowcode.fitnessy.R

class FitnessBodyFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private lateinit var btnCore: Button
    private lateinit var btnMuscles: Button

    private val coreWorkouts = listOf(
        WorkoutMuscleItem("Plank", R.drawable.ic_plank,"https://drive.google.com/uc?export=download&id=1lbPeo-Cezla53GrKTcfJcpew1HhLmDCW"),
        WorkoutMuscleItem("Sit-ups", R.drawable.ic_sit_ups,"https://drive.google.com/uc?export=download&id=1EZdRA-7UFlsuAUl9rwSQDi41a8zqdugH"),
        WorkoutMuscleItem("Crunches", R.drawable.ic_crunches,"https://drive.google.com/uc?export=download&id=16pd3luqR2jnhiBCKpQQz7tIjSf30qNRM"),
        WorkoutMuscleItem("Bicycle Crunches", R.drawable.ic_core, "https://drive.google.com/uc?export=download&id=1iPFp3qPehUkd0N5zo6K5SZ6wbUQGLr0M"),
        WorkoutMuscleItem("Leg Raises", R.drawable.ic_leg_raises,"https://drive.google.com/uc?export=download&id=1kBBd5vSXnevniBJ46iVq4U6MjJfGMsfe"),
        WorkoutMuscleItem("Russian Twists", R.drawable.ic_russian_twists,"https://drive.google.com/uc?export=download&id=1-dE--Eq39-Q5VtPDgtA1d67Z7qGUY6Dh"),
        WorkoutMuscleItem("Mountain Climbers", R.drawable.ic_core,"https://drive.google.com/uc?export=download&id=1gAmuR4ex0Bs3_dl3KwiXSbk16Wc1QBwZ"),
        WorkoutMuscleItem("Ab Wheel Roullouts", R.drawable.ic_core,"https://drive.google.com/uc?export=download&id=1YpX_I7ygk1OrmuBIIPMp2uML1X8opTNy")
    )

    private val muscleWorkouts = listOf(
        WorkoutMuscleItem("Chest", R.drawable.ic_chest),
        WorkoutMuscleItem("Back", R.drawable.ic_back),
        WorkoutMuscleItem("Shoulders", R.drawable.ic_shoulders),
        WorkoutMuscleItem("Arms", R.drawable.ic_arms),
        WorkoutMuscleItem("Legs", R.drawable.ic_legs)
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fitness_body, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        btnCore = view.findViewById(R.id.btnCore)
        btnMuscles = view.findViewById(R.id.btnMuscles)

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = WorkoutAdapter(coreWorkouts) { item ->
            if (item.videoUrl.isNotEmpty()) {
                // Play video
                val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
                intent.putExtra("videoUrl", item.videoUrl)
                startActivity(intent)
            } else {
                // It's a category, go deeper
                val fragment = MuscleWorkoutFragment.newInstance(item.title)
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            if (btnMuscles.currentTextColor == Color.WHITE) {
                val fragment = MuscleWorkoutFragment.newInstance(item.title)
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }


        recyclerView.adapter = adapter

//        // Initially hide the buttons
//        btnCore.visibility = View.GONE
//        btnMuscles.visibility = View.GONE
//        recyclerView.visibility = View.GONE
//
//        // Show buttons when the fragment is visible
//        btnCore.visibility = View.VISIBLE
//        btnMuscles.visibility = View.VISIBLE

        recyclerView.visibility = View.GONE


        btnCore.setOnClickListener {
            adapter.updateList(coreWorkouts)
            recyclerView.visibility = View.VISIBLE
            updateButtonStyles(isCoreSelected = true)
        }

        btnMuscles.setOnClickListener {
            adapter.updateList(muscleWorkouts)
            recyclerView.visibility = View.VISIBLE
            updateButtonStyles(isCoreSelected = false)
        }

        resetButtonStyles()
        //updateButtonStyles(isCoreSelected = false)
        return view
    }

    private fun updateButtonStyles(isCoreSelected: Boolean) {
        if (isCoreSelected) {
            btnCore.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            btnCore.setTextColor(Color.WHITE)
            btnMuscles.setBackgroundColor(Color.TRANSPARENT)
            btnMuscles.setTextColor(Color.BLACK)
        } else {
            btnMuscles.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            btnMuscles.setTextColor(Color.WHITE)
            btnCore.setBackgroundColor(Color.TRANSPARENT)
            btnCore.setTextColor(Color.BLACK)
        }
    }

    private fun resetButtonStyles() {
        btnCore.setBackgroundColor(Color.TRANSPARENT)
        btnCore.setTextColor(Color.BLACK)
        btnMuscles.setBackgroundColor(Color.TRANSPARENT)
        btnMuscles.setTextColor(Color.BLACK)
    }
}