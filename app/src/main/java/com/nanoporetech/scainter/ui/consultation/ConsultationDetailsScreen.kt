package com.nanoporetech.scainter.ui.consultation

import android.R.attr.maxLines
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.imageUrl
import com.nanoporetech.scainter.ui.components.CardBody
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardHeaderDrawable
import com.nanoporetech.scainter.ui.components.CardItem
import com.nanoporetech.scainter.ui.theme.ScaInterTheme
import com.nanoporetech.scainter.ui.utils.displayedDate
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime

@Composable
fun ConsultationDetailsScreen(
    consultation: Consultation,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        // SUBSCRIBER INFO
        SubscriberInfo(
            consultation = consultation,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier.height(dimensionResource(R.dimen.padding_medium)))

        // CONSULTATION DETAILS
        ConsultationInfo(
            consultation = consultation,
            modifier = Modifier
                .fillMaxWidth()
        )


        // PRESCRIPTION SECTION

        // COSTS SECTION
    }
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

    val padding_medium = dimensionResource(R.dimen.padding_medium)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier
            .padding(padding_medium)) {
            CardHeaderDrawable(
                title = stringResource(R.string.consultation_info_title),
                iconImg = painterResource(R.drawable.stethoscope),
                modifier = Modifier
                    .padding(bottom = padding_medium)
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
            //verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
            ) {
            Text(
                text = consultation.fullname,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (consultation.internalId != "") {
                Text(
                    text = consultation.internalId,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = displayedDate(consultation.dateOfBirth),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "${consultation.subscriberName} (${consultation.contractType})",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun SubscriberAvatar(
    imgUrl: String,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(175.dp)
                )
            },
            error = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(175.dp)
                )
            },
            modifier = Modifier
                .size(175.dp)
                .clip(CircleShape)
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ConsultationDetailsScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppConstants.lightGreen)
                .padding(8.dp)
        ) {
            ConsultationDetailsScreen(
                consultation = DataSource.consultations().first()
            )
        }
    }
}