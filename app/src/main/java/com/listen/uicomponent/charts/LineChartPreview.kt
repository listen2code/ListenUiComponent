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
 * 独立的平滑折线图预览组件 (LineChartPreview)。
 * 遵守 Rule 3 单文件 250 行红线规范，将预览函数解耦独立。
 */
@Preview(showBackground = true)
@Composable
fun LineChartPreview() {
    ListenTheme(themeMode = ThemeMode.LIGHT, accentColor = AccentColor.OCEAN_BLUE) {
        Surface(modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.surface) {
            LineChart(
                points = listOf(
                    LineChartPoint(label = "1", value = 50.0, subLabel = "09-01"),
                    LineChartPoint(label = "5", value = 120.0, subLabel = "09-05"),
                    LineChartPoint(label = "10", value = 300.0, subLabel = "09-10"),
                    LineChartPoint(label = "15", value = 80.0, subLabel = "09-15"),
                    LineChartPoint(label = "20", value = 220.0, subLabel = "09-20"),
                    LineChartPoint(label = "25", value = 160.0, subLabel = "09-25"),
                    LineChartPoint(label = "30", value = 95.0, subLabel = "09-30")
                ),
                currencySymbol = "￥"
            )
        }
    }
}
