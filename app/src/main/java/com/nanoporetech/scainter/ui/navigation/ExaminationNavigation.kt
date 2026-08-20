package com.nanoporetech.scainter.ui.navigation

import android.R.attr.type
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.nanoporetech.scainter.ui.events.UiEvent
import com.nanoporetech.scainter.ui.examination.ExaminationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.examination.ExaminationListScreen
import com.nanoporetech.scainter.ui.examination.ExaminationPolicyHolderDetailsScreen
import com.nanoporetech.scainter.ui.examination.ExaminationSameDayExamScreen
import com.nanoporetech.scainter.ui.examination.ExaminationViewModel
import com.nanoporetech.scainter.ui.examination.NewExaminationScreen
import com.nanoporetech.scainter.ui.examination.NewExaminationViewModel
import com.nanoporetech.scainter.ui.menu.NavGraphs
import com.nanoporetech.scainter.ui.menu.NavResult
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.utils.AppSnackbarVisuals
import com.nanoporetech.scainter.ui.utils.SnackbarType

private const val TAG = "ExaminationNavigation"

object ExaminationPolicyHolderDetails {
    const val POLICY_HOLDER_ID = "policyHolderId"

    val route =
        "${ScaAppScreen.ExaminationPolicyHolderDetails.name}/{$POLICY_HOLDER_ID}"

    fun createRoute(policyHolderId: Int) =
        "${ScaAppScreen.ExaminationPolicyHolderDetails.name}/$policyHolderId"
}

object ExaminationFamilyMembersList {
    const val FAMILY_ID = "familyId"

    val route =
        "${ScaAppScreen.ExaminationFamilyMembersList.name}/{$FAMILY_ID}"
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

@SuppressLint("LocalContextGetResourceValueCall")
private fun NavGraphBuilder.newExaminationGraph(
    navController: NavController,
    providerName: String,
    snackbarHostState: SnackbarHostState,
) {
    composable(route = ScaAppScreen.ExaminationNewExamination.name) {
        NewExaminationScreen(
            onScanQrCode = {
                navController.navigate(
                    ScaAppScreen.codeScannerRoute(
                        ExaminationFamilyMembersList.route
                    )
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium)),
        )
    }

    composable(route = ExaminationFamilyMembersList.route,
            arguments = listOf(
                navArgument(ExaminationFamilyMembersList.FAMILY_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
        val familyId = requireNotNull(
            backStackEntry.arguments?.getString(ExaminationFamilyMembersList.FAMILY_ID)
        )

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_EXAMINATION)
        }

        val viewModel: NewExaminationViewModel = viewModel(
            viewModelStoreOwner = parentEntry,
            factory = NewExaminationViewModel.provideFactory(
                familyId = familyId,
                providerName = providerName
            )
        )

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        ExaminationFamilyMembersListScreen(
            members = state.familyMembers,
            onMemberSelected = { policyHolderId ->
                navController.navigate(route = ExaminationPolicyHolderDetails.createRoute(policyHolderId))
            },
            onScanQrCode = {
                navController.navigate(
                    ScaAppScreen.codeScannerRoute(
                        ScaAppScreen.ExaminationFamilyMembersList
                    )
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
        val policyHolderId = backStackEntry.arguments?.getInt(ExaminationPolicyHolderDetails.POLICY_HOLDER_ID)

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_EXAMINATION)
        }

        val viewModel: NewExaminationViewModel = viewModel(
            viewModelStoreOwner = parentEntry
        )

        val localUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = localUiState.policyHolders.firstOrNull { it.id == policyHolderId }

        Log.d(TAG, "PolicyHolder: $policyHolder")

        // NOTE: do the setPolicyHolder in a LaunchedEffect to avoid repeated recompositions
        LaunchedEffect(policyHolder) {
            policyHolder?.let {
                viewModel.setPolicyHolder(it)
            }
        }

        if (policyHolder != null) {
            ExaminationPolicyHolderDetailsScreen(
                policyHolder = policyHolder,
                onDayExamination = {
                    navController.navigate(ScaAppScreen.ExaminationSameDayExamination.name)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }

    composable(route = ScaAppScreen.ExaminationSameDayExamination.name) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(NavGraphs.NEW_EXAMINATION)
        }

        val newExaminationViewModel: NewExaminationViewModel = viewModel(
            viewModelStoreOwner = parentEntry
        )

        val context = LocalContext.current
        val newExaminationUiState by newExaminationViewModel.uiState.collectAsStateWithLifecycle()
        val policyHolder = newExaminationUiState.currentPolicyHolder

        if (policyHolder != null) {
            val viewModel: ExaminationViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                key = "same_day_examination_${policyHolder.id}",
                factory = ExaminationViewModel.provideFactory(
                    providerName = providerName,
                    careCoverage = policyHolder.coverExternal,
                    userId = policyHolder.id.toString()
                )
            )

            // convert from model.events to nav_result
            LaunchedEffect(viewModel) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiEvent.Success -> {
                            navController
                                .getBackStackEntry(
                                    ScaAppScreen.HealthCareDashboard.name)
                                .savedStateHandle["nav_result"] = NavResult.NewSameDayExaminationSuccess.name

                            navController.navigate(NavGraphs.EXISTING_EXAMINATION) {
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
                NavResult.NewSameDayExaminationSuccess.name -> {
                    snackbarHostState.showSnackbar(
                        AppSnackbarVisuals(
                            message = context.getString(R.string.new_same_day_care_success_message),
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

        ExaminationListScreen(
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
