package com.listen.uicomponent.components

import androidx.compose.ui.tooling.preview.Preview
import com.listen.uicomponent.theme.ListenTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding

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

    // 使用纯函数进行小数位数校验与规范化，解决多小数点、超出限制及国际键盘逗号兼容问题
    val handleValueChange: (String) -> Unit = { newText ->
        val sanitized = filterDecimalInput(newText, maxDecimalPlaces)
        if (sanitized != null) {
            onValueChange(sanitized)
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

/**
 * 校验并规范化带小数点的数值输入（原因：支持灵活配置最大允许小数位数，如最多 2 位、3 位，解决越界输入与国际键盘符号兼容问题）。
 *
 * @param input 用户最新输入的字符串
 * @param maxDecimalPlaces 允许的最大小数位数（如 2、3；为 0 表示仅整数；为 null 表示不限制小数位数）
 * @return 过滤规范化后的字符串；若输入格式非法（多小数点、超出限制）则返回 null 以拦截本次输入
 */
fun filterDecimalInput(input: String, maxDecimalPlaces: Int?): String? {
    if (maxDecimalPlaces == null) return input
    if (maxDecimalPlaces < 0) return null

    // 兼容国际软键盘（部分语言使用逗号作为小数点）
    val normalized = input.replace(',', '.')
    val filtered = normalized.filter { it.isDigit() || it == '.' }
    if (filtered.count { it == '.' } > 1) return null

    // 用户直接输入小数点时自动补全前导 0（例如 "." 自动修正为 "0."）
    val formatted = if (filtered.startsWith(".")) "0$filtered" else filtered
    val dotIndex = formatted.indexOf('.')

    val isValid = when {
        dotIndex == -1 -> true
        maxDecimalPlaces == 0 -> false
        else -> formatted.length - 1 - dotIndex <= maxDecimalPlaces
    }
    return if (isValid) formatted else null
}

@Preview(showBackground = true)
@Composable
fun CommonEditTextPreview() {
    ListenTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CommonEditText(value = "99.99", onValueChange = {}, placeholder = "Max 2 decimals", maxDecimalPlaces = 2)
            CommonEditText(value = "3.141", onValueChange = {}, placeholder = "Max 3 decimals", maxDecimalPlaces = 3)
            CommonEditText(value = "", onValueChange = {}, placeholder = "Empty with error", errorMessage = "Invalid input")
        }
    }
}
