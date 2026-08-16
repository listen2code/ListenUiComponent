# ListenUiComponent

<p align="center">
  <strong>通用 Android Compose 视觉与 UI 组件库 (Universal Android Compose UI Component & Design System SDK)</strong>
</p>

---

## 📖 简介

`ListenUiComponent` 是 Listen 系列原生 Android 应用的通用视觉设计系统与 Compose UI 组件库。统一基础配色、深浅色模式、强调色调色盘以及可复用的复杂交互控件。

---

## 🌟 核心组件

- **Material 3 主题系统**：动态 Color Token、深浅/AMOLED 色彩模式与 6+ 强调色 (Accent Color Palette)。
- **通用计算器键盘**：`CustomKeypad` 自定义数字计算键盘，支持算术求和与高效键盘交互。
- **Canvas 图表组件库**：`DonutChart` 环形占比图、`BarChart` 收支对比柱状图、`LineChart` 趋势折线图。
- **基础通用控件**：`CommonCard`、`CategoryBadge`、`ItemRow`、交互浮窗与通用按钮。

---

## 📦 使用说明 (Composite Build / Gradle)

在宿主 APP 项目的 `settings.gradle.kts` 中添加工程引用：

```kotlin
// settings.gradle.kts
includeBuild("../ListenUiComponent")
```

在模块 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("com.listen:listen-uicomponent:1.0.0")
}
```

---

## 📄 开源许可

基于 [MIT License](LICENSE) 开源。
