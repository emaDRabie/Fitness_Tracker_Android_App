package com.crowcode.fitnessy.user

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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

@Composable
fun Last30DaysGraph() {
    // Sample data - replace with your actual data
    val dataPoints = listOf(
        50f, 70f, 85f, 60f, 45f, 80f, 90f, 75f, 65f, 55f,
        40f, 60f, 70f, 85f, 90f, 80f, 75f, 65f, 70f, 80f,
        90f, 85f, 75f, 65f, 60f, 70f, 80f, 85f, 90f, 95f
    )

    val xAxisLabels = listOf("10/1", "11/1", "12/1", "1/1")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Last 30 days",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val graphHeight = size.height - 50
                val graphWidth = size.width - 40

                // Calculate Y scale
                val maxValue = dataPoints.maxOrNull() ?: 100f
                val yScale = graphHeight / maxValue

                // Draw Y axis
                drawLine(
                    start = Offset(30f, 0f),
                    end = Offset(30f, graphHeight),
                    color = Color.Gray,
                    strokeWidth = 2f
                )

                // Draw X axis
                drawLine(
                    start = Offset(30f, graphHeight),
                    end = Offset(size.width - 10, graphHeight),
                    color = Color.Gray,
                    strokeWidth = 2f
                )

                // Draw grid lines and Y axis labels
                val yStep = maxValue / 5
                for (i in 0..5) {
                    val yPos = graphHeight - (i * yStep * yScale)
                    drawLine(
                        start = Offset(30f, yPos),
                        end = Offset(size.width - 10, yPos),
                        color = Color.LightGray,
                        strokeWidth = 1f
                    )

                    // Draw Y axis label
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${(i * yStep).toInt()}",
                            10f,
                            yPos + 15,
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = android.graphics.Color.GRAY
                            }
                        )
                    }
                }

                // Calculate X positions for data points
                val xStep = graphWidth / (dataPoints.size - 1)
                val points = dataPoints.mapIndexed { index, value ->
                    Offset(30f + index * xStep, graphHeight - (value * yScale))
                }

                // Draw line graph
                val path = Path().apply {
                    points.forEachIndexed { index, point ->
                        if (index == 0) {
                            moveTo(point.x, point.y)
                        } else {
                            lineTo(point.x, point.y)
                        }
                    }
                }

                drawPath(
                    path = path,
                    color = Color.Blue,
                    style = Stroke(width = 3f)
                )

                // Draw X axis labels
                val labelStep = dataPoints.size / (xAxisLabels.size - 1)
                xAxisLabels.forEachIndexed { index, label ->
                    val xPos = 30f + index * labelStep * xStep
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            label,
                            xPos - 20,
                            graphHeight + 30,
                            android.graphics.Paint().apply {
                                textSize = 24f
                                color = android.graphics.Color.GRAY
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLast30DaysGraph() {
    Last30DaysGraph()
}