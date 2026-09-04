package com.listen.uicomponent.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Universal outlined text field component for user input across Listen applications.
 *
 * @param value Current input text
 * @param onValueChange Value change callback
 * @param modifier Composable modifier
 * @param label Optional field label
 * @param placeholder Optional placeholder text
 * @param leadingIcon Optional leading icon
 * @param trailingIcon Optional custom trailing icon
 * @param showClearButton Whether to show a clear button when text is non-empty
 * @param isError Whether the input is in error state
 * @param errorMessage Optional error message displayed below
 * @param singleLine Whether the field is restricted to single line
 * @param maxLines Maximum lines if not singleLine
 * @param keyboardOptions Keyboard options
 * @param keyboardActions Keyboard actions
 * @param visualTransformation Visual transformation (e.g. password masking)
 * @param cornerRadius Field corner radius
 * @param maxDecimalPlaces Maximum decimal places allowed (defaults to 2 if keyboardType is Decimal, otherwise null). Set explicitly to customize or null to disable.
 */
@Composable
fun CommonEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    showClearButton: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 4,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cornerRadius: Dp = 12.dp,
    maxDecimalPlaces: Int? = if (keyboardOptions.keyboardType == KeyboardType.Decimal) 2 else null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val unifiedTextStyle = LocalTextStyle.current.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    val handleValueChange: (String) -> Unit = { newText ->
        if (maxDecimalPlaces != null) {
            val filtered = newText.filter { it.isDigit() || it == '.' }
            val dotCount = filtered.count { it == '.' }
            if (dotCount <= 1) {
                val dotIndex = filtered.indexOf('.')
                val isValidDecimals = if (dotIndex == -1) {
                    true
                } else if (maxDecimalPlaces == 0) {
                    false
                } else {
                    filtered.length - 1 - dotIndex <= maxDecimalPlaces
                }
                if (isValidDecimals) {
                    onValueChange(filtered)
                }
            }
        } else {
            onValueChange(newText)
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = handleValueChange,
        readOnly = readOnly,
        textStyle = unifiedTextStyle,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (singleLine && (!isError || errorMessage == null)) {
                    if (label != null) Modifier.heightIn(min = 64.dp) else Modifier.height(56.dp)
                } else {
                    Modifier.heightIn(min = 56.dp)
                }
            )
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (readOnly && focusState.isFocused) {
                    keyboardController?.hide()
                }
            },
        label = label?.let { { Text(it, fontSize = 13.sp) } },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = unifiedTextStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                )
            }
        },
        leadingIcon = leadingIcon?.let { icon ->
            {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        },
        trailingIcon = if (trailingIcon != null || showClearButton) {
            {
                if (trailingIcon != null) {
                    trailingIcon()
                } else if (showClearButton) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (value.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onValueChange("")
                                    focusRequester.requestFocus()
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else null,
        isError = isError,
        supportingText = if (isError && errorMessage != null) {
            { Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 11.sp) }
        } else null,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(cornerRadius),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
        )
    )
}
