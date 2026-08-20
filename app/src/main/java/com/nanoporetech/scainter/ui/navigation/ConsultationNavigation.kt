package com.nanoporetech.scainter.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.consultation.ConsultationDetailsScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationListScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationNewPrescriptionScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationPolicyHolderDetailsScreen
import com.nanoporetech.scainter.ui.consultation.ListConsultationsViewModel
import com.nanoporetech.scainter.ui.consultation.MedicalPrescriptionViewModel
import com.nanoporetech.scainter.ui.consultation.NewConsultationScreen
import com.nanoporetech.scainter.ui.consultation.NewConsultationViewModel
import com.nanoporetech.scainter.ui.events.UiEvent
import com.nanoporetech.scainter.ui.menu.NavGraphs
import com.nanoporetech.scainter.ui.menu.NavResult
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.utils.AppSnackbarVisuals
import com.nanoporetech.scainter.ui.utils.SnackbarType
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "ConsultationNavigation"

object ConsultationPolicyHolderDetails {
    const val POLICY_HOLDER_ID = "policyHolderId"

    val route =
        "${ScaAppScreen.ConsultationPolicyHolderDetails.name}/{$POLICY_HOLDER_ID}"

    fun createRoute(policyHolderId: Int) =
        "${ScaAppScreen.ConsultationPolicyHolderDetails.name}/$policyHolderId"
}

object ConsultationDetails {
    const val CONSULTATION_ID = "consultationId"

    val route =
        "${ScaAppScreen.ConsultationDetails.name}/{$CONSULTATION_ID}"

    fun createRoute(consultationId: Int) =
        "${ScaAppScreen.ConsultationDetails.name}/$consultationId"
}

fun NavGraphBuilder.consultationNavigation(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    navigation(
        route = NavGraphs.NEW_CONSULTATION,
        startDestination = ScaAppScreen.ConsultationNewConsultation.name
    ) {
        newConsultationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }

    navigation(
        route = NavGraphs.EXISTING_CONSULTATION,
        startDestination = ScaAppScreen.ConsultationList.name
    ) {
        existingConsultationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.newConsultationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    composable(route = ScaAppScreen.ConsultationNewConsultation.name) { backStackEntry ->
        val scanResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(SCAN_RESULT, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(scanResult) {
            scanResult?.let { familyId ->
                val parentEntry = navController.getBackStackEntry(
                    NavGraphs.NEW_CONSULTATION
                )
                parentEntry.savedStateHandle[FAMILY_ID] = familyId
                backStackEntry.savedStateHandle[SCAN_RESULT] = null
                navController.navigate(
                    route = ScaAppScreen.ConsultationFamilyMembersList.name
                )
            }
        }

        NewConsultationScreen(
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

    composable(route = ScaAppScreen.ConsultationFamilyMembersList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_CONSULTATION)
        }

        val viewModel = newConsultationViewModel(
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

        ConsultationFamilyMembersListScreen(
            members = state.familyMembers,
            onMemberSelected = { policyHolderId ->
                navController.navigate(route = ConsultationPolicyHolderDetails.createRoute(policyHolderId))
            },
            onScanQrCode = {
                navController.navigate(
                    route = ScaAppScreen.CodeScanner.name
                )
            },
            modifier = Modifier
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    composable(
        route = ConsultationPolicyHolderDetails.route,
        arguments = listOf(
            navArgument(ConsultationPolicyHolderDetails.POLICY_HOLDER_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val policyHolderId = backStackEntry.arguments?.getInt(ConsultationPolicyHolderDetails.POLICY_HOLDER_ID)

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_CONSULTATION)
        }

        val viewModel: NewConsultationViewModel = viewModel(
            viewModelStoreOwner = parentEntry
        )

        val context = LocalContext.current

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = uiState.policyHolders.firstOrNull { it.id == policyHolderId }

        Log.d(TAG, "PolicyHolder: $policyHolder")

        LaunchedEffect(policyHolder) {
            policyHolder?.let {
                viewModel.setPolicyHolder(it)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is UiEvent.Success -> {
                        // indicate success/failure
                        navController
                            .getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)
                            .savedStateHandle["nav_result"] = NavResult.NewConsultationSuccess.name

                        // then navigate
                        navController.navigate(NavGraphs.EXISTING_CONSULTATION) {
                            popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                                inclusive = false
                            }
                        }
                    }
                    is UiEvent.Error -> {
                        snackbarHostState.showSnackbar(
                            message = context.getString(event.errorId),
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            }
        }

        if (policyHolder != null) {
            ConsultationPolicyHolderDetailsScreen(
                policyHolder = policyHolder,
                selectedConsultation = uiState.selectedConsultation,
                selectedCost = uiState.selectedCost,
                onConsultationSelected = viewModel::setConsultation,
                onCostSelected = viewModel::setCost,
                onValidate = { viewModel.newConsultation() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
private fun newConsultationViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    providerName: String
): NewConsultationViewModel {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(NavGraphs.NEW_CONSULTATION)
    }

    return viewModel(
        viewModelStoreOwner = parentEntry,
        factory = NewConsultationViewModel.provideFactory(
            providerName = providerName
        )
    )
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.existingConsultationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    composable(route = ScaAppScreen.ConsultationList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.EXISTING_CONSULTATION)
        }

        val dashboardEntry = remember(backStackEntry) {
            navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)
        }
        val dashboardNavResult by dashboardEntry
            .savedStateHandle
            .getStateFlow<String?>("nav_result", null)
            .collectAsStateWithLifecycle()

        val context = LocalContext.current

        LaunchedEffect(dashboardNavResult) {
            when (dashboardNavResult) {
                NavResult.NewConsultationSuccess.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.new_consultation_success_message),
                            type = SnackbarType.Success
                        )
                    )
                }
                NavResult.NewConsultationFailed.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.err_unknown_error_message),
                            type = SnackbarType.Error,
                            duration = SnackbarDuration.Long
                        )
                    )
                }
                NavResult.NewPrescriptionSuccess.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.new_prescription_added),
                            type = SnackbarType.Success
                        )
                    )
                }
                null -> Unit
            }
            // now clear old nav result
            if (dashboardNavResult != null) {
                dashboardEntry.savedStateHandle["nav_result"] = null
            }
        }

        val consultationViewModel: ListConsultationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = ListConsultationsViewModel.provideFactory(
                providerName = providerName
            )
        )

        val uiState by consultationViewModel.uiState.collectAsStateWithLifecycle()

        ConsultationListScreen(
            consultations = uiState.consultations,
            isLoading = uiState.isLoading,
            onRowClick = { consultation ->
                navController.navigate(
                    route = ConsultationDetails.createRoute(consultation.id)
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    composable(
        route = ConsultationDetails.route,
        arguments = listOf(
            navArgument(ConsultationDetails.CONSULTATION_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val consultationId = requireNotNull(
            backStackEntry.arguments?.getInt(ConsultationDetails.CONSULTATION_ID)
        )

        // grab parent's view model
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(
                NavGraphs.EXISTING_CONSULTATION
            )
        }

        val viewModel: ListConsultationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // extract consultation with matching consultationId
        val consultation = uiState.consultations.find {
            it.id == consultationId
        }

        if (consultation != null) {
            ConsultationDetailsScreen(
                consultation = consultation,
                onNewPrescription = {
                    navController.navigate(
                        route = ScaAppScreen.ConsultationNewPrescription.name
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }

    composable(route = ScaAppScreen.ConsultationNewPrescription.name) { backStackEntry ->
        // grab the consultation id from the parent route
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(ConsultationDetails.route)
        }

        val consultationId = requireNotNull(
            parentEntry.arguments?.getInt(ConsultationDetails.CONSULTATION_ID)
        ).toString()

        val context = LocalContext.current

        val prescriptionViewModel: MedicalPrescriptionViewModel = viewModel(
            factory = MedicalPrescriptionViewModel.provideFactory(
                consultationId = consultationId
            )
        )

        // convert from model.events to nav_result
        LaunchedEffect(Unit) {
            prescriptionViewModel.events.collect { event ->
                when (event) {
                    is UiEvent.Success -> {
                        navController
                            .getBackStackEntry(
                                ScaAppScreen.HealthCareDashboard.name)
                            .savedStateHandle["nav_result"] = NavResult.NewPrescriptionSuccess.name

                        navController.navigate(NavGraphs.EXISTING_CONSULTATION) {
                            popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                                inclusive = false
                            }
                        }
                    }
                    is UiEvent.Error -> {
                        snackbarHostState.showSnackbar(
                            AppSnackbarVisuals(
                                message = context.getString(event.errorId),
                                type = SnackbarType.Error,
                                duration = SnackbarDuration.Long
                            )
                        )
                    }
                }
            }
        }

        val uiState by prescriptionViewModel.uiState.collectAsStateWithLifecycle()

        ConsultationNewPrescriptionScreen(
            state = uiState,
            onDoctorChanged = prescriptionViewModel::setDoctor,
            onAffectionChanged = prescriptionViewModel::setAffection,
            onMedicationChanged = prescriptionViewModel::setMedication,
            onQuantityChanged = prescriptionViewModel::setQuantity,
            onPosologyChanged = prescriptionViewModel::setPosology,
            onMedication1Changed = prescriptionViewModel::setMedication1,
            onQuantity1Changed = prescriptionViewModel::setQuantity1,
            onPosology1Changed = prescriptionViewModel::setPosology1,
            onMedication2Changed = prescriptionViewModel::setMedication2,
            onQuantity2Changed = prescriptionViewModel::setQuantity2,
            onPosology2Changed = prescriptionViewModel::setPosology2,
            onMedication3Changed = prescriptionViewModel::setMedication3,
            onQuantity3Changed = prescriptionViewModel::setQuantity3,
            onPosology3Changed = prescriptionViewModel::setPosology3,
            onSendPrescription = prescriptionViewModel::addPrescription,
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium)),
        )
    }
}
