package com.listen.uicomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ExpenseRed
import com.listen.uicomponent.theme.IncomeGreen

/**
 * Visual variant styles for CommonBadge and CommonTag.
 */
enum class CommonBadgeStyle {
    Primary,
    Secondary,
    Success,
    Warning,
    Error,
    Neutral
}

/**
 * Size variants for CommonBadge.
 */
enum class CommonBadgeSize {
    Small,
    Medium
}

/**
 * Universal badge and status chip component.
 *
 * @param text Badge text
 * @param modifier Composable modifier (first optional parameter)
 * @param style Semantic visual style
 * @param size Predefined size variant
 * @param showDot Whether to render a leading colored status dot
 * @param icon Optional leading icon slot
 * @param cornerRadius Custom corner radius
 */
@Composable
fun CommonBadge(
    text: String,
    modifier: Modifier = Modifier,
    style: CommonBadgeStyle = CommonBadgeStyle.Primary,
    size: CommonBadgeSize = CommonBadgeSize.Small,
    showDot: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    cornerRadius: Dp = 6.dp
) {
    val (bgColor: Color, textColor: Color) = when (style) {
        CommonBadgeStyle.Primary -> Pair(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), MaterialTheme.colorScheme.primary)
        CommonBadgeStyle.Secondary -> Pair(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f), MaterialTheme.colorScheme.secondary)
        CommonBadgeStyle.Success -> Pair(IncomeGreen.copy(alpha = 0.12f), IncomeGreen)
        CommonBadgeStyle.Warning -> Pair(Color(0xFFF59E0B).copy(alpha = 0.14f), Color(0xFFD97706))
        CommonBadgeStyle.Error -> Pair(ExpenseRed.copy(alpha = 0.12f), ExpenseRed)
        CommonBadgeStyle.Neutral -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    val (horizontalPad, verticalPad, fontSize) = when (size) {
        CommonBadgeSize.Small -> Triple(6.dp, 2.dp, 10.sp)
        CommonBadgeSize.Medium -> Triple(8.dp, 4.dp, 12.sp)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(bgColor)
            .padding(horizontal = horizontalPad, vertical = verticalPad),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (showDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(textColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
            } else if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                maxLines = 1
            )
        }
    }
}
