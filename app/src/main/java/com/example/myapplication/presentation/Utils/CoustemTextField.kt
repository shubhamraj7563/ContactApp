package com.example.myapplication.presentation.utils

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material.Icon

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    leadingIcon : ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    ) {
    OutlinedTextField(
        value = value,
        onValueChange  = onValueChange,
        label = { Text(label) }, // Set the label text
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
            cursorColor = MaterialTheme.colorScheme.primary,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),

        ),
        modifier = modifier,
        singleLine = singleLine,
        leadingIcon = leadingIcon?.let {
            {
                Icon(imageVector = leadingIcon, contentDescription = null)
            }

        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions

    )
}