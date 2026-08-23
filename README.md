# ListenUiComponent

<p align="center">
  <strong>通用 Android Compose 视觉与 UI 组件库 (Universal Android Compose UI Component & Design System SDK)</strong>
</p>

---

## 📖 简介与定位

`ListenUiComponent` 是 Listen 多 App 生态矩阵（记账、资产管理、习惯打卡、备忘录等）的**跨项目通用视觉设计系统与 UI 组件库**。
严格遵循**无业务耦合（Zero Business Coupling）**原则，提供无状态、高度可复用、主题感知的通用 Jetpack Compose 控件与 Canvas 绘图系统。

---

## 🌟 核心模块结构 (`com.listen.uicomponent`)

- **`theme/` (设计系统与色彩体系)**：
  - `Color.kt`：基础色彩 Token、通用状态色与 6 种动态强调色 (`EMERALD`, `OCEAN_BLUE`, `SUNSET_ORANGE`, `ROYAL_PURPLE`, `ROSE`, `AMBER`)。
  - `Theme.kt`：`ListenTheme` Material 3 动态 Theme 包装器。
- **`keypad/` (通用交互键盘)**：
  - `NumericKeypad.kt`：可高度定制确认文案 (`doneText`) 的通用数字计算键盘（支持算术运算符、退格与长按清空）。
- **`charts/` (通用 Canvas 数据可视化图表)**：
  - `DonutChart.kt`：通用 Canvas 环形占比图（支持中空指标文本、动态扇区绘制与平滑动画）。
  - `BarChart.kt`：通用 Canvas 垂直柱状走势图（X 轴防遮挡对齐、极值标注与负荷高亮）。
- **`components/` (通用原子与复合组件)**：
  - `SurfaceCard.kt`：规范化圆角卡片容器，统一阴影、边框与内边距几何规范。
  - `SegmentedProgressBar.kt`：通用多色分段比例条。
  - `SearchBarInput.kt`：通用带清除按键的圆角搜索框（默认通用占位符，支持动态配置）。
  - `IconBadge.kt`：通用彩色背景图标徽章。
  - `EmptyStateView.kt`：通用空状态图文提示组件。
  - `LoadingView.kt`：通用居中加载指示器。
- **`apm/` (运行时诊断面板)**：
  - `LogInspectorSheet.kt`：基于通用 `LogEntryUi` 的 APM 实时日志诊断浮窗（支持多频道 Chip 过滤、文本搜索与一键导出）。

---

## 📦 Gradle 引入说明 (Composite Build)

在宿主 App 项目的 `settings.gradle.kts` 中包含模块：

```kotlin
// settings.gradle.kts
includeBuild("../ListenUiComponent")
```

在宿主 App 模块的 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("com.listen:listen-uicomponent")
}
```

---

## 📄 开源许可

基于 [MIT License](LICENSE) 开源。
