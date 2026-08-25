package com.listen.uicomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Universal slim divider component with customizable indentation and alpha transparency.
 *
 * @param modifier Composable modifier (first optional parameter)
 * @param thickness Divider stroke thickness
 * @param color Line color (defaults to outline variant with slight transparency)
 * @param startIndent Inset spacing on start/left
 * @param endIndent Inset spacing on end/right
 */
@Composable
fun CommonDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 0.5.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = startIndent, end = endIndent)
            .height(thickness)
            .background(color)
    )
}
