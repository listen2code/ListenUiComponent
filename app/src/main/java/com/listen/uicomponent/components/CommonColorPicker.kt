package com.listen.uicomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.listen.uicomponent.theme.ListenTheme
import com.listen.uicomponent.theme.parseHexColor

/**
 * Universal color palette swatch picker component.
 * Displays a row of circular color swatches with active selection border and checkmark.
 *
 * @param colors List of hex color strings (e.g. "#10B981")
 * @param selectedColor Currently selected hex color string
 * @param onColorSelected Callback when a color swatch is clicked
 * @param modifier Composable modifier
 * @param circleSize Diameter of each color circle swatch
 * @param checkIconSize Size of the checkmark icon when selected
 * @param selectedBorderWidth Width of the selected ring border
 */
@Composable
fun CommonColorPicker(
    colors: List<String>,
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    circleSize: Dp = 32.dp,
    checkIconSize: Dp = 18.dp,
    selectedBorderWidth: Dp = 3.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEach { hex ->
            val isSelected = hex.equals(selectedColor, ignoreCase = true)
            val color = parseHexColor(hex)

            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(color)
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = selectedBorderWidth,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                        } else Modifier
                    )
                    .clickable { onColorSelected(hex) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(checkIconSize)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonColorPickerPreview() {
    val sampleColors = listOf("#EF4444", "#F59E0B", "#10B981", "#3B82F6", "#8B5CF6", "#EC4899")
    ListenTheme {
        CommonColorPicker(
            colors = sampleColors,
            selectedColor = "#10B981",
            onColorSelected = {}
        )
    }
}
