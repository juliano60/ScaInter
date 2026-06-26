package com.nanoporetech.scainter.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.model.displayedName
import com.nanoporetech.scainter.ui.components.MainHeader
import com.nanoporetech.scainter.ui.consultation.ConsultationCard
import com.nanoporetech.scainter.ui.examination.ExaminationCard
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationCard
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@Composable
fun HealthCareScreen(
    provider: Provider,
    modifier: Modifier = Modifier,
    onNewConsultation: () -> Unit = {},
    onNewExamination: () -> Unit = {},
    onNewHospitalisation: () -> Unit = {},
    onViewConsultations: () -> Unit = {},
    onViewExaminations: () -> Unit = {},
    onViewHospitalisations: () -> Unit = {},
) {
    val spacingLarge = dimensionResource(R.dimen.vertical_spacing_xlarge)

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingLarge),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // MAIN HEADER SECTION
        MainHeader(
            title = provider.displayedName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(R.dimen.padding_small))
        )

        // CONSULTATION MENU
        ConsultationCard(
            onNewConsultation = onNewConsultation,
            onViewConsultations = onViewConsultations,
            modifier = Modifier
                .fillMaxWidth()
        )

        // EXAMINATION MENU
        ExaminationCard(
            onNewExamination = onNewExamination,
            onViewExamination = onViewExaminations,
            modifier = Modifier
                .fillMaxWidth()
        )

        // HOSPITALISATION MENU
        HospitalisationCard(
            onNewHospitalisation = onNewHospitalisation,
            onViewHospitalisation = onViewHospitalisations,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun HealthCareScreenPreview() {
    ScaInterAppTheme {
        HealthCareScreen(
            provider = DataSource.healthProviders().first(),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}