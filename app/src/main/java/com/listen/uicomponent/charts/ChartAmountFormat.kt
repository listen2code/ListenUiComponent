package com.listen.uicomponent.charts

import java.util.Locale

/**
 * Common amount formatter for chart components.
 * Strips unneeded trailing zeros and decimal points, ensuring values like 100.00 become "100" and 50.0 become "50".
 */
internal fun Double.formatChartAmount(): String {
    if (this == 0.0 || this == -0.0) return "0"
    val str = String.format(Locale.US, "%.2f", this)
    return when {
        str.endsWith(".00") -> str.removeSuffix(".00")
        str.endsWith(".0") -> str.removeSuffix(".0")
        str.contains(".") -> str.trimEnd('0').trimEnd('.')
        else -> str
    }
}

/**
 * Common percentage formatter for chart components.
 * Strips unneeded trailing zeros and decimal points, ensuring values like 100.0% become "100%" and 50.0% become "50%".
 */
internal fun Float.formatPercentage(): String = this.toDouble().formatPercentage()

internal fun Double.formatPercentage(): String {
    if (this == 0.0 || this == -0.0) return "0"
    val str = String.format(Locale.US, "%.1f", this)
    return when {
        str.endsWith(".00") -> str.removeSuffix(".00")
        str.endsWith(".0") -> str.removeSuffix(".0")
        str.contains(".") -> str.trimEnd('0').trimEnd('.')
        else -> str
    }
}

