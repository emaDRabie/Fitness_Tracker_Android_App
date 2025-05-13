package com.crowcode.fitnessy.user

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true)
@Composable
fun HealthDashboardPreview() {
    // Ensure that the preview is wrapped correctly inside a composable function
    HealthDashboardScreen()
}


@Composable
fun HealthDashboardScreen() {
    var calorieScore by remember { mutableIntStateOf(500) }
    var waterScore by remember { mutableIntStateOf(850) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(text = "Profile Name", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Pass calorieScore and waterScore to the MetricsSection
        NutritionScoreWithButtons(calorieScore, waterScore) { newCalories, newWater ->
            calorieScore = newCalories
            waterScore = newWater
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pass calorieScore and waterScore to update MetricItem in the MetricsSection
        MetricsSection(calorieScore, waterScore)

        Last30DaysGraph()

    }
}


@Composable
fun MetricsSection(calorieScore: Int, waterScore: Int) {
    Column {
        Text(text = "Metrics", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // Update the MetricItem values dynamically with calorieScore and waterScore
        MetricItem(label = "Calories", value = "$calorieScore cal")

        MetricItem(label = "Water", value = "$waterScore ml")
        MetricItem(label = "Weight", value = "58 kg")
    }
}


@Composable
fun MetricItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun NutritionScoreWithButtons(
    initialCalorie: Int,
    initialWater: Int,
    onScoreChange: (Int, Int) -> Unit
) {
    var calorieScore by remember { mutableIntStateOf(initialCalorie) }
    var waterScore by remember { mutableIntStateOf(initialWater) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CircularScoreDisplay(
                value = calorieScore,
                maxValue = 2000,
                label = "Calorie Score",
                unit = "kcal",
                color = Color(0xFFFF5722)
            )
            CircularScoreDisplay(
                value = waterScore,
                maxValue = 2000,
                label = "Water Score",
                unit = "ml",
                color = Color(0xFF2196F3)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoreControlButton(
                "Calorie",
                calorieScore,
                2000,
                Color(0xFFFF5722)
            ) { newCalorieScore ->
                calorieScore = newCalorieScore
                onScoreChange(calorieScore, waterScore)  // Notify the parent composable
            }
            ScoreControlButton("Water", waterScore, 2000, Color(0xFF2196F3)) { newWaterScore ->
                waterScore = newWaterScore
                onScoreChange(calorieScore, waterScore)  // Notify the parent composable
            }
        }
    }
}

@Composable
fun ScoreControlButton(
    label: String,
    currentValue: Int,
    maxValue: Int,
    color: Color,
    onValueChange: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var tempValue by remember { mutableStateOf(currentValue.toString()) }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text("$label Score")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Adjust $label Score") },
            text = {
                TextField(
                    value = tempValue,
                    onValueChange = { tempValue = it },
                    label = { Text("Value (0-$maxValue)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    tempValue.toIntOrNull()?.let { newValue ->
                        onValueChange(newValue.coerceIn(0, maxValue)) // Set new value
                    }
                    showDialog = false
                }) {
                    Text("Set")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}


@Composable
fun CircularScoreDisplay(
    value: Int,
    maxValue: Int,
    label: String,
    unit: String,
    color: Color,
    size: Dp = 120.dp
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.DarkGray)
        Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val thickness = size.toPx() / 10f
                val radius = (size.toPx() - thickness) / 2
                val center = Offset(size.toPx() / 2, size.toPx() / 2)
                drawCircle(
                    color = color.copy(alpha = 0.2f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = thickness)
                )
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * value / maxValue.toFloat(),
                    useCenter = false,
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = thickness, cap = StrokeCap.Round),
                    topLeft = center - Offset(radius, radius)
                )
            }
            Text("$value $unit", fontWeight = FontWeight.Bold, color = color)
        }
    }
}