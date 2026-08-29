package com.listen.uicomponent.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data model for a single point in the Line Chart.
 *
 * @param label Day of the month or label string (e.g. "1", "15", "24")
 * @param value Spending amount for this day
 * @param subLabel Optional secondary text (e.g., date formatted "08-01")
 */
data class LineChartPoint(
    val label: String,
    val value: Double,
    val subLabel: String = ""
)

/**
 * Modern minimalist Line Chart Composable for month daily spending trend.
 * Features gradient fill beneath the curve, smooth path rendering, and evenly distributed X-axis ticks.
 *
 * @param points List of daily LineChartPoint data
 * @param modifier Composable modifier (first optional parameter)
 * @param chartHeight Canvas height for the chart area
 * @param lineColor Stroke color of the trend line
 * @param showFill Whether to draw a gradient fill below the line
 */
@Composable
fun LineChart(
    points: List<LineChartPoint>,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    showFill: Boolean = true,
    currencySymbol: String = "",
    maxLabel: String? = null,
    totalLabel: String? = null
) {
    if (points.isEmpty()) return

    val maxValue = remember(points) { points.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0 }
    val totalExpenseSum = remember(points) { points.sumOf { it.value } }
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)

    val animationProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = 1f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 600,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "LineChartDraw"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Top Info Row: Total & Max
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(primaryColor, CircleShape)
                )
                Text(
                    text = totalLabel ?: "${currencySymbol}${"%.2f".format(totalExpenseSum)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = maxLabel ?: "Max: ${currencySymbol}${"%.0f".format(maxValue)}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Canvas Line Drawing Area
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            val width = size.width
            val height = size.height
            val bottomPadding = 4f
            val topPadding = 12f
            val availableHeight = height - topPadding - bottomPadding

            if (points.size == 1) {
                val ratio = ((points[0].value / maxValue) * animationProgress).toFloat().coerceIn(0f, 1f)
                val y = topPadding + availableHeight * (1f - ratio)
                drawCircle(
                    color = primaryColor,
                    radius = 4.dp.toPx(),
                    center = Offset(width / 2f, y)
                )
                return@Canvas
            }

            val stepX = width / (points.size - 1).coerceAtLeast(1)
            val coordinates = points.mapIndexed { index, point ->
                val ratio = ((point.value / maxValue) * animationProgress).toFloat().coerceIn(0f, 1f)
                val x = index * stepX
                val y = topPadding + availableHeight * (1f - ratio)
                Offset(x, y)
            }

            // Draw horizontal subtle grid lines
            drawLine(
                color = gridColor,
                start = Offset(0f, topPadding),
                end = Offset(width, topPadding),
                strokeWidth = 1f
            )
            drawLine(
                color = gridColor,
                start = Offset(0f, height - bottomPadding),
                end = Offset(width, height - bottomPadding),
                strokeWidth = 1f
            )

            // Draw Gradient Fill Path
            if (showFill) {
                val fillPath = Path().apply {
                    moveTo(coordinates.first().x, coordinates.first().y)
                    for (i in 1 until coordinates.size) {
                        val prev = coordinates[i - 1]
                        val curr = coordinates[i]
                        val cx = (prev.x + curr.x) / 2f
                        cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                    }
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.28f),
                            primaryColor.copy(alpha = 0.02f)
                        ),
                        startY = 0f,
                        endY = height
                    )
                )
            }

            // Draw Smooth Line Path
            val strokePath = Path().apply {
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 1 until coordinates.size) {
                    val prev = coordinates[i - 1]
                    val curr = coordinates[i]
                    val cx = (prev.x + curr.x) / 2f
                    cubicTo(cx, prev.y, cx, curr.y, curr.x, curr.y)
                }
            }

            drawPath(
                path = strokePath,
                color = primaryColor,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Draw Active Value Dots for non-zero points
            coordinates.forEachIndexed { index, offset ->
                if (points[index].value > 0) {
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = offset
                    )
                    drawCircle(
                        color = primaryColor,
                        radius = 2.5.dp.toPx(),
                        center = offset
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Bottom X-Axis Ticks Row
        LineChartXAxisLabels(points = points)
    }
}

/**
 * X-Axis date label generator displaying spaced date ticks without clutter.
 */
@Composable
private fun LineChartXAxisLabels(points: List<LineChartPoint>) {
    val total = points.size
    if (total == 0) return

    val labelIndices = remember(total) {
        if (total <= 7) {
            points.indices.toList()
        } else {
            val step = (total / 6).coerceAtLeast(1)
            val list = mutableListOf<Int>()
            var curr = 0
            while (curr < total) {
                list.add(curr)
                curr += step
            }
            if (!list.contains(total - 1)) {
                list.add(total - 1)
            }
            list
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        labelIndices.forEach { index ->
            val pt = points.getOrNull(index) ?: return@forEach
            Text(
                text = "${pt.label}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
