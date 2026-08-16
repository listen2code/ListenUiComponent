package com.listen.uicomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.listen.uicomponent.theme.parseHexColor

@Composable
fun IconBadge(
    imageVector: ImageVector,
    colorHex: String,
    modifier: Modifier = Modifier,
    badgeSize: Dp = 42.dp,
    iconSize: Dp = 22.dp
) {
    val bg = parseHexColor(colorHex)

    Box(
        modifier = modifier
            .size(badgeSize)
            .clip(CircleShape)
            .background(bg.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = bg,
            modifier = Modifier.size(iconSize)
        )
    }
}
