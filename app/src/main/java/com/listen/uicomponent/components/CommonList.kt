package com.listen.uicomponent.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Universal multi-state list container managing Loading, Empty, Error, and Content presentation.
 *
 * @param modifier Composable modifier (first optional parameter)
 * @param isLoading Whether data is currently loading
 * @param isError Whether an error occurred
 * @param isEmpty Whether the dataset is empty
 * @param state LazyListState for scroll control
 * @param errorMessage Error message string when isError is true
 * @param emptyMessage Description when isEmpty is true
 * @param emptyIcon Empty state vector icon
 * @param onRetry Retry callback when in error state
 * @param contentPadding Outer list padding
 * @param verticalArrangement Spacing arrangement between items
 * @param skeletonCount Number of skeleton shimmer rows to show when isLoading is true
 * @param content LazyList content builder
 */
@Composable
fun CommonList(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isError: Boolean = false,
    isEmpty: Boolean = false,
    state: LazyListState = rememberLazyListState(),
    errorMessage: String? = null,
    emptyMessage: String = "No Data Available",
    emptyIcon: ImageVector = Icons.Default.Info,
    onRetry: (() -> Unit)? = null,
    contentPadding: Dp = 0.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    skeletonCount: Int = 5,
    content: LazyListScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(contentPadding)
                ) {
                    items(skeletonCount) {
                        CommonSkeletonRow()
                    }
                }
            }
            isError -> {
                CommonError(
                    message = errorMessage,
                    onRetry = onRetry,
                    modifier = Modifier.padding(16.dp)
                )
            }
            isEmpty -> {
                CommonEmpty(
                    message = emptyMessage,
                    icon = emptyIcon,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(contentPadding),
                    verticalArrangement = verticalArrangement,
                    content = content
                )
            }
        }
    }
}
