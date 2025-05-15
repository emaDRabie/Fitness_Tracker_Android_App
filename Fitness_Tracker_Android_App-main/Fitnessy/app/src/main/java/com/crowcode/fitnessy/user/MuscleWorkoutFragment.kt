package com.crowcode.fitnessy.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crowcode.fitnessy.R


class MuscleWorkoutFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private lateinit var btnBack: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_muscle_workout, container, false)

        view.findViewById<TextView>(R.id.tv).text = requireArguments().getString("category")


        btnBack = view.findViewById(R.id.back_arrow)
        btnBack.setOnClickListener {
            // Use fragment transaction to pop the current fragment and return to the previous fragment
            //requireActivity().supportFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()

        }

        recyclerView = view.findViewById(R.id.recyclerMuscle)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val exercises = when (requireArguments().getString("category")) {
            "Chest" -> listOf(
                WorkoutMuscleItem("Flat bench press",R.drawable.ic_chest,"https://drive.google.com/uc?export=download&id=1lk6-0UrDu0G6XUuKET1Mw0_KHkTABBTr"),
                WorkoutMuscleItem("Incline bench press",R.drawable.ic_chest,"https://drive.google.com/uc?export=download&id=1yTZLH2wjTsaFTlCmiCTl5pn2q8dUUb6a"),
                WorkoutMuscleItem("Push Ups",R.drawable.ic_chest,"https://drive.google.com/uc?export=download&id=13StS3cX__rTeQMSlqV5Q90iHJjTbayqS"),
                WorkoutMuscleItem("Fly Cable crossover",R.drawable.ic_chest,"https://drive.google.com/uc?export=download&id=1yF8_mnQ4T3vS7b7MRfzXb6n1xfrsmQfh")
            )

            "Back" -> listOf(
                WorkoutMuscleItem("Lat Pull-down", R.drawable.ic_back,"https://drive.google.com/uc?export=download&id=1sxOc4vBCfuaoeAvJkof2yKq3xPn9M0-G"),
                WorkoutMuscleItem("Seated Cable Row", R.drawable.ic_back,"https://drive.google.com/uc?export=download&id=16WPsijykAMo8tv-7-g-I5ZcmTDJY0AdB"),
                WorkoutMuscleItem("Deadlift", R.drawable.ic_back,"https://drive.google.com/uc?export=download&id=1FNZHvUsbglWmB5emzMXAlcMoRji5s-Nu"),
                WorkoutMuscleItem("Single-Arm Dumbbell Row", R.drawable.ic_back,"https://drive.google.com/uc?export=download&id=1otyYrenzI9Wic-vgkNIn3wTI1qiKwz-T"),
                WorkoutMuscleItem("Pull up", R.drawable.ic_back,"https://drive.google.com/uc?export=download&id=1bMN8b2uo0IK-QK1RxMzlngAnAhE67U6q")
            )

            "Shoulders" -> listOf(
                WorkoutMuscleItem("Arnold Press", R.drawable.ic_shoulders,"https://drive.google.com/uc?export=download&id=1hVqfkaz4U7VKJd_PvBaN-e0ZgqSgk9xt"),
                WorkoutMuscleItem("Lateral Raises", R.drawable.ic_shoulders,"https://drive.google.com/uc?export=download&id=1Ac3APtTz8eIu9-bHEdaDBdb36xRyiDCO"),
                WorkoutMuscleItem("Front Raises", R.drawable.ic_shoulders,"https://drive.google.com/uc?export=download&id=1zhuT7CkGm9Yv3-bU6iGBxCxeXig7alD7"),
                WorkoutMuscleItem("Upright Rows", R.drawable.ic_shoulders,"https://drive.google.com/uc?export=download&id=1Nh2WqAV0qoa7dOYwbktVjsy4aTD-4OH3"),
                WorkoutMuscleItem("Cable Reverse Fly", R.drawable.ic_shoulders,"https://drive.google.com/uc?export=download&id=1nKitOvVn1nk9aefNxmMN4bswt70G3NOx")
            )

            "Arms" -> listOf(
                WorkoutMuscleItem("Barbell Biceps Curl",R.drawable.ic_arms,"https://drive.google.com/uc?export=download&id=1sEuS0atC38awsi6SwrggVBMz4XuqGG8e"),
                WorkoutMuscleItem("Hammer Curl",R.drawable.ic_arms,"https://drive.google.com/uc?export=download&id=1IrmdSXOUUhGyM6OvS1jin6rfU1DpnrcU"),
                WorkoutMuscleItem("Concentration Curl",R.drawable.ic_arms,"https://drive.google.com/uc?export=download&id=1_qljTort9_lJbeeWHI5FkmqCDnXTpF5r"),
                WorkoutMuscleItem("Rope Push-down",R.drawable.ic_arms,"https://drive.google.com/uc?export=download&id=1AAhr9ocXUNLWTkt7Bbqq7PWBlSfBQb3R"),
                WorkoutMuscleItem("Arms Overhead Triceps Extention",R.drawable.ic_arms,"https://drive.google.com/uc?export=download&id=15EotGpDw304fqU-hix7mC3vJrh0P9KH5"),
                WorkoutMuscleItem("Triceps Dips",R.drawable.ic_arms,"https://drive.google.com/uc?export=download&id=16iSMQ12P4-MNyBpakC5hGhgK1GOX3aTP")
            )

            "Legs" -> listOf(
                WorkoutMuscleItem("Leg press", R.drawable.ic_legs,"https://drive.google.com/uc?export=download&id=11JK8j6G9FN9jSSF_Y7pkVALm2HTOwLW0"),
                WorkoutMuscleItem("Leg Extension", R.drawable.ic_legs,"https://drive.google.com/uc?export=download&id=1_GV_ExTyN6t6C3zIPwkWp3oUUjg4-h2l"),
                WorkoutMuscleItem("Hip Thrusts", R.drawable.ic_legs,"https://drive.google.com/uc?export=download&id=1qliTBrq_9iPBusvZ9iTlGL-DM_b5oQzF"),
                WorkoutMuscleItem("RDLs", R.drawable.ic_legs,"https://drive.google.com/uc?export=download&id=1dKQIxV4zF1L1MSqwmhHsyK89F4gYlvZz"),
                WorkoutMuscleItem("Goblet Squats", R.drawable.ic_legs,"https://drive.google.com/uc?export=download&id=13yOFv00M2Qul8ekI6PWx_3JIfWijlPw1"),
                WorkoutMuscleItem("Calves", R.drawable.ic_legs,"https://drive.google.com/uc?export=download&id=1k8z_WcVmNbbi8JhcWBFS3QOAEwEt9f8W")
            )

            else -> emptyList()
        }

        adapter = WorkoutAdapter(exercises) { item ->
            val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl", item.videoUrl)
            startActivity(intent)
        }

        recyclerView.adapter = adapter


        return view

    }

    companion object {
        fun newInstance(category: String): MuscleWorkoutFragment {
            val fragment = MuscleWorkoutFragment()
            val args = Bundle()
            args.putString("category", category)
            fragment.arguments = args
            return fragment
        }
    }


}