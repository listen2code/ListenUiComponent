package com.listen.uicomponent.charts

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.listen.uicomponent.theme.AccentColor
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.ThemeMode

/**
 * 独立的环形统计图预览组件 (DonutChartPreview)。
 * 遵守 Rule 3 单文件 250 行红线规范，将预览函数解耦独立。
 */
@Preview(showBackground = true)
@Composable
fun DonutChartPreview() {
    ListenTheme(themeMode = ThemeMode.LIGHT, accentColor = AccentColor.EMERALD) {
        Surface(modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.surface) {
            DonutChart(
                items = listOf(
                    PieChartItem("Food", "#FF5722", 150.0, 0.6f),
                    PieChartItem("Rent", "#4CAF50", 100.0, 0.4f)
                ),
                totalValue = 250.0,
                centerTitle = "Total",
                centerValueText = "$250.00"
            )
        }
    }
}
