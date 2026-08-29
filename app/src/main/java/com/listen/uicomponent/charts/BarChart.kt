package com.listen.uicomponent.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.parseHexColor

data class BarChartItem(
    val label: String,
    val value: Double,
    val colorHex: String
)

/**
 * Clean 7-day trend bar chart with robust spacing and zero text overlap/clipping.
 *
 * @param items List of BarChartItem
 * @param modifier Composable modifier (first optional parameter)
 * @param trackHeight Height of the background pill track (excluding labels)
 * @param barWidth Width of each bar
 * @param trackColor Background track color
 */
@Composable
fun BarChart(
    items: List<BarChartItem>,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 96.dp,
    barWidth: Dp = 16.dp,
    trackColor: Color = Color.Gray.copy(alpha = 0.12f)
) {
    if (items.isEmpty()) return

    val maxValue = items.maxOfOrNull { it.value }?.coerceAtLeast(1.0) ?: 1.0

    // Unique data fingerprint calculated from current bar items list
    val dataSignature = remember(items) {
        items.hashCode().toString()
    }
    // Preserves the last animated signature across LazyColumn scroll recycling
    var animatedSignature by rememberSaveable { mutableStateOf("") }
    // Initialize directly to 1f if this data was already animated to avoid scroll re-trigger
    val animProgress = remember {
        Animatable(if (animatedSignature == dataSignature && items.isNotEmpty()) 1f else 0f)
    }

    // Only trigger bar growth animation when items dataset actually changes
    LaunchedEffect(dataSignature) {
        if (items.isEmpty()) {
            animProgress.snapTo(0f)
        } else if (animatedSignature != dataSignature) {
            animatedSignature = dataSignature
            animProgress.snapTo(0f)
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 650,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEach { item ->
                val ratio = if (item.value > 0) {
                    (item.value / maxValue).toFloat().coerceIn(0.12f, 1f)
                } else {
                    0f
                }
                val color = if (item.value > 0) parseHexColor(item.colorHex) else Color.Transparent
                val activeBarHeight = trackHeight * ratio * animProgress.value

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    // 1. Top Value Label (Dedicated 18.dp height slot, never overlaps)
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        if (item.value > 0) {
                            Text(
                                text = "%.0f".format(item.value),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 2. Bar Track with Filled Value Bar Inside
                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height(trackHeight)
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
                            // Subtle indicator dot for 0 expenditure
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 3.dp)
                                    .size(3.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 3. Bottom Date/Weekday Label (Dedicated 20.dp slot, never clipped)
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = item.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
