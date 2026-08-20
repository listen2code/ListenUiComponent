package com.listen.uicomponent

import androidx.compose.ui.graphics.Color
import com.listen.uicomponent.theme.AccentColor
import com.listen.uicomponent.theme.ThemeMode
import com.listen.uicomponent.theme.parseHexColor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccentColorTest {

    @Test
    fun testAllAccentColorValues() {
        val accents = AccentColor.entries
        assertEquals(6, accents.size)
        assertTrue(accents.contains(AccentColor.EMERALD))
        assertTrue(accents.contains(AccentColor.OCEAN_BLUE))
        assertTrue(accents.contains(AccentColor.SUNSET_ORANGE))
        assertTrue(accents.contains(AccentColor.ROYAL_PURPLE))
        assertTrue(accents.contains(AccentColor.ROSE))
        assertTrue(accents.contains(AccentColor.AMBER))

        accents.forEach {
            assertTrue(it.colorHex.startsWith("#"))
            val color = parseHexColor(it.colorHex)
            assertNotEquals(Color.Unspecified, color)
        }
    }

    @Test
    fun testParseHexColorEdgeCases() {
        // Valid 6 char hex
        val c1 = parseHexColor("#EF4444")
        assertNotEquals(Color.Unspecified, c1)

        // Valid 8 char hex
        val c2 = parseHexColor("#80EF4444")
        assertNotEquals(Color.Unspecified, c2)

        // Invalid fallback
        val c3 = parseHexColor("invalid_color_string", Color.Gray)
        assertEquals(Color.Gray, c3)
    }

    @Test
    fun testThemeModeEnum() {
        val modes = ThemeMode.entries
        assertEquals(3, modes.size)
        assertTrue(modes.contains(ThemeMode.LIGHT))
        assertTrue(modes.contains(ThemeMode.DARK))
        assertTrue(modes.contains(ThemeMode.SYSTEM))
    }
}
