package com.nanoporetech.scainter.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.ui.menu.HealthCareScreen
import com.nanoporetech.scainter.ui.menu.NavGraphs
import com.nanoporetech.scainter.ui.menu.ScaAppScreen

fun NavGraphBuilder.dashboardNavigation(
    navController: NavController,
    provider: Provider,
) {
    composable(route = ScaAppScreen.HealthCareDashboard.name) {
        HealthCareScreen(
            provider = provider,
            onViewConsultations = {
                navController.navigate(NavGraphs.EXISTING_CONSULTATION)
            },
            onViewExaminations = {
                navController.navigate(NavGraphs.EXISTING_EXAMINATION)
            },
            onViewHospitalisations = {
                navController.navigate(NavGraphs.EXISTING_HOSPITALISATION)
            },
            onNewConsultation = {
                navController.navigate(NavGraphs.NEW_CONSULTATION)
            },
            onNewExamination = {
                navController.navigate(NavGraphs.NEW_EXAMINATION)
            },
            onNewHospitalisation = {
                navController.navigate(NavGraphs.NEW_HOSPITALISATION)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}