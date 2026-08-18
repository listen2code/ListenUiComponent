package com.listen.uicomponent.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    height: Dp = 160.dp,
    barWidth: Dp = 22.dp,
    trackColor: Color = Color.LightGray.copy(alpha = 0.2f)
) {
    if (items.isEmpty()) return

    val maxValue = items.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEach { item ->
                val ratio = (item.value / maxValue).toFloat().coerceIn(0.05f, 1f)
                val color = parseHexColor(item.colorHex)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = if (item.value > 0) String.format("%.0f", item.value) else "",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .height(height * 0.75f * ratio)
                            .fillMaxWidth(1f / items.size.coerceAtLeast(1) * 0.65f)
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(color)
                    )

                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }
    }
}
