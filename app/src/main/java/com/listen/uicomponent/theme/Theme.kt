package com.listen.uicomponent.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

@Composable
fun ListenTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accentColor: AccentColor = AccentColor.EMERALD,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val primaryColor = parseHexColor(accentColor.colorHex)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = primaryColor.copy(alpha = 0.25f),
            onPrimaryContainer = primaryColor,
            secondary = primaryColor,
            onSecondary = Color.White,
            secondaryContainer = primaryColor.copy(alpha = 0.25f),
            onSecondaryContainer = primaryColor,
            tertiary = primaryColor,
            onTertiary = Color.White,
            tertiaryContainer = primaryColor.copy(alpha = 0.25f),
            onTertiaryContainer = primaryColor,
            background = DarkBackground,
            onBackground = DarkOnBackground,
            surface = DarkSurface,
            onSurface = DarkOnSurface,
            surfaceVariant = DarkSurfaceVariant,
            onSurfaceVariant = DarkOnBackground
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = primaryColor.copy(alpha = 0.15f),
            onPrimaryContainer = primaryColor,
            secondary = primaryColor,
            onSecondary = Color.White,
            secondaryContainer = primaryColor.copy(alpha = 0.15f),
            onSecondaryContainer = primaryColor,
            tertiary = primaryColor,
            onTertiary = Color.White,
            tertiaryContainer = primaryColor.copy(alpha = 0.15f),
            onTertiaryContainer = primaryColor,
            background = LightBackground,
            onBackground = LightOnBackground,
            surface = LightSurface,
            onSurface = LightOnSurface,
            surfaceVariant = LightSurfaceVariant,
            onSurfaceVariant = LightOnBackground
        )
    }

    // [Bugfix] 解决浅色主题下状态栏与背景“白底白字”不可见的问题：
    // 原因分析：应用开启 enableEdgeToEdge() 后状态栏背景透明透出浅色内容底色。在浅色主题下若系统未收到 LightStatusBar
    // 指令，状态栏图标和文字仍会保持默认的白色，导致白底白字无法看清（电池、时间、WiFi 消失）。
    // 解决的问题：通过 WindowCompat.getInsetsController 动态对齐状态栏与导航栏图标风格：
    // 浅色主题 (!darkTheme) -> isAppearanceLightStatusBars = true（系统图标渲染为深黑，清晰可见）；
    // 深色主题 (darkTheme) -> isAppearanceLightStatusBars = false（系统图标渲染为白字）。
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = view.context.findActivity()?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Preview(showBackground = true, name = "Theme Light")
@Composable
fun ListenThemeLightPreview() {
    ListenTheme(themeMode = ThemeMode.LIGHT) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Text(text = "ListenTheme Light Mode")
        }
    }
}

@Preview(showBackground = true, name = "Theme Dark")
@Composable
fun ListenThemeDarkPreview() {
    ListenTheme(themeMode = ThemeMode.DARK) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Text(text = "ListenTheme Dark Mode")
        }
    }
}

@Preview(showBackground = true, name = "Theme System")
@Composable
fun ListenThemeSystemPreview() {
    ListenTheme(themeMode = ThemeMode.SYSTEM) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Text(text = "ListenTheme System Mode")
        }
    }
}
