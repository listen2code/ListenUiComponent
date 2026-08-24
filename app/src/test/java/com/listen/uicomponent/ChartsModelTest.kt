package com.listen.uicomponent

import com.listen.uicomponent.charts.BarChartItem
import com.listen.uicomponent.charts.PieChartItem
import com.listen.uicomponent.components.ProgressSegment
import org.junit.Assert.assertEquals
import org.junit.Test

class ChartsModelTest {

    @Test
    fun testPieChartItemData() {
        val item = PieChartItem(
            label = "餐饮",
            colorHex = "#EF4444",
            value = 250.0,
            percentage = 0.5f
        )
        assertEquals("餐饮", item.label)
        assertEquals("#EF4444", item.colorHex)
        assertEquals(250.0, item.value, 0.001)
        assertEquals(0.5f, item.percentage)
    }

    @Test
    fun testBarChartItemData() {
        val item = BarChartItem(
            label = "08-18",
            value = 120.5,
            colorHex = "#3B82F6"
        )
        assertEquals("08-18", item.label)
        assertEquals(120.5, item.value, 0.001)
        assertEquals("#3B82F6", item.colorHex)
    }

    @Test
    fun testProgressSegmentData() {
        val segment = ProgressSegment(
            colorHex = "#10B981",
            percentage = 0.35f
        )
        assertEquals("#10B981", segment.colorHex)
        assertEquals(0.35f, segment.percentage)
    }

    @Test
    fun testLineChartPointData() {
        val point = com.listen.uicomponent.charts.LineChartPoint(
            label = "15",
            value = 350.0,
            subLabel = "08-15"
        )
        assertEquals("15", point.label)
        assertEquals(350.0, point.value, 0.001)
        assertEquals("08-15", point.subLabel)
    }
}
