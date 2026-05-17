package com.nanoporetech.scainter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.model.displayedName
import com.nanoporetech.scainter.ui.components.MainHeader
import com.nanoporetech.scainter.ui.components.SubHeader
import com.nanoporetech.scainter.ui.consultation.ConsultationCard
import com.nanoporetech.scainter.ui.examination.ExaminationCard
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationCard
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@Composable
fun HealthCareScreen(
    provider: Provider,
    modifier: Modifier = Modifier,
    onNewConsultation: () -> Unit = {},
    onViewConsultations: () -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vertical_spacing_small)),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // MAIN HEADER SECTION
            MainHeader(
                title = provider.displayedName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_medium))
            )

            // SUB HEADER SECTION
            SubHeader(
                title = stringResource(R.string.dashboard),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource((R.dimen.padding_small)))
            )

            // CONSULTATION MENU
            ConsultationCard(
                onNewConsultation = {},
                onViewConsultations = onViewConsultations,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.vertical_spacing_xsmall)))

            // EXAMINATION MENU
            ExaminationCard(
                onNewExamination = {},
                onViewExamination = {},
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.vertical_spacing_xsmall)))

            // HOSPITALISATION MENU
            HospitalisationCard(
                onNewHospitalisation = {},
                onViewHospitalisation = {},
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}


@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun HealthCareScreenPreview() {
    ScaInterAppTheme() {
        HealthCareScreen(
            provider = DataSource.healthProviders().first(),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}