package com.nanoporetech.scainter.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationListScreen
import com.nanoporetech.scainter.ui.hospitalisation.NewHospitalisationScreen
import com.nanoporetech.scainter.ui.hospitalisation.NewHospitalisationViewModel
import com.nanoporetech.scainter.ui.menu.NavGraphs
import com.nanoporetech.scainter.ui.menu.ScaAppScreen


private const val TAG = "HospitalisationNavigation"

object HospitalisationPolicyHolderDetails {
    const val POLICY_HOLDER_ID = "policyHolderId"

    val route =
        "${ScaAppScreen.HospitalisationPolicyHolderDetails.name}/{$POLICY_HOLDER_ID}"

    fun createRoute(policyHolderId: Int) =
        "${ScaAppScreen.HospitalisationPolicyHolderDetails.name}/$policyHolderId"
}

object HospitalisationFamilyMembersList {
    const val FAMILY_ID = "familyId"

    val route =
        "${ScaAppScreen.HospitalisationFamilyMembersList.name}/{$FAMILY_ID}"
}

object HospitalisationDetails {
    const val HOSPITALISATION_ID = "hospitalisationId"

    val route =
        "${ScaAppScreen.HospitalisationDetails.name}/{$HOSPITALISATION_ID}"

    fun createRoute(hospitalisationId: Int) =
        "${ScaAppScreen.HospitalisationDetails.name}/$hospitalisationId"
}

fun NavGraphBuilder.hospitalisationNavigation(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState
) {
    navigation(
        route = NavGraphs.NEW_HOSPITALISATION,
        startDestination = ScaAppScreen.HospitalisationNewHospitalisation.name
    ) {
        newHospitalisationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }

    navigation(
        route = NavGraphs.EXISTING_HOSPITALISATION,
        startDestination = ScaAppScreen.HospitalisationList.name
    ) {
        existingHospitalisationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.newHospitalisationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState,
) {
    composable(route = ScaAppScreen.HospitalisationNewHospitalisation.name) {
        NewHospitalisationScreen(
            onScanQrCode = {
                navController.navigate(
                    ScaAppScreen.codeScannerRoute(
                        HospitalisationFamilyMembersList.route
                    )
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium)),
        )
    }

    composable(route = ScaAppScreen.HospitalisationFamilyMembersList.name,
        arguments = listOf(
            navArgument(ConsultationFamilyMembersList.FAMILY_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val familyId = requireNotNull(
            backStackEntry.arguments?.getString(HospitalisationFamilyMembersList.FAMILY_ID)
        )
        val viewModel: NewHospitalisationViewModel = viewModel(
            factory = NewHospitalisationViewModel.provideFactory(
                familyId = familyId,
                providerName = providerName
            )
        )
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        HospitalisationFamilyMembersListScreen(
            members = state.familyMembers,
            onMemberSelected = { policyHolderId ->
                navController.navigate(route = HospitalisationPolicyHolderDetails.createRoute(policyHolderId))
            },
            onScanQrCode = {
                navController.navigate(
                    ScaAppScreen.codeScannerRoute(
                        ScaAppScreen.HospitalisationFamilyMembersList
                    )
                )
            },
            modifier = Modifier
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.existingHospitalisationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    composable(route = ScaAppScreen.HospitalisationList.name) {
        HospitalisationListScreen(
            providerName = providerName,
            /*onRowClick = { consultation ->
            navController.navigate(
                route = "${ScaAppScreen.ExaminationDetailsScreen.name}/${consultation.id}"
            )
        },*/
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
