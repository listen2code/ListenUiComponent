# ListenUiComponent

<p align="center">
  <strong>通用 Android Compose 视觉与 UI 组件库 (Universal Android Compose UI Component & Design System SDK)</strong>
</p>

---

## 📖 简介与定位

`ListenUiComponent` 是 Listen 多 App 生态矩阵（记账、资产管理、习惯打卡、备忘录等）的**跨项目通用视觉设计系统与 UI 组件库**。
严格遵循**无业务耦合（Zero Business Coupling）**原则，提供无状态、高度可复用、主题感知的通用 Jetpack Compose 控件、标准化 `Common*` 基础原子组件与 Canvas 绘图系统。

---

## 🌟 核心模块结构 (`com.listen.uicomponent`)

### 1. 通用基础组件套件 (`components/`)

全套件均严格遵循 Compose API 规范（`modifier: Modifier = Modifier` 作为第一个可选参数、支持主题自适应、无魔法数值）：

| 组件 | 功能定位 | 核心特性 |
| :--- | :--- | :--- |
| **`CommonButton`** | 通用按钮 | 支持 Primary, Secondary, Tonal, Outlined, Danger, Text 六大风格；内置单行防折行文本自适应缩放 (`AutoResizeText`)；支持前置图标与点击水波纹。 |
| **`CommonDialog`** | 通用对话框 | 统一 16.dp 弹窗圆角、标题字阶、Elevation、图标插槽与确认/取消按钮插槽。 |
| **`CommonEditText`** | 通用输入框 | 统一 12.dp 浅底圆角、内置一键清空 (`ClearButton`)、错误高亮提示态与键盘选项；支持 `readOnly` 模式（自动锁焦抑制软键盘）与 56.dp 固定高度约束。 |
| **`CommonEmpty`** | 通用空状态 | 统一缺省图文排版、预设 200.dp 占位高度与可插拔动作按钮。 |
| **`CommonLoading`** | 通用加载器 | 统一居中菊花加载指示器、尺寸配置与状态文本。 |
| **`CommonText`** | 通用文本 | 统一主题配色、单行溢出自适应缩放 (`autoResize`) 与字阶规范。 |
| **`CommonSnackbar`** | 通用浮动提示 | 悬浮胶囊 Toast，支持 Success, Error, Warning, Info 四大语义、平滑升降动画与 Action 按钮。 |
| **`CommonBanner`** | 顶部通知横幅 | 支持 Info, Warning, Error, Success 状态、展开收起动画、操作按钮与关闭叉号。 |
| **`CommonBadge`** | 状态徽标与标签 | 支持 Small / Medium 尺寸、状态圆点 (`showDot`)、图标插槽与 6 种视觉风格。 |
| **`CommonListItem`** | 通用列表项 / Cell | 统一包含左侧图标插槽、主标题、副标题、右侧自定义内容或 Chevron 箭头、点击波纹与下分割线。 |
| **`CommonSwitchRow`** | 开关交互行 | 点击整行即可触发 Switch 开关切换，支持前置图标、副标题与下分割线。 |
| **`CommonSegmentedControl`**| 分段选择胶囊 | iOS 风格平滑弹性滑块动画指示器，支持纯文本与多段等分切换。 |
| **`CommonBottomSheet`** | 统一底部抽屉 | 统一 24.dp 顶部圆角、标准 Header 标题与确认按钮、自动集成软键盘与导航栏避让 (`imePadding`, `navigationBarsPadding`)。 |
| **`CommonSkeleton`** | 骨架屏扫光动效 | 提供高流畅度 `Modifier.shimmer()` 微光扩散 Modifier、`CommonSkeletonBox` 与预制列表骨架行 `CommonSkeletonRow`。 |
| **`CommonError`** | 异常与重试缺省页 | 统一网络/加载异常图文、居中排版与“点击重试”操作按钮。 |
| **`CommonList`** | 多状态列表容器 | 自动集成 Loading (骨架屏) -> Empty -> Error -> Content 四态切换与 LazyColumn 渲染。 |
| **`CommonDivider`** | 极细分割线 | 统一 0.5.dp 细线条、透明度与左右自定义缩进 (`startIndent`, `endIndent`)。 |
| **`BaseScreenScaffold`** | 通用页面脚手架 | 统一标准顶部栏 (TopBar)、内容区域与底部导航栏布局容器，内置系统栏适配、`scrollBehavior` 嵌套滚动联动（TopAppBar 滑动折叠/展开）与标准化页面结构。 |
| **`EmptyStateView`** | 增强空状态视图 | 基于 `CommonEmpty` 的增强版空状态组件，提供更丰富的自定义插槽与动画支持。 |
| **`IconBadge`** | 图标徽标 | 带状态圆点或数字角标的图标组件，适用于通知、消息未读等场景。 |
| **`LoadingView`** | 增强加载视图 | 基于 `CommonLoading` 的增强版加载组件，提供更多样化的加载状态展示。 |
| **`SearchBarInput`** | 通用搜索输入框 | 标准搜索栏组件，内置搜索图标、输入框与清空按钮，支持查询回调与键盘交互。 |
| **`SegmentedProgressBar`** | 分段比例进度条 | 多段式彩色进度条，支持自定义段数、颜色与比例，适用于分类占比与预算进度展示；支持 `onSegmentClick` 交互回调与双向联动高亮。 |
| **`SurfaceCard`** | 通用卡片容器 | 统一 Surface 卡片包装器，支持精准 `cornerRadius` 与 `contentPadding` 几何对齐。 |

---

### 2. 交互键盘与可视化图表

- **`keypad/` (通用交互键盘)**：
  - `NumericKeypad.kt`：可高度定制确认文案 (`doneText`) 的通用数字输入键盘（4x3 标准布局：0-9 数字、小数点与退格），支持可选的「继续记账」双按钮模式 (`onContinuePress` / `continueText`)。
- **`charts/` (通用 Canvas 数据可视化图表)**：
  - `DonutChart.kt`：通用 Canvas 环形占比图（支持中空指标文本、动态扇区扫掠动效、交互扇区点击高亮与外部双向联动，`onTooltipClick` 悬浮气泡点击回调）。
  - `DonutChartTooltip.kt`：环形图动态悬浮气泡框（按圆弧中心法向自动计算锚点弹出）。
  - `BarChart.kt`：通用 Canvas 垂直柱状走势图（X 轴防遮挡对齐、极值标注与负荷高亮）。
  - `LineChart.kt`：通用 Canvas 折线平滑走势图（长按拖拽手势、竖直引导虚线、高亮点、峰值联动、可定制 `currencySymbol` / `maxLabel` / `totalLabel` 标签与 `onTooltipClick` 悬浮气泡点击回调）。
  - `LineChartHeader.kt`：折线图顶部摘要行（总计/峰值/均值金额标签），与 `LineChart` 配套使用。
  - `LineChartTooltip.kt`：折线图拖拽数据悬浮气泡框（展示日期、金额与交互跳转指引）。
  - `LineChartXAxisLabels.kt`：X 轴时间刻度渲染组件（基于自定义 Compose `Layout` 实现 Canvas 坐标像素级对齐与密集刻度展示）。

---

### 3. 设计系统与诊断面板

- **`theme/` (设计系统与色彩体系)**：
  - `Color.kt`：基础色彩 Token、通用状态色 (`ExpenseRed`, `IncomeGreen`) 与 6 种动态强调色 (`EMERALD`, `OCEAN_BLUE`, `SUNSET_ORANGE`, `ROYAL_PURPLE`, `ROSE`, `AMBER`)。
  - `Theme.kt`：`ListenTheme` Material 3 动态 Theme 包装器。
- **`apm/` (运行时诊断面板)**：
  - `LogInspectorSheet.kt`：基于通用 `LogEntryUi` 的 APM 实时日志诊断浮窗（支持多频道 Chip 过滤、文本搜索与一键导出）。
  - `LogInspectorComponents.kt`：日志条目卡片、频道过滤 Chip 行与搜索栏等 APM 浮窗子组件集合。

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
