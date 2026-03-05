package com.genpause.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genpause.app.ui.theme.*

// ── Time Saved Hero Card ──

@Composable
fun TimeSavedHeroCard(
    timeSavedMinutes: Int,
    modifier: Modifier = Modifier
) {
    val hours = timeSavedMinutes / 60
    val mins = timeSavedMinutes % 60
    val display = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ZenPrimary.copy(alpha = 0.15f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "⏱️",
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = display,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = ZenPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Time Saved",
                fontSize = 16.sp,
                color = ZenTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Attempt Frequency Bar Chart ──

@Composable
fun AttemptBarChart(
    hourlyCounts: List<Pair<Int, Int>>, // (hour, count)
    modifier: Modifier = Modifier
) {
    val maxCount = hourlyCounts.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1

    Column(modifier = modifier) {
        Text(
            text = "Attempt Frequency (24h)",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ZenTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ZenElevatedSurface)
                .padding(12.dp)
        ) {
            val barWidth = size.width / 24f * 0.7f
            val gap = size.width / 24f * 0.3f

            for (h in 0..23) {
                val count = hourlyCounts.find { it.first == h }?.second ?: 0
                val barHeight = if (maxCount > 0) (count.toFloat() / maxCount) * size.height * 0.85f else 0f
                val x = h * (barWidth + gap) + gap / 2

                // Bar
                drawRoundRect(
                    color = ZenPrimary.copy(alpha = 0.7f),
                    topLeft = Offset(x, size.height - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                )
            }
        }

        // Hour labels
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("12am", "6am", "12pm", "6pm", "11pm").forEach { l ->
                Text(text = l, fontSize = 10.sp, color = ZenTextSecondary)
            }
        }
    }
}

// ── Success Rate Trend Line ──

@Composable
fun SuccessRateTrend(
    weeklyRates: List<Float>, // 0..1 success rate per week, most recent last
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Success Rate Trend",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ZenTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (weeklyRates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZenElevatedSurface),
                contentAlignment = Alignment.Center
            ) {
                Text("Not enough data yet", color = ZenTextSecondary, fontSize = 13.sp)
            }
        } else {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZenElevatedSurface)
                    .padding(12.dp)
            ) {
                val path = Path()
                val stepX = if (weeklyRates.size > 1) size.width / (weeklyRates.size - 1) else size.width
                weeklyRates.forEachIndexed { i, rate ->
                    val x = i * stepX
                    val y = size.height - (rate * size.height * 0.9f)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(
                    path = path,
                    color = ZenSecondary,
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )

                // Draw dots
                weeklyRates.forEachIndexed { i, rate ->
                    val x = i * stepX
                    val y = size.height - (rate * size.height * 0.9f)
                    drawCircle(color = ZenSecondary, radius = 5f, center = Offset(x, y))
                }
            }
        }
    }
}

// ── App Distribution Donut Chart ──

@Composable
fun AppDistributionDonut(
    appCounts: List<Pair<String, Int>>, // (app label, count)
    modifier: Modifier = Modifier
) {
    val total = appCounts.sumOf { it.second }.coerceAtLeast(1)
    val colors = listOf(ZenPrimary, ZenSecondary, ZenAccent, Color(0xFF4CAF50), Color(0xFFFF9800))

    Column(modifier = modifier) {
        Text(
            text = "App Distribution",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ZenTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ZenElevatedSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Donut
            Canvas(
                modifier = Modifier.size(100.dp)
            ) {
                var startAngle = -90f
                appCounts.forEachIndexed { i, (_, count) ->
                    val sweep = (count.toFloat() / total) * 360f
                    drawArc(
                        color = colors[i % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 20f, cap = StrokeCap.Round),
                        topLeft = Offset(10f, 10f),
                        size = Size(size.width - 20f, size.height - 20f)
                    )
                    startAngle += sweep
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Legend
            Column {
                appCounts.take(5).forEachIndexed { i, (label, count) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(colors[i % colors.size], RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$label ($count)",
                            fontSize = 12.sp,
                            color = ZenTextSecondary
                        )
                    }
                }
            }
        }
    }
}
