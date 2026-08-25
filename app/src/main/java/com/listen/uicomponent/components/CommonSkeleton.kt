package com.listen.uicomponent.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modifier extension applying a high-performance animated shimmer gradient for skeleton loading states.
 */
fun Modifier.shimmer(
    durationMillis: Int = 1000,
    baseColor: Color = Color.Unspecified,
    highlightColor: Color = Color.Unspecified
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerTranslate"
    )

    val actualBase = if (baseColor != Color.Unspecified) baseColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    val actualHighlight = if (highlightColor != Color.Unspecified) highlightColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)

    val brush = Brush.linearGradient(
        colors = listOf(actualBase, actualHighlight, actualBase),
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    background(brush)
}

/**
 * Universal rectangular or rounded skeleton block.
 *
 * @param modifier Composable modifier (first optional parameter)
 * @param width Block width (null for fillMaxWidth)
 * @param height Block height
 * @param cornerRadius Corner radius
 */
@Composable
fun CommonSkeletonBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 16.dp,
    cornerRadius: Dp = 4.dp
) {
    Box(
        modifier = modifier
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .shimmer()
    )
}

/**
 * Pre-configured skeleton row mimicking a list item (Leading circle + 2 lines of text).
 *
 * @param modifier Composable modifier (first optional parameter)
 * @param hasLeadingCircle Whether to render a circle avatar on the left
 */
@Composable
fun CommonSkeletonRow(
    modifier: Modifier = Modifier,
    hasLeadingCircle: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (hasLeadingCircle) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommonSkeletonBox(height = 14.dp, width = 140.dp)
            CommonSkeletonBox(height = 10.dp, width = 220.dp)
        }
    }
}
