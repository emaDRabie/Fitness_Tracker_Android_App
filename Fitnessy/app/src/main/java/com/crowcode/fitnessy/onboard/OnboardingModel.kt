package com.crowcode.fitnessy.onboard

import androidx.annotation.DrawableRes
import com.crowcode.fitnessy.R

sealed class OnboardingModel(
    @DrawableRes val image: Int,
    val title: String,
    val description: String,
) {

    data object FirstPage : OnboardingModel(
        image = R.drawable.img_1,
        title = "Your Journey Starts Here",
        description = "Define your goals \n" +
                "Customize your fitness plan your way."
    )

    data object SecondPage : OnboardingModel(
        image = R.drawable.img_2,
        title = "Stay on Track",
        description = "Log your workouts\n" +
                "see your progress every step of the way."
    )

    data object ThirdPages : OnboardingModel(
        image = R.drawable.img_3,
        title = "Smash Your Workouts",
        description = "Access guided exercises\n" +
                "Train anytime, anywhere, and level up your fitness."
    )


}