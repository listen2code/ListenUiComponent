package com.listen.uicomponent.charts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * X-Axis date label generator displaying spaced date ticks precisely aligned with canvas X coordinates.
 */
@Composable
fun LineChartXAxisLabels(
    points: List<LineChartPoint>,
    modifier: Modifier = Modifier
) {
    val total = points.size
    if (total == 0) return

    val step = if (total <= 15) 1 else 2
    val labelIndices = remember(total, step) {
        val list = mutableListOf<Int>()
        var curr = 0
        while (curr < total) {
            list.add(curr)
            curr += step
        }
        if (!list.contains(total - 1)) {
            if (list.isNotEmpty() && total - 1 - list.last() == 1) {
                list[list.size - 1] = total - 1
            } else {
                list.add(total - 1)
            }
        }
        list
    }

    Layout(
        content = {
            labelIndices.forEach { index ->
                val pt = points.getOrNull(index) ?: return@forEach
                Text(
                    text = pt.label,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
        val width = constraints.maxWidth
        val height = placeables.maxOfOrNull { it.height } ?: 0
        val maxIndex = (total - 1).coerceAtLeast(1)

        layout(width, height) {
            placeables.forEachIndexed { i, placeable ->
                val pointIndex = labelIndices[i]
                val centerX = (pointIndex.toFloat() / maxIndex) * width
                val left = (centerX - placeable.width / 2f).roundToInt()
                    .coerceIn(0, width - placeable.width)
                placeable.placeRelative(left, 0)
            }
        }
    }
}
