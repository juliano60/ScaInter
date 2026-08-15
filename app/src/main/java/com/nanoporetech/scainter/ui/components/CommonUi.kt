package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme


@Composable
fun MainHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = stringResource(R.string.welcome_header, title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}
@Composable
fun SubHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun PrimaryOutlinedTextField(
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        singleLine = true,
        label = {
            Text(placeholder)
        },
        onValueChange = onValueChanged,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = ScaInterTheme.extendedColors.mainGreen.color,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
        ),
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = VisualTransformation.None,
    )
}

@Composable
fun PrimarySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = ScaInterTheme.extendedColors.mainGreen.color,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                checkedBorderColor = ScaInterTheme.extendedColors.mainGreen.color,
                uncheckedBorderColor =MaterialTheme.colorScheme.outlineVariant,
            )
        )
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    iconImg: ImageVector? = null,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ScaInterTheme.extendedColors.mainGreen.color,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        contentPadding = PaddingValues(
            horizontal = paddingMedium,
            vertical = paddingMedium,
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (iconImg != null) {
                Icon(
                    imageVector = iconImg,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_small))
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun CommonUiPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Box(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_medium))
                    ) {
                        PrimaryButton(
                            iconImg = Icons.Filled.AddCircle,
                            text = stringResource(R.string.add_medication_button),
                            enabled = true,
                            modifier = Modifier
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                PrimaryButton(
                    text = stringResource(R.string.confirm_button),
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
