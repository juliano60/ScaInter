package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictiveTextField(
    value: String,
    placeholder: String,
    suggestions: List<String>,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val filteredSuggestions = if (value.isNotBlank()) {
        val startsWith = suggestions
            .filter {
                value.isNotBlank() && it.startsWith(value, ignoreCase = true)
            }
            .take(10)

        val remaining = 10 - startsWith.size

        startsWith + suggestions
            .filter {
                !it.startsWith(value, ignoreCase = true) &&
                        it.contains(value, ignoreCase = true)
            }
            .take(remaining)
    } else {
        emptyList()
    }

    ExposedDropdownMenuBox(
        expanded = expanded && filteredSuggestions.isNotEmpty(),
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        PrimaryOutlinedTextField(
            value = value,
            placeholder = placeholder,
            onValueChanged = {
                onValueChanged(it)
                expanded = true
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredSuggestions.isNotEmpty(),
            onDismissRequest = { expanded = false }
        ) {
            filteredSuggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion) },
                    onClick = {
                        onValueChanged(suggestion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun PredictiveTextFieldPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            PredictiveTextField(
                value = "",
                placeholder = "Placeholder",
                suggestions = listOf("Suggestion 1", "Suggestion 2", "Suggestion 3"),
                onValueChanged = {}
            )
        }
    }
}