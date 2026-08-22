package com.nanoporetech.scainter.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.nanoporetech.scainter.ui.events.UiMessage
import com.nanoporetech.scainter.ui.examination.ExaminationDetailsScreen
import com.nanoporetech.scainter.ui.examination.ExaminationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.examination.ExaminationListScreen
import com.nanoporetech.scainter.ui.examination.ExaminationPolicyHolderDetailsScreen
import com.nanoporetech.scainter.ui.examination.ExaminationRegularExamScreen
import com.nanoporetech.scainter.ui.examination.ExaminationSameDayExamScreen
import com.nanoporetech.scainter.ui.examination.SameDayExaminationViewModel
import com.nanoporetech.scainter.ui.examination.ListExaminationsViewModel
import com.nanoporetech.scainter.ui.examination.NewExaminationScreen
import com.nanoporetech.scainter.ui.examination.NewExaminationViewModel
import com.nanoporetech.scainter.ui.examination.RegularExaminationViewModel
import com.nanoporetech.scainter.ui.menu.NavGraphs
import com.nanoporetech.scainter.ui.menu.NavResult
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.utils.AppSnackbarVisuals
import com.nanoporetech.scainter.ui.utils.SnackbarType

private const val TAG = "ExaminationNavigation"
private const val NAV_RESULT = "nav_result"

object ExaminationPolicyHolderDetails {
    const val POLICY_HOLDER_ID = "policyHolderId"

    val route =
        "${ScaAppScreen.ExaminationPolicyHolderDetails.name}/{$POLICY_HOLDER_ID}"

    fun createRoute(policyHolderId: Int) =
        "${ScaAppScreen.ExaminationPolicyHolderDetails.name}/$policyHolderId"
}

object ExaminationDetails {
    const val EXAMINATION_ID = "examinationId"

    val route =
        "${ScaAppScreen.ExaminationDetails.name}/{$EXAMINATION_ID}"

    fun createRoute(examinationId: Int) =
        "${ScaAppScreen.ExaminationDetails.name}/$examinationId"
}

fun NavGraphBuilder.examinationNavigation(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState,
) {
    navigation(
        route = NavGraphs.NEW_EXAMINATION,
        startDestination = ScaAppScreen.ExaminationNewExamination.name
    ) {
        newExaminationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }

    navigation(
        route = NavGraphs.EXISTING_EXAMINATION,
        startDestination = ScaAppScreen.ExaminationList.name
    ) {
        existingExaminationGraph(
            navController = navController,
            providerName = providerName,
            snackbarHostState = snackbarHostState
        )
    }
}

@SuppressLint("LocalContextGetResourceValueCall", "ComposableDestinationInComposeScope")
private fun NavGraphBuilder.newExaminationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState,
) {
    composable(route = ScaAppScreen.ExaminationNewExamination.name) { backStackEntry ->
        val scanResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(SCAN_RESULT, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(scanResult) {
            scanResult?.let { familyId ->
                val parentEntry = navController.getBackStackEntry(
                    NavGraphs.NEW_EXAMINATION
                )
                parentEntry.savedStateHandle[FAMILY_ID] = familyId
                backStackEntry.savedStateHandle[SCAN_RESULT] = null
                navController.navigate(
                    route = ScaAppScreen.ExaminationFamilyMembersList.name
                )
            }
        }

        NewExaminationScreen(
            onScanQrCode = {
                navController.navigate(route = ScaAppScreen.CodeScanner.name)
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium)),
        )
    }

    composable(route = ScaAppScreen.ExaminationFamilyMembersList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_EXAMINATION)
        }

        val viewModel = newExaminationViewModel(
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

        ExaminationFamilyMembersListScreen(
            members = state.familyMembers,
            onMemberSelected = { policyHolderId ->
                navController.navigate(route = ExaminationPolicyHolderDetails.createRoute(policyHolderId))
            },
            onScanQrCode = {
                navController.navigate(
                    ScaAppScreen.CodeScanner.name
                )
            },
            modifier = Modifier
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    composable(
        route = ExaminationPolicyHolderDetails.route,
        arguments = listOf(
            navArgument(ExaminationPolicyHolderDetails.POLICY_HOLDER_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val policyHolderId = requireNotNull(
            backStackEntry.arguments?.getInt(
                ExaminationPolicyHolderDetails.POLICY_HOLDER_ID
            )
        )

        val viewModel = newExaminationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val localUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = localUiState.policyHolders.firstOrNull { it.id == policyHolderId }

        Log.d(TAG, "PolicyHolder: $policyHolder")

        // NOTE: do the setPolicyHolder in a LaunchedEffect to avoid repeated recompositions
        LaunchedEffect(policyHolder) {
            policyHolder?.let(viewModel::setPolicyHolder)
        }

        if (policyHolder != null) {
            ExaminationPolicyHolderDetailsScreen(
                policyHolder = policyHolder,
                onDayExamination = {
                    navController.navigate(ScaAppScreen.ExaminationSameDayExamination.name)
                },
                onRegularExamination = {
                    navController.navigate(ScaAppScreen.ExaminationRegularExamination.name)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }

    composable(route = ScaAppScreen.ExaminationRegularExamination.name) { backStackEntry ->
        val newExaminationViewModel = newExaminationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val newExaminationUiState by newExaminationViewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = newExaminationUiState.currentPolicyHolder

        if (policyHolder != null) {
            val viewModel: RegularExaminationViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                key = "regular_examination_${policyHolder.id}",
                factory = RegularExaminationViewModel.provideFactory(
                    providerName = providerName,
                    insuranceType = policyHolder.insuranceType,
                    userId = policyHolder.id.toString()
                )
            )

            val context = LocalContext.current
            val localUiState by viewModel.uiState.collectAsStateWithLifecycle()

            // convert from model.events to nav_result
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiMessage.Success -> {
                            val dashboardEntry = navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)

                            dashboardEntry.savedStateHandle[NAV_RESULT] = NavResult.NewRegularExaminationSuccess.name

                            navController.navigate(NavGraphs.EXISTING_EXAMINATION) {
                                popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                                    inclusive = false
                                }
                            }
                        }
                        is UiMessage.Error -> {
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

            ExaminationRegularExamScreen(
                state = localUiState,
                onDoctorChanged = viewModel::setDoctor,
                onSpecialtyChanged = viewModel::setSpecialty,
                onReasonChanged = viewModel::setReason,
                onExaminationSelected = viewModel::setSelectedExamination,
                onRequestExamination = viewModel::requestExamination,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium)),
            )
        }
    }

    composable(route = ScaAppScreen.ExaminationSameDayExamination.name) { backStackEntry ->
        val newExaminationViewModel = newExaminationViewModel(
            navController,
            backStackEntry,
            providerName
        )

        val context = LocalContext.current
        val newExaminationUiState by newExaminationViewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = newExaminationUiState.currentPolicyHolder

        if (policyHolder != null) {
            val viewModel: SameDayExaminationViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                key = "same_day_examination_${policyHolder.id}",
                factory = SameDayExaminationViewModel.provideFactory(
                    providerName = providerName,
                    careCoverage = policyHolder.coverExternal,
                    userId = policyHolder.id.toString()
                )
            )

            // convert from model.events to nav_result
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiMessage.Success -> {
                            val dashboardEntry = navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)

                            dashboardEntry.savedStateHandle[NAV_RESULT] = NavResult.NewSameDayExaminationSuccess.name

                            navController.navigate(NavGraphs.EXISTING_EXAMINATION) {
                                popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                                    inclusive = false
                                }
                            }
                        }
                        is UiMessage.Error -> {
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

            val localUiState by viewModel.uiState.collectAsStateWithLifecycle()

            ExaminationSameDayExamScreen(
                reason = localUiState.reason,
                designation = localUiState.designation,
                costTotal = localUiState.costTotal,
                costSca = localUiState.costSca,
                costUser = localUiState.costUser,
                isSubmitting = localUiState.isSubmitting,
                onReasonChanged = viewModel::setReason,
                onDesignationChanged = viewModel::setDesignation,
                onCostChanged = viewModel::updateCost,
                onSubmitRequest = viewModel::submitRequest,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium)),
            )
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.existingExaminationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState) {

    composable(route = ScaAppScreen.ExaminationList.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.EXISTING_EXAMINATION)
        }

        val dashboardEntry = remember(backStackEntry) {
            navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)
        }
        val dashboardNavResult by dashboardEntry
            .savedStateHandle
            .getStateFlow<String?>(NAV_RESULT, null)
            .collectAsStateWithLifecycle()

        val context = LocalContext.current

        LaunchedEffect(dashboardNavResult) {
            when (dashboardNavResult) {
                NavResult.NewSameDayExaminationSuccess.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.new_same_day_care_success_message),
                            type = SnackbarType.Success
                        )
                    )
                }
                NavResult.NewRegularExaminationSuccess.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.new_regular_exam_success_message),
                            type = SnackbarType.Success
                        )
                    )
                }
                null -> Unit
            }
            // now clear old nav result
            if (dashboardNavResult != null) {
                dashboardEntry.savedStateHandle[NAV_RESULT] = null
            }
        }

        val examinationViewModel: ListExaminationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = ListExaminationsViewModel.provideFactory(
                providerName = providerName
            )
        )

        LaunchedEffect(Unit) {
            examinationViewModel.loadExaminations()
        }

        val uiState by examinationViewModel.uiState.collectAsStateWithLifecycle()

        ExaminationListScreen(
            examinations = uiState.examinations,
            isLoading = uiState.isLoading,
            onRowClick = { examination ->
                navController.navigate(
                    route = ExaminationDetails.createRoute(examination.id)
                )
            },
            onRefresh = examinationViewModel::loadExaminations,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }

    composable(
        route = ExaminationDetails.route,
        arguments = listOf(
            navArgument(ExaminationDetails.EXAMINATION_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val examinationId = requireNotNull(
            backStackEntry.arguments?.getInt(ExaminationDetails.EXAMINATION_ID)
        )

        // grab parent's view model
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(
                NavGraphs.EXISTING_EXAMINATION
            )
        }

        val viewModel: ListExaminationsViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = ListExaminationsViewModel.provideFactory(
                providerName = providerName
            )
        )

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // extract consultation with matching consultationId
        val examination = uiState.examinations.find {
            it.id == examinationId
        }

        if (examination != null) {
            ExaminationDetailsScreen(
                examination = examination,
                isRefreshing = uiState.isLoading,
                onRefresh = viewModel::loadExaminations,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
private fun newExaminationViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    providerName: String
): NewExaminationViewModel {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry(NavGraphs.NEW_EXAMINATION)
    }

    return viewModel(
        viewModelStoreOwner = parentEntry,
        factory = NewExaminationViewModel.provideFactory(
            providerName = providerName
        )
    )
}
