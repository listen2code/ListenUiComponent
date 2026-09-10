package com.listen.uicomponent.components

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ListenTheme

enum class CommonButtonStyle {
    Primary,
    Secondary,
    Tonal,
    Outlined,
    Text,
    Danger
}

/**
 * Universal Button component with auto-scaling single-line text to prevent unexpected line wrapping.
 * Includes built-in debounce/throttling to prevent duplicate triggers from rapid accidental clicks.
 *
 * @param text Button label text
 * @param onClick Click callback
 * @param modifier Composable modifier (first optional parameter)
 * @param enabled Whether the button is enabled
 * @param style Visual style variant
 * @param debounceIntervalMs 防重复点击的限制间隔时间（毫秒，默认 500ms；<= 0 时不限制）
 * @param icon Optional leading icon
 * @param cornerRadius Button corner radius
 * @param contentPadding Inner padding values
 */
@Composable
fun CommonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: CommonButtonStyle = CommonButtonStyle.Primary,
    debounceIntervalMs: Long = 500L,
    icon: (@Composable () -> Unit)? = null,
    cornerRadius: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
) {
    // [Fix/Feature] 防连击保护：避免用户快速连续点击（例如手抖、网络未响应时的狂点）导致多次提交、重复弹窗或并发请求。
    // 使用 SystemClock.uptimeMillis() 单调递增时钟进行时间差判断，若距离上次触发未达到指定毫秒阈值则拦截此次点击。
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val debouncedOnClick: () -> Unit = remember(onClick, debounceIntervalMs) {
        {
            val currentTime = SystemClock.uptimeMillis()
            if (debounceIntervalMs <= 0L || currentTime - lastClickTime >= debounceIntervalMs) {
                lastClickTime = currentTime
                onClick()
            }
        }
    }

    val shape = RoundedCornerShape(cornerRadius)

    val colors = when (style) {
        CommonButtonStyle.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
        CommonButtonStyle.Secondary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        CommonButtonStyle.Tonal -> ButtonDefaults.filledTonalButtonColors()
        CommonButtonStyle.Danger -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )
        CommonButtonStyle.Outlined, CommonButtonStyle.Text -> null
    }

    val content: @Composable () -> Unit = {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(6.dp))
            }
            AutoResizeText(
                text = text,
                maxLines = 1,
                targetTextSize = 12.sp,
                minTextSize = 8.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    when (style) {
        CommonButtonStyle.Outlined -> {
            OutlinedButton(
                onClick = debouncedOnClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = contentPadding
            ) {
                content()
            }
        }
        CommonButtonStyle.Text -> {
            TextButton(
                onClick = debouncedOnClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                contentPadding = contentPadding
            ) {
                content()
            }
        }
        else -> {
            Button(
                onClick = debouncedOnClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                colors = colors ?: ButtonDefaults.buttonColors(),
                contentPadding = contentPadding
            ) {
                content()
            }
        }
    }
}


// 完善 Preview，全量覆盖全部 6 种按钮风格 (Primary, Secondary, Tonal, Outlined, Danger, Text) 及防抖参数
@Preview(showBackground = true)
@Composable
fun CommonButtonPreview() {
    ListenTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommonButton(text = "Primary", onClick = {}, style = CommonButtonStyle.Primary)
                CommonButton(text = "Secondary", onClick = {}, style = CommonButtonStyle.Secondary)
                CommonButton(text = "Tonal", onClick = {}, style = CommonButtonStyle.Tonal)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommonButton(text = "Outlined", onClick = {}, style = CommonButtonStyle.Outlined)
                CommonButton(text = "Danger", onClick = {}, style = CommonButtonStyle.Danger)
                CommonButton(text = "Text", onClick = {}, style = CommonButtonStyle.Text)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommonButton(text = "Debounced (200ms)", onClick = {}, debounceIntervalMs = 200L)
            }
        }
    }
}
