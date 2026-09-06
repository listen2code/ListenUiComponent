package com.listen.uicomponent.components

import androidx.compose.ui.tooling.preview.Preview
import com.listen.uicomponent.theme.ListenTheme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}
@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() {
    ListenTheme {
        LoadingView()
    }
}
