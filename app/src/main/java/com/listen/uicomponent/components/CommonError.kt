package com.listen.uicomponent.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Universal error / failure state component with retry action support.
 *
 * @param title Primary error title string
 * @param modifier Composable modifier (first optional parameter)
 * @param message Optional detailed description or error explanation
 * @param icon Error image vector icon (defaults to Icons.Default.Warning)
 * @param retryText Optional text for retry button (e.g. "Retry", "Reload")
 * @param onRetry Optional callback invoked when retry button is tapped
 * @param height Custom container height
 */
@Composable
fun CommonError(
    title: String = "Something went wrong",
    modifier: Modifier = Modifier,
    message: String? = null,
    icon: ImageVector = Icons.Default.Warning,
    retryText: String? = "Retry",
    onRetry: (() -> Unit)? = null,
    height: Dp = 220.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (message != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            if (retryText != null && onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                CommonButton(
                    text = retryText,
                    onClick = onRetry,
                    style = CommonButtonStyle.Primary
                )
            }
        }
    }
}
