# ListenUiComponent

<p align="center">
  <strong>通用 Android Compose 视觉与 UI 组件库 (Universal Android Compose UI Component & Design System SDK)</strong>
</p>

---

## 📖 简介

`ListenUiComponent` 是 Listen 系列原生 Android 应用的**跨项目通用视觉设计系统与 UI 组件库**。彻底剥离了特定的领域/业务逻辑，统一应用基础配色、深浅色/纯黑模式、6+ 动态 Accent 强调色调色盘，以及独立成类的基础通用控件与 Canvas 图表系统。

---

## 🌟 核心模块结构 (`com.listen.uicomponent`)

- **`theme/`**：
  - `Color.kt`：基础色彩 Token、收支红绿标准色与 6 种动态强调色 (`EMERALD`, `SAPPHIRE`, `AMBER`, `ROSE`, `VIOLET`, `SLATE`)。
  - `Theme.kt`：`ListenTheme` Material 3 动态 Theme 包装器。
- **`keypad/`**：
  - `NumericKeypad.kt`：通用数字键盘组件（支持算术运算符、退格、最大金额安全边界与醒目“完成记账 ✓”按钮）。
- **`charts/`**：
  - `DonutChart.kt`：通用 Canvas 环形占比图（自适应中心指标展示）。
  - `BarChart.kt`：通用 Canvas 垂直柱状走势图。
- **`components/`**：
  - `SurfaceCard.kt`：规范化圆角卡片容器，支持精准 `cornerRadius` 与 `contentPadding` 几何对齐。
  - `SegmentedProgressBar.kt`：通用多色分段比例条。
  - `SearchBarInput.kt`：通用圆角搜索输入框。
  - `IconBadge.kt`：通用图标与彩色背景徽章。
  - `EmptyStateView.kt`：通用空状态图文提示。
  - `LoadingView.kt`：居中加载指示器。
- **`apm/`**：
  - `LogInspectorSheet.kt`：APM 实时日志浮窗（支持水平滑动 Chip 过滤、文本一键导出与对齐按钮）。

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
    implementation("com.listen:listen-uicomponent")
}
```

---

## 📄 开源许可

基于 [MIT License](LICENSE) 开源。
