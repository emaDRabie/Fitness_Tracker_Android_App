package com.crowcode.grokcompose


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun HydrationScreen() {
    var waterIntake by remember { mutableIntStateOf(850) }
    val dailyGoal = 2000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "Hydration",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Today you took $waterIntake ml of water.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Status Indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val progress = (waterIntake * 100 / dailyGoal).coerceAtMost(100)
            StatusIndicator("Poor", progress < 40)
            StatusIndicator("Good", progress in 40..79)
            StatusIndicator("Perfect", progress >= 80)
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = { waterIntake.toFloat() / dailyGoal },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(vertical = 8.dp),
            color = Color(0xFF2196F3),
            trackColor = Color.LightGray
        )

        // Progress Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("200ml", "500ml", "700ml", "850ml", "2L").forEach { label ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Add Drink Button
        Button(
            onClick = { waterIntake += 250 },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(text = "Add Drink", color = Color.White)
        }
    }
}

@Composable
fun StatusIndicator(label: String, isActive: Boolean) {
    Text(
        text = label,
        fontSize = 14.sp,
        color = if (isActive) Color.White else Color.Black,
        modifier = Modifier
            .background(if (isActive) Color(0xFF2196F3) else Color.LightGray)
            .padding(8.dp)
    )
}