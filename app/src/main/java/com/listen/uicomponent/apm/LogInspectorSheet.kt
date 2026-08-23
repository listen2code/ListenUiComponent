package com.listen.uicomponent.apm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

/**
 * Universal APM Log Inspector Modal Bottom Sheet Component.
 * Supports channel filtering, keyword searching, log clearing and exporting.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInspectorSheet(
    logs: List<LogEntryUi>,
    onClearLogs: () -> Unit,
    onExportLogs: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    lang: String = "zh"
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

    val (titleText, clearText, exportText, placeholderText, emptyText) = when (lang.lowercase()) {
        "en" -> Tuple5("APM Logs & Observability", "Clear", "Export", "Search TraceId / Tag / Keyword...", "No matching logs found")
        "ja" -> Tuple5("APM ログと観測性", "消去", "エクスポート", "TraceId / Tag / キーワード検索...", "一致するログはありません")
        else -> Tuple5("APM 性能与日志", "清空", "导出", "搜索 TraceId / Tag / 关键词...", "暂无匹配日志")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 14.dp, vertical = 2.dp)
        ) {
            // Header Row with title and action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titleText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onClearLogs,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(clearText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    Button(
                        onClick = onExportLogs,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(exportText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Horizontally Scrollable Channel Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    FilterChip(
                        selected = selectedChannel == null,
                        onClick = { selectedChannel = null },
                        label = { Text("ALL (${logs.size})", fontSize = 11.sp) }
                    )
                }
                items(channels) { channel ->
                    val count = logs.count { it.channelName == channel }
                    FilterChip(
                        selected = selectedChannel == channel,
                        onClick = { selectedChannel = channel },
                        label = { Text("$channel ($count)", fontSize = 11.sp) }
                    )
                }
            }

            // Search text field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(placeholderText, fontSize = 11.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Log List
            if (filteredLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emptyText, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredLogs, key = { it.id }) { log ->
                        LogItemRow(log)
                    }
                }
            }
        }
    }
}
