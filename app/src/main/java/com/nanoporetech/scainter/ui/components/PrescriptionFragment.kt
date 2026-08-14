package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.consultation.Prescription
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

data class PrescriptionCardItem(
    val name: String = "",
    val quantity: String = "",
    val posology: String = "",
    val valueColor: Color? = null,
)

@Composable
fun PrescriptionCardBody(
    items: List<PrescriptionCardItem>,
    modifier: Modifier = Modifier,
    indentRight: Boolean = false,
    firstColumnWeight: Float = 0.4f,
    secondColumnWeight: Float = 0.6f,
) {
    Column(modifier) {
        for (item in items) {
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = item.name.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(firstColumnWeight)
                )
                Text(
                    text = item.quantity,
                    style = MaterialTheme.typography.bodyLarge,
                    color = item.valueColor ?: Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(secondColumnWeight),
                    textAlign = if (indentRight) TextAlign.End else TextAlign.Start
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = item.posology.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
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
        ConfirmationDialog(
            title = stringResource(R.string.remove_medication_prompt),
            message = stringResource(
                R.string.confirmation_prompt,
                item.name
            ),
            onConfirm = {
                onRemovePrescription(item)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            },
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun PrescriptionFragmentPreview() {
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
        }
    }
}