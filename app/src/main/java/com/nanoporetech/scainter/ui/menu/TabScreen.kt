package com.nanoporetech.scainter.ui.menu

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.AppUiState
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.ui.qrcode.CodeScannerScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationDetailsScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationFamilyMembersListScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationListScreen
import com.nanoporetech.scainter.ui.consultation.MedicalPrescriptionScreen
import com.nanoporetech.scainter.ui.consultation.NewConsultationScreen
import com.nanoporetech.scainter.ui.examination.ExaminationListScreen
import com.nanoporetech.scainter.ui.hospitalisation.HospitalisationListScreen
import com.nanoporetech.scainter.ui.support.SupportScreen
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme
import kotlinx.coroutines.launch
import kotlin.collections.forEach


private const val TAG = "TabScreen"

enum class ScaAppScreen(@StringRes val title: Int) {
    HealthCareDashboard(title = R.string.page_health_care),
    ConsultationList(title = R.string.page_consultation_list),
    ConsultationDetails(title = R.string.consultation_details_title),
    ConsultationNewPrescription(title = R.string.medical_prescription_title),
    ConsultationNewConsultation(title = R.string.new_consultation),

    ConsultationFamilyMembersList(title = R.string.new_consultation),
    ExaminationList(title = R.string.page_examination_list),
    ExaminationNewExamination(title = R.string.new_examination),
    HospitalisationList(title = R.string.page_hospitalisation_list),
    HospitalisationNewHospitalisation(title = R.string.new_hospitalisation),
    Support(title = R.string.page_support),
    CodeScanner(title = R.string.code_scanner_title)
}
private data class TabSpec(
    /** route is the destination route **/
    val route: String,
    /** label is the tab label **/
    val label: String,
    /** icon is the tab icon **/
    val icon: ImageVector
)

@Composable
fun TabScreen(
    uiState: AppUiState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit = {},
    onFetchConsultations: suspend () -> Boolean = { false },
    onFetchExaminations: suspend () -> Boolean = { false },
    onFetchHospitalisations: suspend () -> Boolean = { false }
) {
    val tabs = listOf(
        TabSpec(
            route = ScaAppScreen.HealthCareDashboard.name,
            label = stringResource(R.string.page_home),
            icon = Icons.Outlined.Home
        ),
        TabSpec(
            route = ScaAppScreen.Support.name,
            label = stringResource(R.string.page_support),
            icon = Icons.AutoMirrored.Outlined.HelpOutline
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ScaAppScreen.HealthCareDashboard.name
    val currentScreen = when {
        currentRoute.startsWith(ScaAppScreen.ConsultationDetails.name)  ->
            ScaAppScreen.ConsultationDetails
        else -> ScaAppScreen.entries.firstOrNull { it.name == currentRoute } ?: ScaAppScreen.HealthCareDashboard
    }
    var scanResult by rememberSaveable { mutableStateOf("") }

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
                    navController.popBackStack()
                },
                onLogout = onLogout,
            )
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
                composable(route = ScaAppScreen.HealthCareDashboard.name) {
                    HealthCareScreen(
                        provider = uiState.provider,
                        onViewConsultations = {
                            scope.launch {
                                val success = onFetchConsultations()
                                if (success) {
                                    navController.navigate(ScaAppScreen.ConsultationList.name)
                                }
                            }
                        },
                        onViewExaminations = {
                            scope.launch {
                                val success = onFetchExaminations()
                                if (success) {
                                    navController.navigate(ScaAppScreen.ExaminationList.name)
                                }
                            }
                        },
                        onViewHospitalisations = {
                            scope.launch {
                                val success = onFetchHospitalisations()
                                if (success) {
                                    navController.navigate(ScaAppScreen.HospitalisationList.name)
                                }
                            }
                        },
                        onNewConsultation = {
                            navController.navigate(ScaAppScreen.ConsultationNewConsultation.name)
                        },
                        onNewExamination = {
                            navController.navigate(ScaAppScreen.ExaminationNewExamination.name)
                        },
                        onNewHospitalisation = {
                            navController.navigate(ScaAppScreen.HospitalisationNewHospitalisation.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = ScaAppScreen.Support.name) {
                    SupportScreen(
                        onBack = {},
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                composable(route = ScaAppScreen.ConsultationList.name) {
                    ConsultationListScreen(
                        consultations = uiState.consultations,
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
                composable(route = ScaAppScreen.ExaminationList.name) {
                    ExaminationListScreen(
                        examinations = uiState.examinations,
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
                composable(route = ScaAppScreen.HospitalisationList.name) {
                    HospitalisationListScreen(
                        hospitalisations = uiState.hospitalisations,
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
                composable(
                    route = "${ScaAppScreen.ConsultationDetails.name}/{consultationId}",
                    arguments = listOf(
                        navArgument("consultationId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val consultationId = backStackEntry.arguments?.getInt("consultationId")
                    ConsultationDetailsScreen(
                        consultation = uiState.consultations.first { it.id == consultationId },
                        onNewPrescription = {
                            navController.navigate(ScaAppScreen.ConsultationNewPrescription.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = ScaAppScreen.ConsultationNewPrescription.name) {
                    MedicalPrescriptionScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = ScaAppScreen.ConsultationNewConsultation.name) {
                    NewConsultationScreen(
                        onScanQrCode = {
                            navController.navigate(ScaAppScreen.CodeScanner.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = AppConstants.lightGreen)
                            .padding(dimensionResource(R.dimen.padding_medium)),
                    )
                }
                composable(route = ScaAppScreen.CodeScanner.name) {
                    CodeScannerScreen(
                        onScanResult = {
                            scanResult = it
                            Log.d(TAG, "QR scan result: $scanResult")
                            navController.popBackStack()
                            navController.navigate(ScaAppScreen.ConsultationFamilyMembersList.name)
                        },
                        modifier = Modifier
                            .background(color = AppConstants.lightGreen)
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = ScaAppScreen.ConsultationFamilyMembersList.name) {
                    ConsultationFamilyMembersListScreen(
                        familyId = scanResult,
                        onMemberSelected = {
                            navController.navigate(ScaAppScreen.ConsultationDetails.name)
                        },
                        onScanQrCode = {
                            navController.navigate(ScaAppScreen.CodeScanner.name)
                        },
                        modifier = Modifier
                            .background(color = AppConstants.lightGreen)
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        }
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
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 6.dp,
            shadowElevation = 10.dp
        ) {
            NavigationBar(
                modifier = Modifier.widthIn(max = 300.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
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