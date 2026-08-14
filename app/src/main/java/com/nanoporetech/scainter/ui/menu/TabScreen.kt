package com.nanoporetech.scainter.ui.menu

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.AppUiState
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.ui.components.showAlert
import com.nanoporetech.scainter.ui.consultation.ConsultationDetailsScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationFamilyMembersListRoute
import com.nanoporetech.scainter.ui.consultation.ConsultationListScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationNewPrescriptionScreen
import com.nanoporetech.scainter.ui.consultation.ListConsultationsViewModel
import com.nanoporetech.scainter.ui.consultation.NewConsultationScreen
import com.nanoporetech.scainter.ui.consultation.NewConsultationViewModel
import com.nanoporetech.scainter.ui.consultation.ConsultationPolicyHolderDetailsScreen
import com.nanoporetech.scainter.ui.events.UiEvent
import com.nanoporetech.scainter.ui.examination.ExaminationFamilyMembersListRoute
import com.nanoporetech.scainter.ui.examination.ExaminationListScreen
import com.nanoporetech.scainter.ui.examination.ExaminationPolicyHolderDetailsScreen
import com.nanoporetech.scainter.ui.examination.ExaminationSameDayExamScreen
import com.nanoporetech.scainter.ui.examination.ExaminationViewModel
import com.nanoporetech.scainter.ui.examination.NewExaminationScreen
import com.nanoporetech.scainter.ui.examination.NewExaminationViewModel
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationFamilyMembersListRoute
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationListScreen
import com.nanoporetech.scainter.ui.hospitalisation.NewHospitalisationScreen
import com.nanoporetech.scainter.ui.qrcode.CodeScannerScreen
import com.nanoporetech.scainter.ui.support.SupportScreen
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme
import com.nanoporetech.scainter.ui.utils.AppSnackbarVisuals
import com.nanoporetech.scainter.ui.utils.SnackbarType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "TabScreen"
private const val CONSULTATION_ID_ARGUMENT = "consultationId"
private const val EXAMINATION_ID_ARGUMENT = "examinationId"
private const val HOSPITALISATION_ID_ARGUMENT = "hospitalisationId"

enum class NavResult {
    NewConsultationSuccess,
    NewConsultationFailed,
    NewPrescriptionSuccess,
}

enum class ScaAppScreen(@StringRes val title: Int) {
    HealthCareDashboard(title = R.string.page_health_care),
    ConsultationList(title = R.string.page_consultation_list),
    ConsultationDetails(title = R.string.consultation_details_title),
    ConsultationNewPrescription(title = R.string.medical_prescription_title),
    ConsultationNewConsultation(title = R.string.new_consultation),
    ConsultationFamilyMembersList(title = R.string.new_consultation),
    ConsultationPolicyHolderDetails(title = R.string.new_consultation),
    ExaminationList(title = R.string.page_examination_list),
    ExaminationNewExamination(title = R.string.new_examination),
    ExaminationFamilyMembersList(title = R.string.new_examination),
    ExaminationPolicyHolderDetails(title = R.string.new_examination),
    ExaminationSameDayExamination(title=R.string.exam_same_day_request_title),
    ExaminationRegularExamination(title=R.string.exam_regular_request_title),
    HospitalisationList(title = R.string.page_hospitalisation_list),
    HospitalisationNewHospitalisation(title = R.string.new_hospitalisation),
    HospitalisationFamilyMembersList(title = R.string.new_hospitalisation),
    HospitalisationPolicyHolderDetails(title = R.string.new_examination),
    Support(title = R.string.page_about),
    CodeScanner(title = R.string.code_scanner_title);

    companion object {
        const val RETURN_TO_ARGUMENT = "returnTo"
        val codeScannerRoute = "${CodeScanner.name}/{$RETURN_TO_ARGUMENT}"
        fun codeScannerRoute(returnTo: ScaAppScreen): String =
            "${CodeScanner.name}/${returnTo.name}"
    }
}
private data class TabSpec(
    /** route is the destination route **/
    val route: String,
    /** label is the tab label **/
    val label: String,
    /** icon is the tab icon **/
    val icon: ImageVector
)

object NavGraphs {
    const val NEW_CONSULTATION = "new_consultation_flow"
    const val EXISTING_CONSULTATION = "existing_consultation_flow"

    const val NEW_EXAMINATION = "new_examination_flow"
    const val EXISTING_EXAMINATION = "existing_examination_flow"

    const val NEW_HOSPITALISATION = "new_hospitalisation_flow"
    const val EXISTING_HOSPITALISATION = "existing_hospitalisation_flow"
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun TabScreen(
    uiState: AppUiState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit = {},
) {
    val tabs = listOf(
        TabSpec(
            route = ScaAppScreen.HealthCareDashboard.name,
            label = stringResource(R.string.page_home),
            icon = Icons.Outlined.Home
        ),
        TabSpec(
            route = ScaAppScreen.Support.name,
            label = stringResource(R.string.page_about),
            icon = Icons.Outlined.Info
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ScaAppScreen.HealthCareDashboard.name
    val currentScreen = when {
        currentRoute.startsWith(ScaAppScreen.ConsultationDetails.name) ->
            ScaAppScreen.ConsultationDetails
        currentRoute.startsWith(ScaAppScreen.ConsultationPolicyHolderDetails.name) ->
            ScaAppScreen.ConsultationPolicyHolderDetails
        currentRoute.startsWith(ScaAppScreen.ExaminationPolicyHolderDetails.name) ->
            ScaAppScreen.ExaminationPolicyHolderDetails
        currentRoute.startsWith(ScaAppScreen.HospitalisationPolicyHolderDetails.name) ->
            ScaAppScreen.HospitalisationPolicyHolderDetails
        else -> ScaAppScreen.entries.firstOrNull { it.name == currentRoute } ?: ScaAppScreen.HealthCareDashboard
    }
    var scanResult by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showConfirmationPrompt by rememberSaveable { mutableStateOf(false) }
    var showConfirmationUpOneLevel by rememberSaveable { mutableStateOf(false) }
    var showExitPrompt by rememberSaveable { mutableStateOf(false) }
    val consultationDetailsRoute = "${ScaAppScreen.ConsultationDetails.name}/{$CONSULTATION_ID_ARGUMENT}"

    fun onTabPressed(route: String) {
        navController.navigate(route) {
            popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                // keep HealthCareScreen
                inclusive = false
            }
            // prevent duplication
            launchSingleTop = true
        }
    }

    Scaffold(
        topBar = {
            ScaTopBar(
                currentScreen = currentScreen,
                showBackButton = navController.previousBackStackEntry != null,
                onNavigateUp = {
                    val current = backStackEntry?.destination?.route

                    when {
                        current?.startsWith(ScaAppScreen.ConsultationNewConsultation.name) == true -> {
                            showConfirmationPrompt = true
                        }
                        current?.startsWith(ScaAppScreen.ConsultationNewPrescription.name) == true -> {
                            showConfirmationUpOneLevel = true
                        }
                        current?.startsWith(ScaAppScreen.CodeScanner.name) == true -> {
                            showConfirmationPrompt = true
                        }
                        else -> navController.popBackStack()
                    }
                },
                onLogout = { showExitPrompt = true },
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                val visuals = data.visuals as? AppSnackbarVisuals

                Snackbar(
                    containerColor = when (visuals?.type) {
                        SnackbarType.Success -> Color(0xFF2E7D32)
                        SnackbarType.Error -> MaterialTheme.colorScheme.error
                        null -> MaterialTheme.colorScheme.inverseSurface
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        },
        bottomBar = {
            DockBottomNavigationBar(
                currentRoute = currentRoute,
                tabs = tabs,
                onTabPressed = { onTabPressed(it) },
            )
        },
        modifier = modifier
    ) { innerPadding ->

        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScaInterTheme.extendedColors.lightGreen.color)
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = ScaAppScreen.HealthCareDashboard.name,
                modifier = Modifier
            ) {
                // DASHBOARD VIEW

                composable(route = ScaAppScreen.HealthCareDashboard.name) {
                    HealthCareScreen(
                        provider = uiState.provider,
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

                // SUPPORT VIEW

                composable(route = ScaAppScreen.Support.name) {
                    SupportScreen(
                        onBack = {},
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                // CONSULTATION FLOWS

                navigation(
                    route = NavGraphs.NEW_CONSULTATION,
                    startDestination = ScaAppScreen.ConsultationNewConsultation.name
                ) {
                    composable(route = ScaAppScreen.ConsultationNewConsultation.name) {
                        NewConsultationScreen(
                            onScanQrCode = {
                                navController.navigate(
                                    ScaAppScreen.codeScannerRoute(
                                        ScaAppScreen.ConsultationFamilyMembersList
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = AppConstants.lightGreen)
                                .padding(dimensionResource(R.dimen.padding_medium)),
                        )
                    }

                    composable(route = ScaAppScreen.ConsultationFamilyMembersList.name) {
                        ConsultationFamilyMembersListRoute(
                            familyId = scanResult,
                            providerName = uiState.provider.name,
                            onMemberSelected = { policyHolderId ->
                                navController.navigate(route = "${ScaAppScreen.ConsultationPolicyHolderDetails.name}/${policyHolderId}")
                            },
                            onScanQrCode = {
                                navController.navigate(
                                    ScaAppScreen.codeScannerRoute(
                                        ScaAppScreen.ConsultationFamilyMembersList
                                    )
                                )
                            },
                            modifier = Modifier
                                .background(color = AppConstants.lightGreen)
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }

                    composable(
                        route = "${ScaAppScreen.ConsultationPolicyHolderDetails.name}/{policyHolderId}",
                        arguments = listOf(
                            navArgument("policyHolderId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val policyHolderId = backStackEntry.arguments?.getInt("policyHolderId")

                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(NavGraphs.NEW_CONSULTATION)
                        }

                        val viewModel: NewConsultationViewModel = viewModel(
                            viewModelStoreOwner = parentEntry
                        )

                        val uiState by viewModel.uiState.collectAsState()
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

                navigation(
                    route = NavGraphs.EXISTING_CONSULTATION,
                    startDestination = ScaAppScreen.ConsultationList.name
                ) {
                    composable(route = ScaAppScreen.ConsultationList.name) { backStackEntry ->
                        val dashboardEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(ScaAppScreen.HealthCareDashboard.name)
                        }
                        val dashboardNavResult by dashboardEntry
                            .savedStateHandle
                            .getStateFlow<String?>("nav_result", null)
                            .collectAsState()

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

                        ConsultationListScreen(
                            providerName = uiState.provider.name,
                            onRowClick = { consultation ->
                                navController.navigate(
                                    route = "${ScaAppScreen.ConsultationDetails.name}/${consultation.id}"
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.padding_medium))
                        )
                    }

                    composable(
                        route = consultationDetailsRoute,
                        arguments = listOf(
                            navArgument(CONSULTATION_ID_ARGUMENT) {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val consultationId = requireNotNull(
                            backStackEntry.arguments?.getInt(CONSULTATION_ID_ARGUMENT)
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

                        val uiState by viewModel.uiState.collectAsState()

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
                            navController.getBackStackEntry(consultationDetailsRoute)
                        }

                        val consultationId = requireNotNull(
                            parentEntry.arguments?.getInt(CONSULTATION_ID_ARGUMENT)
                        ).toString()

                        ConsultationNewPrescriptionScreen(
                            consultationId = consultationId,
                            onSubmitSuccess = {
                                navController
                                    .getBackStackEntry(
                                        ScaAppScreen.HealthCareDashboard.name)
                                    .savedStateHandle["nav_result"] = NavResult.NewPrescriptionSuccess.name

                                navController.navigate(NavGraphs.EXISTING_CONSULTATION) {
                                    popUpTo(ScaAppScreen.HealthCareDashboard.name) {
                                        inclusive = false
                                    }
                                }
                            },
                            onSubmitError = { errorId ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        AppSnackbarVisuals(
                                            message = context.getString(errorId),
                                            type = SnackbarType.Error,
                                            duration = SnackbarDuration.Long
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = AppConstants.lightGreen)
                                .padding(dimensionResource(R.dimen.padding_medium)),
                        )
                    }
                }

                // EXAMINATION FLOWS

                navigation(
                    route = NavGraphs.NEW_EXAMINATION,
                    startDestination = ScaAppScreen.ExaminationNewExamination.name
                ) {
                    composable(route = ScaAppScreen.ExaminationNewExamination.name) {
                        NewExaminationScreen(
                            onScanQrCode = {
                                navController.navigate(
                                    ScaAppScreen.codeScannerRoute(
                                        ScaAppScreen.ExaminationFamilyMembersList
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = AppConstants.lightGreen)
                                .padding(dimensionResource(R.dimen.padding_medium)),
                        )
                    }

                    composable(route = ScaAppScreen.ExaminationFamilyMembersList.name) {
                        ExaminationFamilyMembersListRoute(
                            familyId = scanResult,
                            providerName = uiState.provider.name,
                            onMemberSelected = { policyHolderId ->
                                navController.navigate(route = "${ScaAppScreen.ExaminationPolicyHolderDetails.name}/${policyHolderId}")
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
                        route = "${ScaAppScreen.ExaminationPolicyHolderDetails.name}/{policyHolderId}",
                        arguments = listOf(
                            navArgument("policyHolderId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val policyHolderId = backStackEntry.arguments?.getInt("policyHolderId")

                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry(NavGraphs.NEW_EXAMINATION)
                        }

                        val viewModel: NewExaminationViewModel = viewModel(
                            viewModelStoreOwner = parentEntry
                        )

                        val localUiState by viewModel.uiState.collectAsState()
                        val policyHolder = localUiState.policyHolders.firstOrNull { it.id == policyHolderId }

                        Log.d(TAG, "PolicyHolder: $policyHolder")

                        if (policyHolder != null) {
                            viewModel.setPolicyHolder(policyHolder)

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

                        val newExaminationUiState by newExaminationViewModel.uiState.collectAsState()
                        val policyHolder = newExaminationUiState.currentPolicyHolder

                        if (policyHolder != null) {
                            val viewModel: ExaminationViewModel = viewModel(
                                viewModelStoreOwner = parentEntry,
                                key = "same_day_examination_${policyHolder.id}",
                                factory = ExaminationViewModel.provideFactory(
                                    providerName = uiState.provider.name,
                                    careCoverage = policyHolder.coverExternal,
                                    userId = policyHolder.id.toString()
                                )
                            )

                            val localUiState by viewModel.uiState.collectAsState()

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

                navigation(
                    route = NavGraphs.EXISTING_EXAMINATION,
                    startDestination = ScaAppScreen.ExaminationList.name
                ) {
                    composable(route = ScaAppScreen.ExaminationList.name) {
                        ExaminationListScreen(
                            providerName = uiState.provider.name,
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

                // HOSPITALISATION FLOWS

                navigation(
                    route = NavGraphs.NEW_HOSPITALISATION,
                    startDestination = ScaAppScreen.HospitalisationNewHospitalisation.name
                ) {
                    composable(route = ScaAppScreen.HospitalisationNewHospitalisation.name) {
                        NewHospitalisationScreen(
                            onScanQrCode = {
                                navController.navigate(
                                    ScaAppScreen.codeScannerRoute(
                                        ScaAppScreen.HospitalisationFamilyMembersList
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = AppConstants.lightGreen)
                                .padding(dimensionResource(R.dimen.padding_medium)),
                        )
                    }

                    composable(route = ScaAppScreen.HospitalisationFamilyMembersList.name) {
                        HospitalisationFamilyMembersListRoute(
                            familyId = scanResult,
                            providerName = uiState.provider.name,
                            onMemberSelected = { policyHolderId ->
                                navController.navigate(route = "${ScaAppScreen.HospitalisationPolicyHolderDetails.name}/${policyHolderId}")
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

                navigation(
                    route = NavGraphs.EXISTING_HOSPITALISATION,
                    startDestination = ScaAppScreen.HospitalisationList.name
                ) {
                    composable(route = ScaAppScreen.HospitalisationList.name) {
                        HospitalisationListScreen(
                            providerName = uiState.provider.name,
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

                // CODE SCANNER

                composable(
                    route = ScaAppScreen.codeScannerRoute,
                    arguments = listOf(
                        navArgument(ScaAppScreen.RETURN_TO_ARGUMENT) {
                            type = NavType.StringType
                        }
                    )) { backStackEntry ->

                    val returnTo = backStackEntry.arguments
                        ?.getString(ScaAppScreen.RETURN_TO_ARGUMENT)
                        ?: return@composable

                    CodeScannerScreen(
                        onScanResult = {
                            scanResult = it
                            Log.d(TAG, "QR scan result: $scanResult")
                            navController.popBackStack()
                            navController.navigate(returnTo) {
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .background(color = AppConstants.lightGreen)
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        }
    }

    // navigate up one level
    if (showConfirmationUpOneLevel) {
        showAlert(
            title = R.string.confirmation_generic_title,
            message = R.string.confirmation_abort_prompt,
            onDismiss = { showConfirmationUpOneLevel = false },
            onConfirm = {
                showConfirmationUpOneLevel = false
                navController.popBackStack()
            }
        )
    }

    // navigate up to main menu
    if (showConfirmationPrompt) {
        showAlert(
            title = R.string.confirmation_generic_title,
            message = R.string.confirmation_abort_prompt,
            onDismiss = { showConfirmationPrompt = false },
            onConfirm = {
                showConfirmationPrompt = false
                navController.popBackStack(
                    ScaAppScreen.HealthCareDashboard.name,
                    inclusive = false
                )
            }
        )
    }
    if (showExitPrompt) {
        showAlert(
            title = R.string.confirmation_generic_title,
            message = R.string.confirmation_exit_prompt,
            onDismiss = { showExitPrompt = false },
            onConfirm = {
                showExitPrompt = false
                onLogout()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaTopBar(
    currentScreen: ScaAppScreen,
    onNavigateUp: () -> Unit,
    onLogout: () -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        windowInsets = WindowInsets.statusBars,
        actions = {
            IconButton(
                onClick = onLogout
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = stringResource(R.string.logout)
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
private fun DockBottomNavigationBar(
    currentRoute: String,
    tabs: List<TabSpec>,
    onTabPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .offset(y = (-8).dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 6.dp,
                shadowElevation = 10.dp,
            ) {
                NavigationBar(
                    modifier = Modifier.widthIn(max = 300.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0, 0, 0, 0),
                ) {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = when (tab.route) {
                                ScaAppScreen.HealthCareDashboard.name ->
                                    currentRoute != ScaAppScreen.Support.name
                                else -> currentRoute == tab.route
                            },
                            onClick = { onTabPressed(tab.route) },
                            icon = { Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(28.dp)
                            )},
                            label = { Text(tab.label) },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun TabScreenPreview() {
    ScaInterAppTheme() {
        TabScreen(
            uiState = AppUiState(
                isLoggedIn = true,
                provider = DataSource.healthProviders().first()
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
