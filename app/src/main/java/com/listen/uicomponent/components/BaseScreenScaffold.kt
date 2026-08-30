package com.listen.uicomponent.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight

/**
 * 通用基础屏幕脚手架 (Base Screen Scaffold)。
 * 采用 Compose 推荐的 Slot API (插槽 API) 设计模式，取代传统的 BaseActivity 继承。
 *
 * 【教学点】：
 * 1. Composition over Inheritance：通过将 UI 块作为参数传递，实现极高的灵活性。
 * 2. WindowInsets 管理：自动处理状态栏和导航栏规避，实现 Edge-to-Edge 沉浸式效果。
 * 3. NestedScroll：通过 scrollBehavior 将手势事件从内容区域转发给 TopAppBar，实现自动折叠。
 *
 * @param modifier 外部传入的修饰符，遵循“第一个可选参数”原则
 * @param title 屏幕标题文字（若提供 titleSlot 则此项失效）
 * @param titleSlot 自定义标题插槽，用于放置复杂的标题 UI（如日期切换器）
 * @param navigationIcon 左侧导航图标插槽（通常放置 Back 按键）
 * @param actions 右侧动作按钮组插槽
 * @param floatingActionButton 悬浮按钮插槽
 * @param bottomBar 底部栏插槽（新增：用于放置 BottomAppBar 或固定操作条）
 * @param snackbarHost 消息条宿主插槽
 * @param scrollBehavior 顶栏滚动行为（新增：配合 Modifier.nestedScroll 实现联动效果）
 * @param containerColor 屏幕背景色，默认使用主题背景色
 * @param content 核心内容区域，接收由 Scaffold 计算出的 PaddingValues（含顶栏/底栏高度占位）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreenScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    titleSlot: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHost: (@Composable () -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            // nestedScroll 用于建立滚动链接。
            // 当 content 中的列表滚动时，滚动偏移量会同步给 scrollBehavior，
            // 从而驱动 TopAppBar 实现诸如高度升降、颜色渐变或完全折叠的视觉动画。
            .then(
                if (scrollBehavior != null)
                    Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                else Modifier
            ),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (titleSlot != null) {
                        titleSlot()
                    } else if (title.isNotBlank()) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = { navigationIcon?.invoke() },
                actions = actions,
                scrollBehavior = scrollBehavior,
                // 配置顶栏颜色，ScrolledContainerColor 用于在内容滚动到顶栏下方时显示的颜色
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                // 顶栏自身不处理状态栏 Padding，由外层或 Scaffold 统一调度
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
        floatingActionButton = { floatingActionButton?.invoke() },
        bottomBar = { bottomBar?.invoke() },
        snackbarHost = { snackbarHost?.invoke() },
        // 默认情况下 Scaffold 会自动处理 WindowInsets 并通过 PaddingValues 传递给 content。
        // 若此处设置为 0，则意味着 content 需要自己负责避开状态栏/导航栏（例如使用 Modifier.systemBarsPadding()）。
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = containerColor
    ) { paddingValues ->
        // paddingValues 包含了 TopAppBar 和 BottomBar 所占据的空间，防止内容被遮挡。
        content(paddingValues)
    }
}