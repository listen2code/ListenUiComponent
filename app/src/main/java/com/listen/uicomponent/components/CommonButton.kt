package com.listen.uicomponent.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
 *
 * @param text Button label text
 * @param onClick Click callback
 * @param modifier Composable modifier (first optional parameter)
 * @param enabled Whether the button is enabled
 * @param style Visual style variant
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
    icon: (@Composable () -> Unit)? = null,
    cornerRadius: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
) {
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
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                contentPadding = contentPadding
            ) {
                content()
            }
        }
        CommonButtonStyle.Text -> {
            TextButton(
                onClick = onClick,
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
                onClick = onClick,
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

/**
 * Text component that automatically downscales its font size to fit within single line bounds without wrapping.
 */
@Composable
fun AutoResizeText(
    text: String,
    modifier: Modifier = Modifier,
    targetTextSize: TextUnit = 14.sp,
    minTextSize: TextUnit = 9.sp,
    maxLines: Int = 1,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    style: TextStyle = TextStyle.Default
) {
    var textSize by remember(text, targetTextSize) { mutableStateOf(targetTextSize) }
    var readyToDraw by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontWeight = fontWeight,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        fontSize = textSize,
        softWrap = false,
        style = style,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth && textSize > minTextSize) {
                textSize = (textSize.value * 0.9f).sp
            } else {
                readyToDraw = true
            }
        },
        modifier = modifier.drawWithContent {
            if (readyToDraw) {
                drawContent()
            }
        }
    )
}
