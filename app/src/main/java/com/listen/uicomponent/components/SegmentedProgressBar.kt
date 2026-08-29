package com.listen.uicomponent.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.listen.uicomponent.theme.parseHexColor

data class ProgressSegment(
    val colorHex: String,
    val percentage: Float // 0.0f to 1.0f
)

@Composable
fun SegmentedProgressBar(
    segments: List<ProgressSegment>,
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    highlightColorHex: String? = null,
    trackColor: Color = Color.LightGray.copy(alpha = 0.25f),
    onSegmentClick: ((ProgressSegment) -> Unit)? = null
) {
    // Unique data fingerprint calculated from the current segments list
    val dataSignature = remember(segments) {
        segments.hashCode().toString()
    }
    // Preserves the last animated signature across LazyColumn scroll recycling
    var animatedSignature by rememberSaveable { mutableStateOf("") }
    // Initialize directly to 1f if this data was already animated to avoid scroll re-trigger
    val animProgress = remember {
        Animatable(if (animatedSignature == dataSignature && segments.isNotEmpty()) 1f else 0f)
    }

    // Only trigger expand animation when segment data actually changes
    LaunchedEffect(dataSignature) {
        if (segments.isEmpty()) {
            animProgress.snapTo(0f)
        } else if (animatedSignature != dataSignature) {
            animatedSignature = dataSignature
            animProgress.snapTo(0f)
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(trackColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = animProgress.value)
                .fillMaxHeight()
                .clip(RoundedCornerShape(height / 2))
        ) {
            segments.filter { it.percentage > 0f }.forEach { seg ->
                val isHighlighted = highlightColorHex != null && seg.colorHex.equals(highlightColorHex, ignoreCase = true)
                val baseColor = parseHexColor(seg.colorHex)
                val targetColor = when {
                    highlightColorHex == null -> baseColor
                    isHighlighted -> baseColor
                    else -> baseColor.copy(alpha = 0.25f)
                }
                val animatedColor by animateColorAsState(
                    targetValue = targetColor,
                    animationSpec = tween(durationMillis = 250),
                    label = "SegmentHighlightAnim"
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(seg.percentage.coerceAtLeast(0.001f))
                        .then(
                            if (onSegmentClick != null) {
                                Modifier.clickable { onSegmentClick(seg) }
                            } else Modifier
                        )
                        .background(animatedColor)
                )
            }
        }
    }
}
