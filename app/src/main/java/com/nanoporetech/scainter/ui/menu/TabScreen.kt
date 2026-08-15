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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.AppUiState
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.ui.components.showAlert
import com.nanoporetech.scainter.ui.navigation.codeScannerNavigation
import com.nanoporetech.scainter.ui.navigation.consultationNavigation
import com.nanoporetech.scainter.ui.navigation.dashboardNavigation
import com.nanoporetech.scainter.ui.navigation.examinationNavigation
import com.nanoporetech.scainter.ui.navigation.hospitalisationNavigation
import com.nanoporetech.scainter.ui.navigation.supportNavigation
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme
import com.nanoporetech.scainter.ui.utils.AppSnackbarVisuals
import com.nanoporetech.scainter.ui.utils.SnackbarType


private const val TAG = "TabScreen"

enum class NavResult {
    NewConsultationSuccess,
    NewConsultationFailed,
    NewPrescriptionSuccess,
    NewSameDayExaminationSuccess,
}

enum class ScaAppScreen(@StringRes val title: Int) {
    HealthCareDashboard(title = R.string.health_care_title),
    ConsultationList(title = R.string.consultation_list_title),
    ConsultationDetails(title = R.string.consultation_details_title),
    ConsultationNewPrescription(title = R.string.medical_prescription_title),
    ConsultationNewConsultation(title = R.string.new_consultation),
    ConsultationFamilyMembersList(title = R.string.new_consultation),
    ConsultationPolicyHolderDetails(title = R.string.new_consultation),
    ExaminationList(title = R.string.examination_list_title),
    ExaminationDetails(title = R.string.examination_details_title),
    ExaminationNewExamination(title = R.string.new_examination),
    ExaminationFamilyMembersList(title = R.string.new_examination),
    ExaminationPolicyHolderDetails(title = R.string.new_examination),
    ExaminationSameDayExamination(title=R.string.exam_same_day_request_title),
    ExaminationRegularExamination(title=R.string.exam_regular_request_title),
    HospitalisationList(title = R.string.hospitalisation_list_title),
    HospitalisationDetails(title = R.string.hospitalisation_details_title),
    HospitalisationNewHospitalisation(title = R.string.new_hospitalisation),
    HospitalisationFamilyMembersList(title = R.string.new_hospitalisation),
    HospitalisationPolicyHolderDetails(title = R.string.new_examination),
    Support(title = R.string.about_title),
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
            label = stringResource(R.string.home_title),
            icon = Icons.Outlined.Home
        ),
        TabSpec(
            route = ScaAppScreen.Support.name,
            label = stringResource(R.string.about_title),
            icon = Icons.Outlined.Info
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ScaAppScreen.HealthCareDashboard.name
    val currentScreen = screenForRoute(currentRoute)

    var scanResult by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationPrompt by rememberSaveable { mutableStateOf(false) }
    var showConfirmationUpOneLevel by rememberSaveable { mutableStateOf(false) }
    var showExitPrompt by rememberSaveable { mutableStateOf(false) }


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
                        current?.startsWith(ScaAppScreen.ExaminationNewExamination.name) == true -> {
                            showConfirmationPrompt = true
                        }
                        current?.startsWith(ScaAppScreen.HospitalisationNewHospitalisation.name) == true -> {
                            showConfirmationPrompt = true
                        }
                        current?.startsWith(ScaAppScreen.ConsultationNewPrescription.name) == true -> {
                            showConfirmationUpOneLevel = true
                        }
                        current?.startsWith(ScaAppScreen.ExaminationSameDayExamination.name) == true -> {
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
                dashboardNavigation(
                    navController = navController,
                    provider = uiState.provider
                )

                supportNavigation()

                consultationNavigation(
                    navController = navController,
                    providerName = uiState.provider.name,
                    scanResult = scanResult,
                    snackbarHostState = snackbarHostState
                )

                examinationNavigation(
                    navController = navController,
                    providerName = uiState.provider.name,
                    scanResult = scanResult,
                    snackbarHostState = snackbarHostState
                )

                hospitalisationNavigation(
                    navController = navController,
                    providerName = uiState.provider.name,
                    scanResult = scanResult,
                    snackbarHostState = snackbarHostState
                )

                codeScannerNavigation(
                    navController = navController,
                    onResultChanged = {
                        scanResult = it
                        Log.d(TAG, "QR scan result: $scanResult")
                    }
                )
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

private fun screenForRoute(route: String): ScaAppScreen {
    return when {
        route.startsWith(ScaAppScreen.ConsultationDetails.name) ->
            ScaAppScreen.ConsultationDetails
        route.startsWith(ScaAppScreen.ExaminationDetails.name) ->
            ScaAppScreen.ExaminationDetails
        route.startsWith(ScaAppScreen.HospitalisationDetails.name) ->
            ScaAppScreen.HospitalisationDetails
        route.startsWith(ScaAppScreen.ConsultationPolicyHolderDetails.name) ->
            ScaAppScreen.ConsultationPolicyHolderDetails
        route.startsWith(ScaAppScreen.ExaminationPolicyHolderDetails.name) ->
            ScaAppScreen.ExaminationPolicyHolderDetails
        route.startsWith(ScaAppScreen.HospitalisationPolicyHolderDetails.name) ->
            ScaAppScreen.HospitalisationPolicyHolderDetails
        else ->
            ScaAppScreen.entries.firstOrNull { it.name == route }
                ?: ScaAppScreen.HealthCareDashboard
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
