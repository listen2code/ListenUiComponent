package com.listen.uicomponent.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
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
    centerTitle: String = "Total",
    centerValueText: String = "",
    emptyText: String = "No Data",
    modifier: Modifier = Modifier
) {
    val isEmpty = items.isEmpty() || totalValue <= 0

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val strokeWidth = 32.dp.toPx()
            val canvasSize = size.minDimension - strokeWidth
            val topLeftOffset = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(canvasSize, canvasSize)

            if (isEmpty) {
                // Draw a light gray placeholder ring for empty state
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    size = arcSize,
                    topLeft = topLeftOffset
                )
            } else {
                var startAngle = -90f
                items.forEach { item ->
                    val sweepAngle = item.percentage * 360f
                    val color = parseHexColor(item.colorHex)

                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth),
                        size = arcSize,
                        topLeft = topLeftOffset
                    )

                    startAngle += sweepAngle
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isEmpty) {
                Text(
                    text = emptyText,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                if (centerTitle.isNotBlank()) {
                    Text(
                        text = centerTitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = centerValueText.ifBlank { String.format("%.2f", totalValue) },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
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
