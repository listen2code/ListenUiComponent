package com.listen.uicomponent.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ListenTheme

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

@Preview(showBackground = true)
@Composable
fun CommonTextPreview() {
    ListenTheme {
        CommonText(text = "Hello Listen UI Component")
    }
}
