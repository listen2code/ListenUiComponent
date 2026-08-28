package com.listen.uicomponent.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.AccentColor
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.ThemeMode
import com.listen.uicomponent.theme.parseHexColor

data class PieChartItem(
    val label: String,
    val colorHex: String,
    val value: Double,
    val percentage: Float
)

@Composable
fun DonutChart(
    items: List<PieChartItem>,
    totalValue: Double,
    modifier: Modifier = Modifier,
    centerTitle: String = "Total",
    centerValueText: String = "",
    emptyText: String = "No Data"
) {
    val isEmpty = items.isEmpty() || totalValue <= 0

    val displayValue = centerValueText.ifBlank { "%.2f".format(totalValue) }
    val valueFontSize = when {
        displayValue.length > 13 -> 12.sp
        displayValue.length > 10 -> 14.sp
        displayValue.length > 7 -> 16.sp
        else -> 19.sp
    }

    val animationProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isEmpty) 0f else 1f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 650,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "DonutChartSweep"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val strokeWidth = 24.dp.toPx()
            val canvasSize = size.minDimension - strokeWidth
            val topLeftOffset = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(canvasSize, canvasSize)

            if (isEmpty) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.25f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    size = arcSize,
                    topLeft = topLeftOffset
                )
            } else {
                var startAngle = -90f
                val currentMaxAngle = 360f * animationProgress
                var accumulatedAngle = 0f

                items.forEach { item ->
                    val fullSweep = item.percentage * 360f
                    if (accumulatedAngle < currentMaxAngle) {
                        val sweep = (currentMaxAngle - accumulatedAngle).coerceAtMost(fullSweep)
                        val color = parseHexColor(item.colorHex)

                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = strokeWidth),
                            size = arcSize,
                            topLeft = topLeftOffset
                        )
                    }
                    startAngle += fullSweep
                    accumulatedAngle += fullSweep
                }
            }
        }

        // Inner Circle Text with Overflow Guard & Auto-scaling
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .widthIn(max = 126.dp)
                .padding(horizontal = 4.dp)
        ) {
            if (isEmpty) {
                Text(
                    text = emptyText,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            } else {
                if (centerTitle.isNotBlank()) {
                    Text(
                        text = centerTitle,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = displayValue,
                    fontSize = valueFontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonutChartPreview() {
    val sampleItems = listOf(
        PieChartItem("Food", "#FF5722", 150.0, 0.4f),
        PieChartItem("Transport", "#2196F3", 100.0, 0.25f),
        PieChartItem("Rent", "#4CAF50", 125.0, 0.35f)
    )
    ListenTheme(themeMode = ThemeMode.LIGHT, accentColor = AccentColor.EMERALD) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            DonutChart(
                items = sampleItems,
                totalValue = 375.0,
                centerTitle = "Total Expenses",
                centerValueText = "$375.00"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonutChartEmptyPreview() {
    ListenTheme(themeMode = ThemeMode.LIGHT, accentColor = AccentColor.EMERALD) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            DonutChart(
                items = emptyList(),
                totalValue = 0.0,
                centerTitle = "Total Expenses",
                centerValueText = "$0.00"
            )
        }
    }
}
