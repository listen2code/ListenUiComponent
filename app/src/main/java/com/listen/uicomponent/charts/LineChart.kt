package com.listen.uicomponent.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * Data model for a single point in the Line Chart.
 *
 * @param label Day of the month or label string (e.g. "1", "15", "24")
 * @param value Spending amount for this day
 * @param subLabel Optional secondary text (e.g., date formatted "08-01")
 */
data class LineChartPoint(val label: String, val value: Double, val subLabel: String = "")

/**
 * Modern minimalist Line Chart Composable for month daily spending trend.
 * Features touch scrubbing, vertical dashed guide line, floating tooltip, and smooth curve.
 */
@Composable
fun LineChart(
    points: List<LineChartPoint>,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    showFill: Boolean = true,
    currencySymbol: String = "",
    hideAmount: Boolean = false,
    maxLabel: String? = null,
    totalLabel: String? = null,
    selectedIndex: Int? = null,
    onSelectedIndexChange: ((Int?) -> Unit)? = null,
    onTooltipClick: ((LineChartPoint) -> Unit)? = null
) {
    if (points.isEmpty()) return

    val maxValue = remember(points) { points.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0 }
    val totalExpenseSum = remember(points) { points.sumOf { it.value } }
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)

    var internalSelectedIndex by remember { mutableStateOf<Int?>(null) }
    val currentSelectedIndex = if (onSelectedIndexChange != null) selectedIndex else internalSelectedIndex
    LaunchedEffect(points) { if (onSelectedIndexChange != null) onSelectedIndexChange(null) else internalSelectedIndex = null }

    val dataSignature = remember(points) { points.hashCode().toString() }
    var animatedSignature by rememberSaveable { mutableStateOf("") }
    val animProgress = remember { Animatable(if (animatedSignature == dataSignature && points.isNotEmpty()) 1f else 0f) }

    LaunchedEffect(dataSignature) {
        if (points.isEmpty()) animProgress.snapTo(0f)
        else if (animatedSignature != dataSignature) {
            animatedSignature = dataSignature
            animProgress.snapTo(0f)
            animProgress.animateTo(1f, tween(700, easing = FastOutSlowInEasing))
        }
    }
    val animationProgress = animProgress.value

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
                val totalStr = if (hideAmount) "••••" else "${currencySymbol}${"%.2f".format(totalExpenseSum)}"
                Text(totalLabel ?: totalStr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            val maxIdx = remember(points) { points.indices.maxByOrNull { points[it].value } }
            val maxStr = if (hideAmount) "Max: ••••" else (maxLabel ?: "Max: ${currencySymbol}${"%.0f".format(maxValue)}")
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(primaryColor.copy(alpha = 0.09f))
                    .then(
                        if (maxIdx != null && (points[maxIdx].value > 0 || points.size == 1)) {
                            Modifier.clickable {
                                val target = if (currentSelectedIndex == maxIdx) null else maxIdx
                                if (onSelectedIndexChange != null) onSelectedIndexChange(target) else internalSelectedIndex = target
                            }
                        } else Modifier
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(maxStr, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
            }
        }

        var activeCoordinates by remember { mutableStateOf<List<Offset>>(emptyList()) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(points) {
                        val step = (points.size - 1).coerceAtLeast(1)
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                val idx = (offset.x / (size.width / step)).roundToInt().coerceIn(0, points.size - 1)
                                if (onSelectedIndexChange != null) onSelectedIndexChange(idx) else internalSelectedIndex = idx
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                val idx = (change.position.x / (size.width / step)).roundToInt().coerceIn(0, points.size - 1)
                                if (onSelectedIndexChange != null) onSelectedIndexChange(idx) else internalSelectedIndex = idx
                            }
                        )
                    }
            ) {
                val width = size.width
                val height = size.height
                val bottomPadding = 4f
                val topPadding = 12f
                val availableHeight = height - topPadding - bottomPadding

                if (points.size == 1) {
                    val ratio = ((points[0].value / maxValue) * animationProgress).toFloat().coerceIn(0f, 1f)
                    drawCircle(primaryColor, 4.dp.toPx(), Offset(width / 2f, topPadding + availableHeight * (1f - ratio)))
                    return@Canvas
                }

                val stepX = width / (points.size - 1).coerceAtLeast(1)
                val coordinates = points.mapIndexed { index, point ->
                    val ratio = ((point.value / maxValue) * animationProgress).toFloat().coerceIn(0f, 1f)
                    val x = index * stepX
                    val y = topPadding + availableHeight * (1f - ratio)
                    Offset(x, y)
                }
                activeCoordinates = coordinates

                // Horizontal grid lines
                drawLine(gridColor, Offset(0f, topPadding), Offset(width, topPadding), strokeWidth = 1f)
                drawLine(gridColor, Offset(0f, height - bottomPadding), Offset(width, height - bottomPadding), strokeWidth = 1f)

                // Gradient fill below curve
                if (showFill) {
                    val fillPath = Path().apply {
                        moveTo(coordinates.first().x, coordinates.first().y)
                        for (i in 1 until coordinates.size) {
                            val cx = (coordinates[i - 1].x + coordinates[i].x) / 2f
                            cubicTo(cx, coordinates[i - 1].y, cx, coordinates[i].y, coordinates[i].x, coordinates[i].y)
                        }
                        lineTo(width, height)
                        lineTo(0f, height)
                        close()
                    }
                    drawPath(
                        fillPath,
                        Brush.verticalGradient(
                            listOf(primaryColor.copy(alpha = 0.28f), primaryColor.copy(alpha = 0.02f)),
                            startY = 0f, endY = height
                        )
                    )
                }

                // Smooth line curve
                val strokePath = Path().apply {
                    moveTo(coordinates.first().x, coordinates.first().y)
                    for (i in 1 until coordinates.size) {
                        val cx = (coordinates[i - 1].x + coordinates[i].x) / 2f
                        cubicTo(cx, coordinates[i - 1].y, cx, coordinates[i].y, coordinates[i].x, coordinates[i].y)
                    }
                }
                drawPath(strokePath, color = primaryColor, style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

                // Non-zero points dots
                coordinates.forEachIndexed { index, offset ->
                    if (points[index].value > 0) {
                        drawCircle(Color.White, 4.dp.toPx(), offset)
                        drawCircle(primaryColor, 2.5.dp.toPx(), offset)
                    }
                }

                // Vertical dashed dotted line and active point highlight
                if (currentSelectedIndex != null && currentSelectedIndex in coordinates.indices) {
                    val selCoord = coordinates[currentSelectedIndex]
                    drawLine(
                        primaryColor.copy(alpha = 0.6f), Offset(selCoord.x, topPadding), Offset(selCoord.x, height - bottomPadding),
                        strokeWidth = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                    )
                    drawCircle(primaryColor.copy(alpha = 0.22f), 9.dp.toPx(), selCoord)
                    drawCircle(Color.White, 5.5.dp.toPx(), selCoord)
                    drawCircle(primaryColor, 3.5.dp.toPx(), selCoord)
                }
            }

            // Floating Tooltip over active point
            if (currentSelectedIndex != null && currentSelectedIndex in points.indices && currentSelectedIndex in activeCoordinates.indices) {
                LineChartTooltip(
                    point = points[currentSelectedIndex], coord = activeCoordinates[currentSelectedIndex],
                    currencySymbol = currencySymbol, lineColor = primaryColor, hideAmount = hideAmount,
                    onDismissRequest = { if (onSelectedIndexChange != null) onSelectedIndexChange(null) else internalSelectedIndex = null },
                    onTooltipClick = onTooltipClick
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Bottom X-Axis Ticks Row
        LineChartXAxisLabels(points = points)
    }
}
