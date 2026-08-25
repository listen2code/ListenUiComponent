package com.listen.uicomponent.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ExpenseRed
import com.listen.uicomponent.theme.IncomeGreen

/**
 * Visual variant styles for CommonBanner.
 */
enum class CommonBannerType {
    Info,
    Warning,
    Error,
    Success
}

/**
 * Universal notice and banner component for persistent or actionable top-level messages.
 *
 * @param message Notice message text
 * @param modifier Composable modifier (first optional parameter)
 * @param title Optional bold header title
 * @param type Semantic style variant (Info, Warning, Error, Success)
 * @param visible Visibility controller for animated display
 * @param actionText Optional action button text
 * @param onActionClick Optional callback when action is tapped
 * @param onClose Optional close callback to render a dismiss icon button
 * @param cornerRadius Banner card corner radius
 */
@Composable
fun CommonBanner(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    type: CommonBannerType = CommonBannerType.Info,
    visible: Boolean = true,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    cornerRadius: Dp = 12.dp
) {
    val (icon: ImageVector, accentColor: Color, containerColor: Color) = when (type) {
        CommonBannerType.Info -> Triple(Icons.Default.Info, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
        CommonBannerType.Warning -> Triple(Icons.Default.Warning, Color(0xFFF59E0B), Color(0xFFF59E0B).copy(alpha = 0.12f))
        CommonBannerType.Error -> Triple(Icons.Default.Warning, ExpenseRed, ExpenseRed.copy(alpha = 0.12f))
        CommonBannerType.Success -> Triple(Icons.Default.Check, IncomeGreen, IncomeGreen.copy(alpha = 0.12f))
    }

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(containerColor)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (title != null) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = message,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    if (actionText != null && onActionClick != null) {
                        TextButton(
                            onClick = onActionClick,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = actionText,
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                if (onClose != null) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
