package com.nanoporetech.scainter.ui.examination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Examination
import com.nanoporetech.scainter.model.imageUrl
import com.nanoporetech.scainter.ui.components.CardBodyTwoLines
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardHeaderDrawable
import com.nanoporetech.scainter.ui.components.CardItem
import com.nanoporetech.scainter.ui.components.CostsFragment
import com.nanoporetech.scainter.ui.components.ExaminationCardBody
import com.nanoporetech.scainter.ui.components.ExaminationCardItem
import com.nanoporetech.scainter.ui.components.PolicyHolderInfoFragment
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExaminationDetailsScreen(
    examination: Examination,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(paddingMedium),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // SUBSCRIBER INFO
            PolicyHolderInfoFragment(
                name = examination.fullname,
                internalId = examination.internalId,
                subscriberName = examination.subscriberName,
                contractType = examination.contractType,
                imageUrl = examination.imageUrl,
                coverPercent = examination.coverPercentage,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // EXAMINATION INFO SECTION
            ExaminationInfo(
                examination = examination,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Examination List section
            ExaminationListSection(
                examination = examination,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // COSTS SECTION
            CostsFragment(
                costTotal = examination.total.toString(),
                costSca = examination.totalSca.toString(),
                costUser = examination.totalUser.toString(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
fun ExaminationListSection(
    examination: Examination,
    modifier: Modifier = Modifier
) {
    val items = mutableListOf<ExaminationCardItem>()
    val examDefaultAnswer = stringResource(R.string.exam_default_answer)

    fun addIfNotEmpty(key: String?, value: String?) {
        if (!key.isNullOrBlank()) {
            items.add(ExaminationCardItem(label = key, value = if (value.isNullOrBlank()) examDefaultAnswer else value))
        }
    }

    for (index in examination.exams.indices) {
        addIfNotEmpty(examination.exams[index], examination.answers[index])
    }

    val paddingMedium = dimensionResource(R.dimen.padding_medium)

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
                title = stringResource(R.string.exam_list_title),
                iconImg = Icons.AutoMirrored.Filled.Assignment,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            ExaminationCardBody(items = items)
        }
    }
}

@Composable
fun ExaminationInfo(
    examination: Examination,
    modifier: Modifier = Modifier
) {
    val items = mutableListOf<CardItem>()

    fun addIfNotEmpty(key: String?, value: String?, valueColor: Color? = null) {
        if (!key.isNullOrBlank() && !value.isNullOrBlank()) {
            items.add(CardItem(label = key, value = value, valueColor = valueColor))
        }
    }

    addIfNotEmpty(stringResource(R.string.exam_status_label), examination.status, Color.Red)
    addIfNotEmpty(stringResource(R.string.exam_validator_label), examination.controller)
    addIfNotEmpty(stringResource(R.string.exam_doctor_label), examination.doctor)
    addIfNotEmpty(stringResource(R.string.exam_specialty_label), examination.specialty)
    addIfNotEmpty(stringResource(R.string.exam_reason_label), examination.reason, Color.Red)
    addIfNotEmpty(stringResource(R.string.date_label), displayedDateAndTime(examination.creationDate))

    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(paddingMedium)
        ) {
            CardHeaderDrawable(
                title = stringResource(R.string.exam_info_title),
                iconImg = painterResource(R.drawable.vital_signs),
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBodyTwoLines(items = items)
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ExaminationDetailsScreenPreview() {
    ScaInterAppTheme {
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
                ExaminationDetailsScreen(
                    examination = DataSource.examinations()[0],
                    isRefreshing = false,
                )
            }
        }
    }
}