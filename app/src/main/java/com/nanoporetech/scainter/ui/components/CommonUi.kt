package com.nanoporetech.scainter.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.consultation.Prescription
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

@Composable
fun CardHeader(
    title: String,
    iconImg: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = iconImg,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_small))
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = color,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun CardHeaderDrawable(
    title: String,
    iconImg: Painter,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painter = iconImg,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_small))
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
    }
}


data class CardItem(
    val label: String = "",
    val value: String = "",
    val valueColor: Color? = null,
)

@Composable
fun CardBodyTwoLines(
    items: List<CardItem>,
    modifier: Modifier = Modifier,
    indentRight: Boolean = false,
    keyIsBold: Boolean = true,
    firstColumnWeight: Float = 0.4f,
    secondColumnWeight: Float = 0.6f,
) {
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val paddingXSmall = dimensionResource(R.dimen.padding_xsmall)

    Column(
        verticalArrangement = Arrangement.spacedBy(paddingSmall),
        modifier = modifier) {
        for (item in items) {
            if (item.label.isNotBlank()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(paddingXSmall),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = if (keyIsBold) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(firstColumnWeight)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.bodyLarge,
                            color = item.valueColor ?: MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .weight(secondColumnWeight),
                            textAlign = if (indentRight) TextAlign.End else TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardBody(
    items: List<CardItem>,
    modifier: Modifier = Modifier,
    indentRight: Boolean = false,
    keyIsBold: Boolean = true,
    firstColumnWeight: Float = 0.4f,
    secondColumnWeight: Float = 0.6f,
) {
    Column(modifier) {
        for (item in items) {
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                if (item.label.isNotBlank()) {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = if (keyIsBold) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(firstColumnWeight)
                    )
                    Text(
                        text = item.value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = item.valueColor ?: MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .weight(secondColumnWeight),
                        textAlign = if (indentRight) TextAlign.End else TextAlign.Start
                    )
                }
            }
        }
    }
}

@Composable
fun CardRow(
    title: String,
    iconImg: ImageVector,
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = onClickButton
            )
            //.border(1.dp, color = Color.Blue)
    ) {
        Icon(
            imageVector = iconImg,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_small))
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.horizontal_spacing_small)))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier
            .weight(1.0f))

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceContainerLowest,
        )
    }
}

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
    val filteredSuggestions = suggestions
        .filter {
            value.isNotBlank() && it.contains(value, ignoreCase = true)
        }
        .take(10)

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
    enabled: Boolean = true
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
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun PrescriptionRow(
    item: Prescription,
    modifier: Modifier = Modifier,
    onRemovePrescription: (Prescription) -> Unit = {},
    onChangePrescription: (Prescription) -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text("(x${item.quantityIndex + 1})")
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(
            text = item.name,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
            modifier = Modifier
                .weight(0.6f)
        )
        IconButton(
            onClick = {
                onChangePrescription(item)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_prescription_button),
                tint = ScaInterTheme.extendedColors.mainGreen.color
            )
        }
        IconButton(
            onClick = {
                //onRemovePrescription(item)
                showDeleteDialog = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.remove_medication_button),
                tint = ScaInterTheme.extendedColors.mainGreen.color
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(stringResource(R.string.remove_medication_prompt))
            },
            text = {
                Text(
                    stringResource(
                        R.string.confirmation_prompt,
                        item.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemovePrescription(item)
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}

@Composable
fun PolicyHolderInfo(
    imageUrl: String,
    name: String,
    internalId: String,
    subscriberName: String,
    coverPercent: String,
    contractType: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        PolicyHolderAvatar(
            imgUrl = imageUrl,
            modifier = Modifier
            //.border(1.dp, color = Color.Yellow)
        )

        Spacer(modifier.height(dimensionResource(R.dimen.padding_medium)))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (internalId.isNotBlank()) {
                Text(
                    text = internalId,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = stringResource(R.string.coverage_label_and_value, coverPercent),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "$subscriberName ($contractType)",
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun PolicyHolderAvatar(
    imgUrl: String,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(R.dimen.profile_icon_size)),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.profile_image),
            contentScale = ContentScale.Crop,
            loading = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )
            },
            error = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )
            },
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
        )
    }
}

@Composable
fun showAlert(
    @StringRes title: Int,
    @StringRes message: Int,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(title))
        },
        text = {
            Text(stringResource(message))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
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
            val items = listOf(
                Prescription(
                    name = "Medicament 1",
                    quantityIndex = 1,
                    posology = "",
                ),
                Prescription(
                    name = "Medicament 2",
                    quantityIndex = 2,
                    posology = "",
                )
            )

            LazyColumn {
                items(items) { item ->
                    PrescriptionRow(
                        item
                    )
                }
            }

            /*Column(modifier = Modifier
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
            }*/
            /*val items = listOf<CardItem>(
                CardItem(
                    label = "Label 1",
                    value = "Value 1"
                ),
                CardItem(
                    label = "Label 2",
                    value = "Value 2"
                )
            )

            CardBody(
                items = items
            )*/
        }
    }
}
