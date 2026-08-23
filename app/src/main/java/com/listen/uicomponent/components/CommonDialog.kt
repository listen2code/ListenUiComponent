package com.listen.uicomponent.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Universal Dialog component for Listen applications.
 *
 * @param onDismissRequest Callback when dialog is dismissed
 * @param title Dialog title
 * @param modifier Composable modifier
 * @param icon Optional leading icon
 * @param confirmButton Optional confirm button slot
 * @param dismissButton Optional dismiss button slot
 * @param cornerRadius Dialog card corner radius
 * @param content Dialog body content
 */
@Composable
fun CommonDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        icon = icon,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = content,
        confirmButton = {
            if (confirmButton != null) {
                confirmButton()
            }
        },
        dismissButton = {
            if (dismissButton != null) {
                dismissButton()
            }
        },
        shape = RoundedCornerShape(cornerRadius),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )
}
