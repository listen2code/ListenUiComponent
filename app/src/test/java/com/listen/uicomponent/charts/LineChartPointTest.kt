package com.listen.uicomponent.charts

import org.junit.Assert.assertEquals
import org.junit.Test

class LineChartPointTest {

    @Test
    fun testLineChartPointCreation() {
        val point = LineChartPoint(label = "1", value = 100.0, subLabel = "08-01")
        assertEquals("1", point.label)
        assertEquals(100.0, point.value, 0.0)
        assertEquals("08-01", point.subLabel)
    }

    @Test
    fun testLineChartPointDefaultSubLabel() {
        val point = LineChartPoint(label = "15", value = 50.0)
        assertEquals("15", point.label)
        assertEquals(50.0, point.value, 0.0)
        assertEquals("", point.subLabel)
    }
}
