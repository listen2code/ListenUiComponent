package com.listen.uicomponent.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.PureBlackBackground

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    contentPadding: Dp = 12.dp,
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    val isPureBlack = MaterialTheme.colorScheme.background == PureBlackBackground
    val cardBorder = border ?: if (isPureBlack) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null

    Card(
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = cardBorder,
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SurfaceCardPreview() {
    ListenTheme {
        SurfaceCard {
            CommonText(text = "Card Content")
        }
    }
}
