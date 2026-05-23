package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.imageUrl
import com.nanoporetech.scainter.model.prescriptions
import com.nanoporetech.scainter.ui.components.CardBody
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardHeaderDrawable
import com.nanoporetech.scainter.ui.components.CardItem
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.capitalized
import com.nanoporetech.scainter.ui.utils.displayedDate
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime
import com.nanoporetech.scainter.ui.utils.formatCurrency
import com.nanoporetech.scainter.ui.utils.formatDoctorName

private const val TAG = "ConsultationDetailsScreen"

@Composable
fun ConsultationDetailsScreen(
    consultation: Consultation,
    modifier: Modifier = Modifier,
    onNewPrescription: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()
    )) {
        // SUBSCRIBER INFO
        SubscriberInfo(
            consultation = consultation,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // CONSULTATION DETAILS
        ConsultationInfo(
            consultation = consultation,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // PRESCRIPTION SECTION
        PrescriptionSection(
            consultation = consultation,
            onNewPrescription = onNewPrescription,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // COSTS SECTION
        CostsSection(
            consultation = consultation,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
    }
}

@Composable
fun CostsSection(
    consultation: Consultation,
    modifier: Modifier = Modifier
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val items = listOf<CardItem>(
        CardItem(stringResource(R.string.total_cost_label), formatCurrency(consultation.total)),
        CardItem(stringResource(R.string.total_sca_label), formatCurrency(consultation.totalSca)),
        CardItem(stringResource(R.string.total_holder_label), formatCurrency(consultation.totalUser))
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(paddingMedium)
        ) {
            CardHeader(
                title = stringResource(R.string.total_cost_title),
                iconImg = Icons.Filled.Payments,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBody(
                items = items,
                indentRight = true
            )
        }
    }
}

@Composable
fun PrescriptionSection(
    consultation: Consultation,
    onNewPrescription: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val items = listOf<CardItem>(
        CardItem(stringResource(R.string.prescriber_label), formatDoctorName(consultation.doctor ?: "")),
        CardItem(stringResource(R.string.affection_label), consultation.affliction ?: "")
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(paddingMedium)
        ) {
            CardHeader(
                title = stringResource(R.string.medical_prescription_title),
                iconImg = Icons.AutoMirrored.Filled.Assignment,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBody(items = items)

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            if (consultation.prescriptions.isEmpty()) {
                PrescriptionButton(
                    onNewPrescription = onNewPrescription,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                PrescriptionDetails(
                    consultation = consultation,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }

}

@Composable
fun PrescriptionDetails(
    consultation: Consultation,
    modifier: Modifier = Modifier
) {
    val items = mutableListOf<CardItem>()

    fun addIfNotEmpty(key: String?, value: Int) {
        if (!key.isNullOrBlank()) {
            items.add(CardItem(label = key.capitalized(), value.toString()))
        }
    }

    addIfNotEmpty(consultation.prescription, consultation.quantity)
    addIfNotEmpty(consultation.prescription1, consultation.quantity1)
    addIfNotEmpty(consultation.prescription2, consultation.quantity2)
    addIfNotEmpty(consultation.prescription3, consultation.quantity3)

    CardBody(
        items = items,
        firstColumnWeight = 0.9f,
        secondColumnWeight = 0.1f,
        keyIsBold = false,
        indentRight = true,
        modifier = modifier
    )
}
@Composable
fun PrescriptionButton(
    onNewPrescription: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        iconImg = Icons.Filled.AddCircle,
        text = stringResource(R.string.add_prescription_button),
        modifier = modifier,
        onClick = onNewPrescription
    )
}

@Composable
fun ConsultationInfo(
    consultation: Consultation,
    modifier: Modifier = Modifier
) {
    val items = listOf<CardItem>(
        CardItem(stringResource(R.string.act_label), consultation.act),
        CardItem(stringResource(R.string.date_label),
            displayedDateAndTime(
            consultation.creationDate)),
        CardItem(stringResource(R.string.coverage_label), consultation.percentageCoverage)
    )

    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Column(modifier = Modifier
            .padding(paddingMedium)) {
            CardHeaderDrawable(
                title = stringResource(R.string.consultation_info_title),
                iconImg = painterResource(R.drawable.stethoscope),
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBody(items = items)
        }
    }
}

@Composable
fun SubscriberInfo(
    consultation: Consultation,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        SubscriberAvatar(
            imgUrl = consultation.imageUrl,
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
                text = consultation.fullname,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (consultation.internalId.isNotBlank()) {
                Text(
                    text = consultation.internalId,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = displayedDate(consultation.dateOfBirth),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "${consultation.subscriberName} (${consultation.contractType})",
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun SubscriberAvatar(
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
                            scaleX = 1.2f
                            scaleY = 1.2f
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
                            scaleX = 1.2f
                            scaleY = 1.2f
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

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ConsultationDetailsScreenPreview() {
    ScaInterAppTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                ConsultationDetailsScreen(
                    // 0-1 - with prescriptions
                    // 2 - without prescriptions
                    consultation = DataSource.consultations()[1]
                )
            }
        }
    }
}