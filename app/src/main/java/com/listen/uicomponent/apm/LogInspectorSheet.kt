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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.listen.uicomponent.theme.ListenTheme

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
    // skipPartiallyExpanded = true: 强制 BottomSheet 直接展开到全屏高度，跳过半展开的中间状态，方便查看长列表
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedChannel by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // 双层过滤机制：
    // 第一层：基于 Chip 的 Channel 维度过滤
    // 第二层：基于关键词的文本搜索，覆盖 message、tag 和 traceId
    val filteredLogs = logs.filter { entry ->
        val channelMatch = selectedChannel == null || entry.channelName == selectedChannel
        val queryMatch = searchQuery.isBlank() ||
                entry.message.contains(searchQuery, ignoreCase = true) ||
                entry.tag.contains(searchQuery, ignoreCase = true) ||
                (entry.traceId?.contains(searchQuery, ignoreCase = true) == true)
        channelMatch && queryMatch
    }

    val channels = listOf("APP", "DB", "SYNC", "CRASH")

    // 利用结构化析构 (Destructuring) 实现轻量级多语言支持。
    // 在不依赖 Android 繁重 string 资源文件的前提下，快速实现界面语言切换
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
                // 屏幕占比设计：最高占据 85% 高度，顶部留白 15% 能够让用户看到底层父页面的上下文
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
                    // 指定唯一的 key，使得 Compose 在列表更新时能够执行高效的差异比对 (diffing)，避免不必要的重组
                    items(filteredLogs, key = { it.id }) { log ->
                        LogItemRow(log)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogInspectorSheetPreview() {
    ListenTheme {
        LogInspectorSheet(
            logs = listOf(
                LogEntryUi(id = "1", timestamp = System.currentTimeMillis(), levelName = "INFO", channelName = "APP", tag = "AppInit", message = "Application started"),
                LogEntryUi(id = "2", timestamp = System.currentTimeMillis(), levelName = "WARN", channelName = "SYNC", tag = "CloudSync", message = "Network latency high"),
                LogEntryUi(id = "3", timestamp = System.currentTimeMillis(), levelName = "ERROR", channelName = "CRASH", tag = "Handler", message = "NullPointerException caught")
            ),
            onClearLogs = {},
            onExportLogs = {},
            onDismiss = {}
        )
    }
}
