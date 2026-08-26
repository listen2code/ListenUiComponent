package com.listen.uicomponent.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorTest {

    @Test
    fun testParseHexColor_valid6CharHex() {
        val expected = Color(0xFFFF0000)
        val result = parseHexColor("#FF0000")
        assertEquals(expected, result)
    }

    @Test
    fun testParseHexColor_valid8CharHex() {
        val expected = Color(0x800000FF)
        val result = parseHexColor("#800000FF")
        assertEquals(expected, result)
    }

    @Test
    fun testParseHexColor_withoutHash() {
        val expected = Color(0xFF00FF00)
        val result = parseHexColor("00FF00")
        assertEquals(expected, result)
    }

    @Test
    fun testParseHexColor_invalidHex_returnsFallback() {
        val fallback = Color(0xFF10B981) // default fallback
        val result = parseHexColor("invalid_hex")
        assertEquals(fallback, result)
    }

    @Test
    fun testParseHexColor_invalidLength_returnsFallback() {
        val fallback = Color(0xFF123456)
        val result = parseHexColor("#123", fallback = fallback)
        assertEquals(fallback, result)
    }

    @Test
    fun testParseHexColor_emptyString_returnsFallback() {
        val fallback = Color(0xFF10B981)
        val result = parseHexColor("")
        assertEquals(fallback, result)
    }

    @Test
    fun testColorConstants() {
        assertEquals(Color(0xFF121212), DarkBackground)
        assertEquals(Color(0xFF1E1E1E), DarkSurface)
        assertEquals(Color(0xFF2C2C2C), DarkSurfaceVariant)
        assertEquals(Color(0xFFE0E0E0), DarkOnBackground)
        assertEquals(Color(0xFFF5F5F5), DarkOnSurface)

        assertEquals(Color(0xFFF8F9FA), LightBackground)
        assertEquals(Color(0xFFFFFFFF), LightSurface)
        assertEquals(Color(0xFFF1F3F5), LightSurfaceVariant)
        assertEquals(Color(0xFF212529), LightOnBackground)
        assertEquals(Color(0xFF1A1A1A), LightOnSurface)

        assertEquals(Color(0xFF10B981), IncomeGreen)
        assertEquals(Color(0xFFEF4444), ExpenseRed)
    }
}
