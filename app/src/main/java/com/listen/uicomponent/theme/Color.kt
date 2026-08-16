package com.listen.uicomponent.theme

import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkSurfaceVariant = Color(0xFF2C2C2C)
val DarkOnBackground = Color(0xFFE0E0E0)
val DarkOnSurface = Color(0xFFF5F5F5)

val LightBackground = Color(0xFFF8F9FA)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F3F5)
val LightOnBackground = Color(0xFF212529)
val LightOnSurface = Color(0xFF1A1A1A)

val IncomeGreen = Color(0xFF10B981)
val ExpenseRed = Color(0xFFEF4444)

enum class AccentColor(val colorHex: String, val nameZh: String, val nameEn: String, val nameJa: String) {
    EMERALD("#10B981", "翡翠绿", "Emerald", "エメラルド"),
    OCEAN_BLUE("#3B82F6", "海洋蓝", "Ocean Blue", "オーシャンブルー"),
    SUNSET_ORANGE("#F97316", "日落橙", "Sunset Orange", "サンセットオレンジ"),
    ROYAL_PURPLE("#8B5CF6", "高贵紫", "Royal Purple", "ロイヤルパープル"),
    ROSE("#EC4899", "玫瑰粉", "Rose", "ローズ"),
    AMBER("#F59E0B", "琥珀黄", "Amber", "アンバー")
}

fun parseHexColor(hex: String, fallback: Color = Color(0xFF10B981)): Color {
    return try {
        val cleaned = hex.removePrefix("#")
        val colorInt = cleaned.toLong(16).toInt()
        if (cleaned.length == 6) {
            Color(colorInt or 0xFF000000.toInt())
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        fallback
    }
}
