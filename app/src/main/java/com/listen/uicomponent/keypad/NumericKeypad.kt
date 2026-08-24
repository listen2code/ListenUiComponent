package com.listen.uicomponent.keypad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Clean 4x3 Numeric Keypad Component (Option A).
 * Provides large touch targets for numbers 0-9, decimal point, and backspace,
 * paired with a prominent full-width Done/Save action button.
 *
 * @param onKeyPress Callback when a digit or dot is pressed
 * @param onDeletePress Callback when backspace is pressed
 * @param onDonePress Callback when Done/Save is pressed
 * @param modifier Composable modifier (first optional parameter)
 * @param doneText Label for the primary action button
 */
@Composable
fun NumericKeypad(
    onKeyPress: (String) -> Unit,
    onDeletePress: () -> Unit,
    onDonePress: () -> Unit,
    modifier: Modifier = Modifier,
    doneText: String = "OK"
) {
    val numberRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "DEL")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 4x3 Number Grid
        numberRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { key ->
                    KeypadButton(
                        key = key,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (key == "DEL") {
                                onDeletePress()
                            } else {
                                onKeyPress(key)
                            }
                        }
                    )
                }
            }
        }

        // Full-width Primary Done Action Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onDonePress),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = doneText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun KeypadButton(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDelete = key == "DEL"

    val containerColor = if (isDelete) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isDelete) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val labelText = if (isDelete) "⌫" else key

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = labelText,
            fontSize = if (isDelete) 22.sp else 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}
