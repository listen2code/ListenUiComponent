package com.listen.uicomponent.apm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

data class LogEntryUi(
    val id: String,
    val timestamp: Long,
    val levelName: String, // DEBUG, INFO, WARN, ERROR
    val channelName: String, // APP, DB, SYNC, CRASH
    val tag: String,
    val message: String,
    val traceId: String? = null,
    val stackTrace: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInspectorSheet(
    logs: List<LogEntryUi>,
    onClearLogs: () -> Unit,
    onExportLogs: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedChannel by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredLogs = logs.filter { entry ->
        val channelMatch = selectedChannel == null || entry.channelName == selectedChannel
        val queryMatch = searchQuery.isBlank() ||
                entry.message.contains(searchQuery, ignoreCase = true) ||
                entry.tag.contains(searchQuery, ignoreCase = true) ||
                (entry.traceId?.contains(searchQuery, ignoreCase = true) == true)
        channelMatch && queryMatch
    }

    val channels = listOf("APP", "DB", "SYNC", "CRASH")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "APM 性能与日志浮窗",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onClearLogs) {
                        Text("清空", fontSize = 11.sp)
                    }
                    Button(onClick = onExportLogs) {
                        Text("导出", fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Channel Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedChannel == null,
                    onClick = { selectedChannel = null },
                    label = { Text("ALL (${logs.size})", fontSize = 11.sp) }
                )
                channels.forEach { channel ->
                    val count = logs.count { it.channelName == channel }
                    FilterChip(
                        selected = selectedChannel == channel,
                        onClick = { selectedChannel = channel },
                        label = { Text("$channel ($count)", fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search text field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("搜索 TraceId / Tag / 关键词...", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Log List
            if (filteredLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无匹配日志", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredLogs, key = { it.id }) { log ->
                        LogItemRow(log)
                    }
                }
            }
        }
    }
}

@Composable
private fun LogItemRow(log: LogEntryUi) {
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
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "[${log.channelName}]",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "[${log.levelName}]",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = levelColor
                    )
                    Text(
                        text = log.tag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    log.traceId?.let { trace ->
                        Text(
                            text = "[$trace]",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                Text(
                    text = timeStr,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = log.message,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )

            log.stackTrace?.let { stack ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stack,
                    fontSize = 10.sp,
                    color = ExpenseRed,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 6
                )
            }
        }
    }
}
