package com.listen.uicomponent.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 曲线图顶部摘要行 (LineChartHeader)。
 * 展示月度总计指标以及最高支出点标签。
 */
@Composable
internal fun LineChartHeader(
    totalLabel: String?,
    totalExpenseSum: Double,
    maxValue: Double,
    maxLabel: String?,
    currencySymbol: String,
    hideAmount: Boolean,
    primaryColor: Color,
    points: List<LineChartPoint>,
    currentSelectedIndex: Int?,
    onSelectedIndexChange: ((Int?) -> Unit)?,
    onInternalIndexChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(primaryColor, CircleShape)
            )
            val totalStr = if (hideAmount) "••••" else "$currencySymbol${"%.2f".format(totalExpenseSum)}"
            Text(totalLabel ?: totalStr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
        val maxIdx = remember(points) { points.indices.maxByOrNull { points[it].value } }
        val maxStr = if (hideAmount) "Max: ••••" else (maxLabel ?: "Max: $currencySymbol${"%.0f".format(maxValue)}")
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(primaryColor.copy(alpha = 0.09f))
                .then(
                    if (maxIdx != null && (points[maxIdx].value > 0 || points.size == 1)) {
                        Modifier.clickable {
                            val target = if (currentSelectedIndex == maxIdx) null else maxIdx
                            if (onSelectedIndexChange != null) onSelectedIndexChange(target) else onInternalIndexChange(target)
                        }
                    } else Modifier
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(maxStr, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
        }
    }
}
