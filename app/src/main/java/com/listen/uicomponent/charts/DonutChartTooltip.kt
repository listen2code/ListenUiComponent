package com.listen.uicomponent.charts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.listen.uicomponent.theme.AccentColor
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.ThemeMode
import com.listen.uicomponent.theme.parseHexColor
import kotlin.math.roundToInt

@Composable
fun DonutChartTooltip(
    item: PieChartItem,
    tapOffset: Offset,
    currencySymbol: String,
    hideAmount: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: ((PieChartItem) -> Unit)? = null
) {
    val density = LocalDensity.current
    Popup(
        alignment = Alignment.Center,
        offset = with(density) {
            val offsetX = (tapOffset.x - 90.dp.toPx()).roundToInt()
            val offsetY = (tapOffset.y - 90.dp.toPx() - 42.dp.toPx()).roundToInt()
            IntOffset(offsetX, offsetY)
        },
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(dismissOnClickOutside = true)
    ) {
        DonutChartTooltipContent(
            item = item,
            currencySymbol = currencySymbol,
            hideAmount = hideAmount,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun DonutChartTooltipContent(
    item: PieChartItem,
    currencySymbol: String,
    hideAmount: Boolean,
    onItemClick: ((PieChartItem) -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .clickable { onItemClick?.invoke(item) }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(parseHexColor(item.colorHex))
            )
            Column {
                Text(
                    text = item.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val amt = if (hideAmount) "••••" else "$currencySymbol${"%.2f".format(item.value)}"
                Text(
                    text = "$amt (${"%.1f".format(item.percentage * 100)}%)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = parseHexColor(item.colorHex)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonutChartTooltipPreview() {
    ListenTheme(themeMode = ThemeMode.LIGHT, accentColor = AccentColor.EMERALD) {
        Box(modifier = Modifier.padding(16.dp)) {
            DonutChartTooltipContent(
                item = PieChartItem("Food", "#FF5722", 150.0, 0.4f),
                currencySymbol = "$",
                hideAmount = false,
                onItemClick = {}
            )
        }
    }
}
