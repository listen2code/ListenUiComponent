package com.listen.uicomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ListenTheme

/**
 * Universal navigation capsule component with previous/next buttons, center title, and swipe gesture support.
 *
 * @param title Center title text
 * @param onPrevious Callback triggered when tapping previous button or swiping right
 * @param onNext Callback triggered when tapping next button or swiping left
 * @param modifier Composable modifier
 * @param onTitleClick Optional callback when center title is tapped
 * @param enableSwipe Whether horizontal drag/swipe gestures are enabled
 * @param cornerRadius Capsule corner radius
 */
@Composable
fun CommonNavigationCapsule(
    title: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    onTitleClick: (() -> Unit)? = null,
    enableSwipe: Boolean = true,
    cornerRadius: Dp = 20.dp
) {
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .then(
                if (enableSwipe) {
                    Modifier.pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { dragAccumulator = 0f },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                dragAccumulator += dragAmount
                            },
                            onDragEnd = {
                                val thresholdPx = 50f
                                if (dragAccumulator > thresholdPx) {
                                    onPrevious()
                                } else if (dragAccumulator < -thresholdPx) {
                                    onNext()
                                }
                                dragAccumulator = 0f
                            },
                            onDragCancel = {
                                dragAccumulator = 0f
                            }
                        )
                    }
                } else Modifier
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onPrevious,
                modifier = Modifier.size(26.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (onTitleClick != null) Modifier.clickable(onClick = onTitleClick) else Modifier
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )

            IconButton(
                onClick = onNext,
                modifier = Modifier.size(26.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonNavigationCapsulePreview() {
    ListenTheme {
        CommonNavigationCapsule(
            title = "2026年09月",
            onPrevious = {},
            onNext = {},
            onTitleClick = {}
        )
    }
}
