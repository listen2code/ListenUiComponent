package com.listen.uicomponent.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Universal text component across Listen applications supporting theme tokens and auto-scaling.
 *
 * @param text Content string
 * @param modifier Composable modifier
 * @param color Text color
 * @param fontSize Font size
 * @param fontWeight Font weight
 * @param maxLines Maximum line count
 * @param overflow Text overflow behavior
 * @param textAlign Text alignment
 * @param autoResize Whether to downscale automatically if overflowing
 * @param minFontSize Minimum font size when autoResize is enabled
 * @param style Text style
 */
@Composable
fun CommonText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null,
    autoResize: Boolean = false,
    minFontSize: TextUnit = 9.sp,
    style: TextStyle = TextStyle.Default
) {
    if (autoResize && maxLines == 1) {
        AutoResizeText(
            text = text,
            modifier = modifier,
            targetTextSize = fontSize,
            minTextSize = minFontSize,
            maxLines = 1,
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
            style = style
        )
    } else {
        Text(
            text = text,
            modifier = modifier,
            color = if (color == Color.Unspecified) MaterialTheme.colorScheme.onSurface else color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = maxLines,
            overflow = overflow,
            textAlign = textAlign,
            style = style
        )
    }
}
