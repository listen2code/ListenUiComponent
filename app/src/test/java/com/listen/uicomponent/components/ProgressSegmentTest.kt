package com.listen.uicomponent.components

import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressSegmentTest {

    @Test
    fun testProgressSegmentCreation() {
        val segment = ProgressSegment(colorHex = "#FF0000", percentage = 0.5f)
        assertEquals("#FF0000", segment.colorHex)
        assertEquals(0.5f, segment.percentage, 0.0f)
    }
    
    @Test
    fun testProgressSegmentBoundaryValues() {
        val minSegment = ProgressSegment(colorHex = "#000000", percentage = 0.0f)
        assertEquals(0.0f, minSegment.percentage, 0.0f)

        val maxSegment = ProgressSegment(colorHex = "#FFFFFF", percentage = 1.0f)
        assertEquals(1.0f, maxSegment.percentage, 0.0f)
    }
}
