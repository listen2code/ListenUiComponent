package com.listen.uicomponent.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ListenTheme

/**
 * Universal filter capsule and tag component supporting selectable and removable modes.
 *
 * @param label Text displayed inside the chip
 * @param modifier Composable modifier
 * @param selected Whether the chip is currently active in selectable mode
 * @param onClick Callback triggered when tapping the chip
 * @param removable If true, renders a right-side 'X' close button
 * @param onRemove Callback triggered when tapping the 'X' close button
 * @param icon Optional leading vector icon
 * @param iconTint Optional tint color for leading icon
 * @param height Height of the chip container
 * @param cornerRadius Corner radius of the chip
 */
@Composable
fun CommonFilterChip(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    removable: Boolean = false,
    onRemove: (() -> Unit)? = null,
    icon: ImageVector? = null,
    iconTint: Color? = null,
    height: Dp = 28.dp,
    cornerRadius: Dp = 8.dp
) {
    val containerColor = if (selected || removable) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    }

    val borderColor = if (selected || removable) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    }

    val textColor = if (selected || removable) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(cornerRadius),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(start = 8.dp, end = if (removable) 4.dp else 8.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint ?: (if (selected || removable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected || removable) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor
            )
            if (removable && onRemove != null) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        .clickable(onClick = onRemove),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonFilterChipPreview() {
    ListenTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            CommonFilterChip(label = "Selected", selected = true, onClick = {})
            CommonFilterChip(label = "Unselected", selected = false, onClick = {})
            CommonFilterChip(label = "Removable Tag", removable = true, onRemove = {})
        }
    }
}
