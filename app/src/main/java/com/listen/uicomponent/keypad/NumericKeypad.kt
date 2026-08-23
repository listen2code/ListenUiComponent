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
 * Universal customizable numeric keypad component for numbers, amounts, and calculation inputs.
 */
@Composable
fun NumericKeypad(
    onKeyPress: (String) -> Unit,
    onDeletePress: () -> Unit,
    onDonePress: () -> Unit,
    modifier: Modifier = Modifier,
    doneText: String = "OK",
    showOperators: Boolean = true
) {
    val keys = if (showOperators) {
        listOf(
            listOf("7", "8", "9", "+"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "DEL"),
            listOf(".", "0", "00", "OK")
        )
    } else {
        listOf(
            listOf("7", "8", "9", "DEL"),
            listOf("4", "5", "6", "00"),
            listOf("1", "2", "3", "."),
            listOf("0", "OK")
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { key ->
                    val weight = if (key == "OK" && !showOperators) 3f else 1f
                    KeypadButton(
                        key = key,
                        doneText = doneText,
                        modifier = Modifier.weight(weight),
                        onClick = {
                            when (key) {
                                "DEL" -> onDeletePress()
                                "OK" -> onDonePress()
                                else -> onKeyPress(key)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    key: String,
    doneText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPrimaryAction = key == "OK"
    val isOperator = key == "+" || key == "-"
    val isSpecial = key == "DEL"

    val containerColor = when {
        isPrimaryAction -> MaterialTheme.colorScheme.primary
        isOperator -> MaterialTheme.colorScheme.primaryContainer
        isSpecial -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isPrimaryAction -> MaterialTheme.colorScheme.onPrimary
        isOperator -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    val labelText = when (key) {
        "DEL" -> "⌫"
        "OK" -> doneText
        else -> key
    }

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = labelText,
            fontSize = if (key == "OK") 15.sp else if (key == "DEL") 22.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
