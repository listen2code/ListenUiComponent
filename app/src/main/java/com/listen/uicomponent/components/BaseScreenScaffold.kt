package com.listen.uicomponent.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Universal Base Screen Scaffold with Slot API for Compose Declarative UI.
 * Replaces traditional inheritance patterns with Composition over Inheritance.
 * Automatically handles TopAppBar slots, status bars insets, FloatingActionButton, and theme backgrounds.
 *
 * @param title Screen title text (ignored if titleSlot is provided)
 * @param titleSlot Optional custom Composable slot for the center title (e.g. date selector or custom header)
 * @param navigationIcon Optional navigation icon Composable (e.g. Back button)
 * @param actions Optional action icons on the right of the TopAppBar
 * @param floatingActionButton Optional floating action button slot
 * @param snackbarHost Optional snackbar host slot
 * @param modifier Composable modifier
 * @param content Screen body content receiving Scaffold PaddingValues
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
    snackbarHost: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = { floatingActionButton?.invoke() },
        snackbarHost = { snackbarHost?.invoke() },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        content(paddingValues)
    }
}
