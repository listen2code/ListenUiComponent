package com.listen.uicomponent.apm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ExpenseRed
import com.listen.uicomponent.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)

/**
 * Individual log entry card for APM Inspector.
 */
@Composable
internal fun LogItemRow(
    log: LogEntryUi,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    val timeStr = sdf.format(Date(log.timestamp))

    val levelColor = when (log.levelName) {
        "DEBUG" -> Color.Gray
        "INFO" -> IncomeGreen
        "WARN" -> Color(0xFFF59E0B)
        "ERROR" -> ExpenseRed
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(6.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "[${log.channelName}]",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "[${log.levelName}]",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = levelColor
                    )
                    Text(
                        text = log.tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    log.traceId?.let { trace ->
                        Text(
                            text = "[$trace]",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                Text(
                    text = timeStr,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = log.message,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )

            log.stackTrace?.let { stack ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stack,
                    fontSize = 9.sp,
                    color = ExpenseRed,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 6
                )
            }
        }
    }
}
