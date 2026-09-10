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
import androidx.compose.ui.tooling.preview.Preview
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.ExpenseRed
import com.listen.uicomponent.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 内部辅助数据结构，作为一个轻量级的替代方案用于处理多语言文本元组，
 * 避免了为仅仅传递几个 i18n 字符串而创建专门的数据类。
 */
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

    // 通过不同颜色直观展示日志严重程度，建立视觉层级结构 (Gray=DEBUG, Green=INFO, Amber=WARN, Red=ERROR)
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
            // 使用带 50% 透明度的 surfaceVariant 颜色作为卡片背景。
            // 这在保持文本高可读性的同时，避免了视觉上的厚重感
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
                            // 等宽字体设计，确保技术日志中 ID 信息的对齐和排版一致性
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
                // 等宽字体对齐文本内容
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
                    // 限制最大行数，防止单条 Crash 堆栈信息过长导致整个列表可见区域被完全霸占
                    maxLines = 6
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun LogItemRowPreview() {
    ListenTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LogItemRow(
                log = LogEntryUi(
                    id = "1",
                    timestamp = System.currentTimeMillis(),
                    levelName = "INFO",
                    channelName = "DB",
                    tag = "TransactionDao",
                    message = "Query executed in 4.2ms",
                    traceId = "tr-101"
                )
            )
        }
    }
}
