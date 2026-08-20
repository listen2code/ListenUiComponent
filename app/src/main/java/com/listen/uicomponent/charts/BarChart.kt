package com.listen.uicomponent.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.parseHexColor

data class BarChartItem(
    val label: String,
    val value: Double,
    val colorHex: String
)

@Composable
fun BarChart(
    items: List<BarChartItem>,
    modifier: Modifier = Modifier,
    height: Dp = 140.dp,
    barWidth: Dp = 18.dp,
    trackColor: Color = Color.Gray.copy(alpha = 0.12f)
) {
    if (items.isEmpty()) return

    val maxValue = items.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0
    val chartTrackHeight = height - 42.dp // leave room for amount label on top and date label below

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEach { item ->
                val ratio = if (item.value > 0) {
                    (item.value / maxValue).toFloat().coerceIn(0.12f, 1f)
                } else {
                    0f
                }
                val color = if (item.value > 0) parseHexColor(item.colorHex) else Color.Transparent
                val activeBarHeight = chartTrackHeight * ratio

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Top Value Label
                    Text(
                        text = if (item.value > 0) String.format("%.0f", item.value) else "",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    // Pill Track with Filled Active Bar Inside
                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height(chartTrackHeight)
                            .clip(RoundedCornerShape(barWidth / 2))
                            .background(trackColor),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        if (item.value > 0) {
                            Box(
                                modifier = Modifier
                                    .width(barWidth)
                                    .height(activeBarHeight)
                                    .clip(RoundedCornerShape(barWidth / 2))
                                    .background(color)
                            )
                        } else {
                            // Small subtle dot at bottom for 0 days
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f))
                                    .padding(bottom = 2.dp)
                            )
                        }
                    }

                    // Bottom Date Label
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
