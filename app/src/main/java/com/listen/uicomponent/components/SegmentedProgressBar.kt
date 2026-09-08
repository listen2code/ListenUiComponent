package com.listen.uicomponent.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.parseHexColor

/**
 * 分段比例条数据项 (ProgressSegment)。
 *
 * @param colorHex 颜色十六进制字符串 (如 "#EF4444")
 * @param percentage 占比 (0.0f ~ 1.0f)
 */
data class ProgressSegment(
    val colorHex: String,
    val percentage: Float // 0.0f to 1.0f
)

/**
 * 根据横向触摸坐标 [x] 与总可用宽度 [totalWidth]，精准换算出命中属于哪个分段区块 [ProgressSegment]。
 *
 * @param segments 有效分段列表
 * @param x 触摸点相对进度条左边缘的 X 轴坐标 (像素)
 * @param totalWidth 进度条在当前屏幕上的总渲染宽度 (像素)
 * @return 命中的分段；若列表为空或宽度异常则返回 null
 */
fun findSegmentAtX(
    segments: List<ProgressSegment>,
    x: Float,
    totalWidth: Float
): ProgressSegment? {
    val validSegments = segments.filter { it.percentage > 0f }
    if (validSegments.isEmpty() || totalWidth <= 0f) return null

    val totalWeight = validSegments.sumOf { it.percentage.coerceAtLeast(0.001f).toDouble() }.toFloat()
    if (totalWeight <= 0f) return null

    val clampedX = x.coerceIn(0f, totalWidth)
    val ratio = clampedX / totalWidth
    val targetWeight = ratio * totalWeight

    var accumulated = 0f
    for (seg in validSegments) {
        accumulated += seg.percentage.coerceAtLeast(0.001f)
        if (accumulated >= targetWeight) {
            return seg
        }
    }
    return validSegments.lastOrNull()
}

/**
 * 通用分段比例进度条 (SegmentedProgressBar)。
 * 支持点击选择、长按后左右拖拽动态滑动切换选中区块，并伴随微触觉震动反馈。
 */
@Composable
fun SegmentedProgressBar(
    segments: List<ProgressSegment>,
    modifier: Modifier = Modifier,
    height: Dp = 10.dp,
    highlightColorHex: String? = null,
    trackColor: Color = Color.LightGray.copy(alpha = 0.25f),
    onSegmentClick: ((ProgressSegment) -> Unit)? = null,
    onSegmentSelect: ((ProgressSegment) -> Unit)? = null
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

    val haptic = LocalHapticFeedback.current
    // 使用 rememberUpdatedState 避免手势协程长时间持有旧参数闭包
    val currentSegments by rememberUpdatedState(segments)
    val currentOnClick by rememberUpdatedState(onSegmentClick)
    val currentOnSelect by rememberUpdatedState(onSegmentSelect)

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
            .pointerInput(dataSignature) {
                // 单击手势：根据点击 X 坐标寻找对应分段并分发点击事件
                detectTapGestures { offset ->
                    val clickedSeg = findSegmentAtX(currentSegments, offset.x, size.width.toFloat())
                    if (clickedSeg != null) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        currentOnClick?.invoke(clickedSeg) ?: currentOnSelect?.invoke(clickedSeg)
                    }
                }
            }
            .pointerInput(dataSignature) {
                // 长按拖动手势：长按唤醒后随手势左右拖动，动态计算手指所在区块并连续分发选择事件
                var lastDraggedSeg: ProgressSegment? = null
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        val seg = findSegmentAtX(currentSegments, offset.x, size.width.toFloat())
                        lastDraggedSeg = seg
                        if (seg != null) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            currentOnSelect?.invoke(seg) ?: currentOnClick?.invoke(seg)
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val seg = findSegmentAtX(currentSegments, change.position.x, size.width.toFloat())
                        if (seg != null && seg != lastDraggedSeg) {
                            lastDraggedSeg = seg
                            // 跨越区块时触发轻微触觉反馈，提升拖拽掌控感
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            currentOnSelect?.invoke(seg) ?: currentOnClick?.invoke(seg)
                        }
                    },
                    onDragEnd = {
                        lastDraggedSeg = null
                    },
                    onDragCancel = {
                        lastDraggedSeg = null
                    }
                )
            }
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
                        .background(animatedColor)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedProgressBarPreview() {
    ListenTheme {
        val sampleSegments = listOf(
            ProgressSegment(percentage = 0.5f, colorHex = "#EF4444"),
            ProgressSegment(percentage = 0.3f, colorHex = "#3B82F6"),
            ProgressSegment(percentage = 0.2f, colorHex = "#10B981")
        )
        Box(modifier = Modifier.padding(16.dp)) {
            SegmentedProgressBar(segments = sampleSegments)
        }
    }
}
