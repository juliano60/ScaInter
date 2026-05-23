package com.nanoporetech.scainter.ui.components

import android.R.attr.text
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
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
    val value: String = ""
)

@Composable
fun CardBody(
    items: List<CardItem>,
    modifier: Modifier = Modifier,
    indentRight: Boolean = false,
    keyIsBold: Boolean = true,
    firstColumnWeight: Float = 0.4f,
    secondColumnWeight: Float = 0.6f
) {
    Column(modifier) {
        for (item in items) {
            Row() {
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
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            style = MaterialTheme.typography.bodyLarge
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
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
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = text,
        singleLine = true,
        label = {
            Text(placeholder)
        },
        onValueChange = onValueChanged,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            disabledIndicatorColor =  MaterialTheme.colorScheme.outlineVariant,
        ),
        modifier = modifier
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
            checkedBorderColor = MaterialTheme.colorScheme.primary,
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

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun CommonUiPreview() {
    ScaInterAppTheme() {
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