package com.listen.uicomponent.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Unit tests for CommonEditText decimal filtering and validation logic.
 * Verifies max decimal places enforcement (e.g. 2, 3 places), international keyboards,
 * multiple decimal points rejection, and leading dot padding.
 */
class CommonEditTextDecimalFilterTest {

    @Test
    fun testTwoDecimalPlacesLimit() {
        // Integer and empty input
        assertEquals("", filterDecimalInput("", 2))
        assertEquals("100", filterDecimalInput("100", 2))

        // Decimals up to 2 places
        assertEquals("100.", filterDecimalInput("100.", 2))
        assertEquals("100.5", filterDecimalInput("100.5", 2))
        assertEquals("100.55", filterDecimalInput("100.55", 2))

        // Exceeding 2 places should be rejected (returns null)
        assertNull(filterDecimalInput("100.555", 2))
    }

    @Test
    fun testThreeDecimalPlacesLimit() {
        assertEquals("3.", filterDecimalInput("3.", 3))
        assertEquals("3.1", filterDecimalInput("3.1", 3))
        assertEquals("3.14", filterDecimalInput("3.14", 3))
        assertEquals("3.141", filterDecimalInput("3.141", 3))

        // Exceeding 3 places should be rejected
        assertNull(filterDecimalInput("3.1415", 3))
    }

    @Test
    fun testZeroDecimalPlacesOnlyIntegers() {
        assertEquals("123", filterDecimalInput("123", 0))
        // Any decimal point should be rejected
        assertNull(filterDecimalInput("123.", 0))
        assertNull(filterDecimalInput("123.0", 0))
    }

    @Test
    fun testNullDecimalPlacesNoLimit() {
        // When maxDecimalPlaces is null, input is untouched
        assertEquals("123.456789", filterDecimalInput("123.456789", null))
        assertEquals("任意文本", filterDecimalInput("任意文本", null))
    }

    @Test
    fun testMultipleDecimalPointsRejected() {
        assertNull(filterDecimalInput("12.3.4", 2))
        assertNull(filterDecimalInput("..", 2))
    }

    @Test
    fun testLeadingDotPadding() {
        // User clicks '.' directly -> auto padded to "0."
        assertEquals("0.", filterDecimalInput(".", 2))
        assertEquals("0.5", filterDecimalInput(".5", 2))
        assertEquals("0.55", filterDecimalInput(".55", 2))
    }

    @Test
    fun testInternationalCommaSupport() {
        // User with European/international keyboard types comma ','
        assertEquals("0.", filterDecimalInput(",", 2))
        assertEquals("12.5", filterDecimalInput("12,5", 2))
        assertEquals("12.50", filterDecimalInput("12,50", 2))
        assertNull(filterDecimalInput("12,500", 2))
    }
}
