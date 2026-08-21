package com.nanoporetech.scainter.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationListScreen
import com.nanoporetech.scainter.ui.hospitalisation.ListHospitalisationsViewModel
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
    composable(route = ScaAppScreen.HospitalisationNewHospitalisation.name) { backStackEntry ->
        val scanResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(SCAN_RESULT, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(scanResult) {
            scanResult?.let { familyId ->
                val parentEntry = navController.getBackStackEntry(
                    NavGraphs.NEW_HOSPITALISATION
                )
                parentEntry.savedStateHandle[FAMILY_ID] = familyId
                backStackEntry.savedStateHandle[SCAN_RESULT] = null
                navController.navigate(
                    route = ScaAppScreen.HospitalisationFamilyMembersList.name
                )
            }
        }

        NewHospitalisationScreen(
            onScanQrCode = {
                navController.navigate(
                    route = ScaAppScreen.CodeScanner.name
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium)),
        )
    }

    composable(route = ScaAppScreen.HospitalisationFamilyMembersList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_HOSPITALISATION)
        }

        val viewModel = newHospitalisationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val scanResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(SCAN_RESULT, null)
            .collectAsStateWithLifecycle()

        val familyId by parentEntry.savedStateHandle
            .getStateFlow<String?>(FAMILY_ID, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(scanResult) {
            scanResult?.let { newFamilyId ->
                parentEntry.savedStateHandle[FAMILY_ID] = newFamilyId
                backStackEntry.savedStateHandle[SCAN_RESULT] = null
            }
        }

        LaunchedEffect(familyId) {
            familyId?.let(viewModel::loadFamily)
        }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        HospitalisationFamilyMembersListScreen(
            members = state.familyMembers,
            onMemberSelected = { policyHolderId ->
                navController.navigate(route = HospitalisationPolicyHolderDetails.createRoute(policyHolderId))
            },
            onScanQrCode = {
                navController.navigate(
                    route = ScaAppScreen.CodeScanner
                )
            },
            modifier = Modifier
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
private fun newHospitalisationViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    providerName: String
): NewHospitalisationViewModel {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(NavGraphs.NEW_HOSPITALISATION)
    }

    return viewModel(
        viewModelStoreOwner = parentEntry,
        factory = NewHospitalisationViewModel.provideFactory(
            providerName = providerName
        )
    )
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.existingHospitalisationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    composable(route = ScaAppScreen.HospitalisationList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.EXISTING_HOSPITALISATION)
        }

        val dashboardEntry = remember(backStackEntry) {
            navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)
        }
        val dashboardNavResult by dashboardEntry
            .savedStateHandle
            .getStateFlow<String?>("nav_result", null)
            .collectAsStateWithLifecycle()

        val context = LocalContext.current

        val hospitalisationViewModel: ListHospitalisationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = ListHospitalisationsViewModel.provideFactory(
                providerName = providerName
            )
        )

        LaunchedEffect(Unit) {
            hospitalisationViewModel.loadHospitalisations()
        }

        val uiState by hospitalisationViewModel.uiState.collectAsStateWithLifecycle()

        HospitalisationListScreen(
            hospitalisations = uiState.hospitalisations,
            isLoading = uiState.isLoading,
            onRowClick = { examination ->
                navController.navigate(
                    route = HospitalisationDetails.createRoute(examination.id)
                )
            },
            onRefresh = hospitalisationViewModel::loadHospitalisations,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
