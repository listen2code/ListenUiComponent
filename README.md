# ListenUiComponent

<p align="center">
  <strong>通用 Android Compose 视觉与 UI 组件库 (Universal Android Compose UI Component & Design System SDK)</strong>
</p>

---

## 📖 简介

`ListenUiComponent` 是 Listen 系列原生 Android 应用的**跨项目通用视觉设计系统与 UI 组件库**。彻底剥离了特定的领域/业务逻辑，统一应用基础配色、深浅色/纯黑模式、6+ 动态 Accent 强调色调色盘，以及独立成类的基础通用控件。

---

## 🌟 核心模块结构 (`com.listen.uicomponent`)

- **`theme/`**：
  - `Color.kt`：基础色彩 Token、深浅/AMOLED 色彩模式与 6+ 强调色 (`EMERALD`, `OCEAN_BLUE`, `SUNSET_ORANGE`, `ROYAL_PURPLE`, `ROSE`, `AMBER`)。
  - `Theme.kt`：`ListenTheme` Material 3 动态 Theme 包装器。
- **`keypad/`**：
  - `NumericKeypad.kt`：通用数字/计算器键盘组件（支持通用数字按键、退格、确定以及可选算术运算符）。
- **`charts/`**：
  - `DonutChart.kt`：通用 Canvas 环形占比图。
- **`components/`**（每个组件独立成类）：
  - `SurfaceCard.kt`：规范化圆角阴影 Surface 卡片。
  - `IconBadge.kt`：通用图标与彩色背景徽章。
  - `EmptyStateView.kt`：通用空状态图文。
  - `LoadingView.kt`：居中加载指示器。

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
