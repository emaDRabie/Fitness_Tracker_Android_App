package com.crowcode.grokcompose


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        // Health Score
        Text(
            text = "Health Score",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "150",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2196F3))
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Metrics Section
        Text(
            text = "Metrics",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricCard("Calories", "500 Cal")
            MetricCard("Weight", "58 kg")
            MetricCard("Water", "850 ml")
        }

        // Weight Loss Section
        Text(
            text = "Weight Loss",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "-4%\nLast 30 days +2%",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Custom Weight Loss Graph
        WeightLossGraph(
            dataPoints = listOf(60f, 59f, 58.5f, 58f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Progress Text
        Text(
            text = "You're 80% of the way to your goal (60%)",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun MetricCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 14.sp)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WeightLossGraph(dataPoints: List<Float>, modifier: Modifier = Modifier) {
    val maxWeight = dataPoints.maxOrNull() ?: 60f
    val minWeight = dataPoints.minOrNull() ?: 58f
    val weightRange = maxWeight - minWeight

    Canvas(modifier = modifier.background(Color.LightGray.copy(alpha = 0.1f))) {
        val width = size.width
        val height = size.height
        val padding = 16.dp.toPx()

        // Calculate points for the graph
        val points = dataPoints.mapIndexed { index, weight ->
            val x = padding + (index * (width - 2 * padding) / (dataPoints.size - 1))
            val y = padding + ((maxWeight - weight) / weightRange) * (height - 2 * padding)
            Offset(x, y)
        }

        // Draw the line
        val path = Path().apply {
            points.forEachIndexed { index, point ->
                if (index == 0) moveTo(point.x, point.y)
                else lineTo(point.x, point.y)
            }
        }

        // Draw the graph line
        drawPath(
            path = path,
            color = Color(0xFF2196F3),
            style = Stroke(width = 4.dp.toPx())
        )

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color(0xFF2196F3),
                radius = 6.dp.toPx(),
                center = point
            )
        }

        // Draw X-axis labels (dates)
        val dates = listOf("10/1", "11/1", "12/1", "1/1")
        dates.forEachIndexed { index, date ->
            val x = padding + (index * (width - 2 * padding) / (dates.size - 1))
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x,
                height - 4.dp.toPx(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}