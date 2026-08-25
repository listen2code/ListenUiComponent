package com.listen.uicomponent.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ExpenseRed
import com.listen.uicomponent.theme.IncomeGreen

/**
 * Semantic type variants for CommonSnackbar.
 */
enum class CommonSnackbarType {
    Success,
    Error,
    Warning,
    Info
}

/**
 * Universal pill-shaped floating snackbar/toast component for displaying feedback.
 *
 * @param message Feedback text to display
 * @param modifier Composable modifier (first optional parameter)
 * @param type Semantic visual style (Success, Error, Warning, Info)
 * @param visible Visibility controller for entry/exit animations
 * @param actionLabel Optional action button text (e.g. "Undo", "Retry")
 * @param onActionClick Optional callback when action button is tapped
 * @param cornerRadius Pill corner radius
 */
@Composable
fun CommonSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    type: CommonSnackbarType = CommonSnackbarType.Info,
    visible: Boolean = true,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    cornerRadius: Dp = 24.dp
) {
    val (icon: ImageVector, accentColor: Color) = when (type) {
        CommonSnackbarType.Success -> Pair(Icons.Default.Check, IncomeGreen)
        CommonSnackbarType.Error -> Pair(Icons.Default.Warning, ExpenseRed)
        CommonSnackbarType.Warning -> Pair(Icons.Default.Warning, Color(0xFFF59E0B))
        CommonSnackbarType.Info -> Pair(Icons.Default.Info, MaterialTheme.colorScheme.primary)
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(cornerRadius),
            color = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }

                if (actionLabel != null && onActionClick != null) {
                    TextButton(
                        onClick = onActionClick,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = actionLabel,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
