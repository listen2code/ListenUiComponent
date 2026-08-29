package com.listen.uicomponent.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
    emptyText: String = "No Data",
    currencySymbol: String = "￥",
    hideAmount: Boolean = false,
    selectedItem: PieChartItem? = null,
    onSelectionChange: ((PieChartItem?) -> Unit)? = null,
    onTooltipClick: ((PieChartItem) -> Unit)? = null
) {
    val isEmpty = items.isEmpty() || totalValue <= 0

    val displayValue = centerValueText.ifBlank { "%.2f".format(totalValue) }
    val valueFontSize = when {
        displayValue.length > 13 -> 12.sp
        displayValue.length > 10 -> 14.sp
        displayValue.length > 7 -> 16.sp
        else -> 19.sp
    }

    var internalSelectedItem by remember { mutableStateOf<PieChartItem?>(null) }
    val currentSelectedItem = if (onSelectionChange != null) selectedItem else internalSelectedItem
    var tapOffset by remember { mutableStateOf<Offset?>(null) }

    LaunchedEffect(items, totalValue) {
        if (onSelectionChange != null) onSelectionChange(null) else internalSelectedItem = null
        tapOffset = null
    }
    val dataSignature = remember(items, totalValue) { "${items.hashCode()}_${totalValue}" }
    var animatedSignature by rememberSaveable { mutableStateOf("") }
    val animatableProgress = remember { Animatable(if (animatedSignature == dataSignature && !isEmpty) 1f else 0f) }

    // Only trigger entry sweep animation when actual data contents change
    LaunchedEffect(dataSignature) {
        if (isEmpty) {
            animatableProgress.snapTo(0f)
        } else if (animatedSignature != dataSignature) {
            animatedSignature = dataSignature
            animatableProgress.snapTo(0f)
            animatableProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
            )
        }
    }
    val animationProgress = animatableProgress.value

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(180.dp)
                .pointerInput(items, isEmpty) {
                    if (isEmpty) return@pointerInput
                    detectTapGestures { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        val distance = kotlin.math.sqrt(dx * dx + dy * dy)
                        val outerRadius = kotlin.math.min(size.width, size.height) / 2f
                        val innerRadius = outerRadius - 24.dp.toPx()

                        if (distance >= innerRadius * 0.6f && distance <= outerRadius * 1.3f) {
                            val angleRad = kotlin.math.atan2(dy, dx)
                            val angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat()
                            val normalizedAngle = (angleDeg + 90f + 360f) % 360f

                            var accumulated = 0f
                            var matched: PieChartItem? = null
                            for (item in items) {
                                val sweep = item.percentage * 360f
                                if (normalizedAngle >= accumulated && normalizedAngle < accumulated + sweep) {
                                    matched = item
                                    break
                                }
                                accumulated += sweep
                            }
                            val matchedItem = if (matched != null && matched != currentSelectedItem) matched else null
                            if (onSelectionChange != null) onSelectionChange(matchedItem) else internalSelectedItem = matchedItem
                            tapOffset = if (matchedItem != null) offset else null
                        } else {
                            if (onSelectionChange != null) onSelectionChange(null) else internalSelectedItem = null
                            tapOffset = null
                        }
                    }
                }
        ) {
            val strokeWidth = 24.dp.toPx()
            val canvasSize = size.minDimension - strokeWidth
            val topLeftOffset = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(canvasSize, canvasSize)

            if (isEmpty) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.25f), startAngle = 0f, sweepAngle = 360f,
                    useCenter = false, style = Stroke(width = strokeWidth), size = arcSize, topLeft = topLeftOffset
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
                        val isSelected = currentSelectedItem == item
                        val actualStroke = if (isSelected) strokeWidth + 5.dp.toPx() else strokeWidth

                        drawArc(
                            color = color, startAngle = startAngle, sweepAngle = sweep,
                            useCenter = false, style = Stroke(width = actualStroke), size = arcSize, topLeft = topLeftOffset
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

        val density = androidx.compose.ui.platform.LocalDensity.current
        val currentItem = currentSelectedItem
        val effectiveTapOffset = tapOffset ?: currentItem?.let { selected ->
            var start = -90f
            var targetMid: Float? = null
            for (it in items) {
                val sweep = it.percentage * 360f
                if (it == selected) { targetMid = start + sweep / 2f; break }
                start += sweep
            }
            targetMid?.let { midDeg ->
                val rad = Math.toRadians(midDeg.toDouble())
                with(density) {
                    val c = 90.dp.toPx()
                    val r = 78.dp.toPx()
                    Offset(c + (r * kotlin.math.cos(rad)).toFloat(), c + (r * kotlin.math.sin(rad)).toFloat())
                }
            }
        }
        if (currentItem != null && effectiveTapOffset != null) {
            DonutChartTooltip(
                item = currentItem,
                tapOffset = effectiveTapOffset,
                currencySymbol = currencySymbol,
                hideAmount = hideAmount,
                onDismissRequest = {
                    if (onSelectionChange != null) onSelectionChange(null) else internalSelectedItem = null
                    tapOffset = null
                },
                onItemClick = onTooltipClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonutChartPreview() {
    ListenTheme(themeMode = ThemeMode.LIGHT, accentColor = AccentColor.EMERALD) {
        Surface(modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.surface) {
            DonutChart(
                items = listOf(PieChartItem("Food", "#FF5722", 150.0, 0.6f), PieChartItem("Rent", "#4CAF50", 100.0, 0.4f)),
                totalValue = 250.0, centerTitle = "Total", centerValueText = "$250.00"
            )
        }
    }
}
