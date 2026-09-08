package com.listen.uicomponent.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
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

    @Test
    fun testFindSegmentAtXWithMultipleSegments() {
        val segA = ProgressSegment(colorHex = "#EF4444", percentage = 0.5f)
        val segB = ProgressSegment(colorHex = "#3B82F6", percentage = 0.3f)
        val segC = ProgressSegment(colorHex = "#10B981", percentage = 0.2f)
        val segments = listOf(segA, segB, segC)
        val totalWidth = 1000f

        // 命中 SegA 区域 (0 ~ 500px)
        assertSame(segA, findSegmentAtX(segments, x = 0f, totalWidth = totalWidth))
        assertSame(segA, findSegmentAtX(segments, x = 250f, totalWidth = totalWidth))
        assertSame(segA, findSegmentAtX(segments, x = 500f, totalWidth = totalWidth))

        // 命中 SegB 区域 (500 ~ 800px)
        assertSame(segB, findSegmentAtX(segments, x = 501f, totalWidth = totalWidth))
        assertSame(segB, findSegmentAtX(segments, x = 650f, totalWidth = totalWidth))
        assertSame(segB, findSegmentAtX(segments, x = 800f, totalWidth = totalWidth))

        // 命中 SegC 区域 (800 ~ 1000px)
        assertSame(segC, findSegmentAtX(segments, x = 801f, totalWidth = totalWidth))
        assertSame(segC, findSegmentAtX(segments, x = 999f, totalWidth = totalWidth))
        assertSame(segC, findSegmentAtX(segments, x = 1000f, totalWidth = totalWidth))
    }

    @Test
    fun testFindSegmentAtXBoundariesAndClamping() {
        val segA = ProgressSegment(colorHex = "#EF4444", percentage = 0.6f)
        val segB = ProgressSegment(colorHex = "#3B82F6", percentage = 0.4f)
        val segments = listOf(segA, segB)

        // 负坐标 Clamp 到首个分段
        assertSame(segA, findSegmentAtX(segments, x = -100f, totalWidth = 500f))

        // 超出宽度 Clamp 到末尾分段
        assertSame(segB, findSegmentAtX(segments, x = 800f, totalWidth = 500f))
    }

    @Test
    fun testFindSegmentAtXEmptyAndInvalidCases() {
        // 空列表
        assertNull(findSegmentAtX(emptyList(), x = 100f, totalWidth = 500f))

        // 零宽度
        val seg = ProgressSegment(colorHex = "#EF4444", percentage = 1.0f)
        assertNull(findSegmentAtX(listOf(seg), x = 100f, totalWidth = 0f))
        assertNull(findSegmentAtX(listOf(seg), x = 100f, totalWidth = -10f))

        // 过滤掉 percentage <= 0 的项
        val zeroSeg = ProgressSegment(colorHex = "#000000", percentage = 0f)
        assertNull(findSegmentAtX(listOf(zeroSeg), x = 50f, totalWidth = 100f))
    }
}
