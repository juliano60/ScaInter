package com.nanoporetech.scainter.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.nanoporetech.scainter.ui.consultation.ConsultationDetailsScreen
import com.nanoporetech.scainter.ui.consultation.ConsultationListScreen
import com.nanoporetech.scainter.ui.consultation.MedicalPrescriptionContent
import com.nanoporetech.scainter.ui.consultation.MedicalPrescriptionScreen
import com.nanoporetech.scainter.ui.support.SupportScreen
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme
import kotlinx.coroutines.launch
import kotlin.collections.forEach


enum class ScaAppScreen(@StringRes val title: Int) {
    HealthCareScreen(title = R.string.page_health_care),
    ConsultationListScreen(title = R.string.page_consultation_list),
    ConsultationDetailsScreen(title = R.string.consultation_details_title),
    ConsultationNewPrescriptionScreen(title = R.string.medical_prescription_title),
    SupportScreen(title = R.string.page_support)
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
) {
    val tabs = listOf(
        TabSpec(
            route = ScaAppScreen.HealthCareScreen.name,
            label = stringResource(R.string.page_home),
            icon = Icons.Outlined.Home
        ),
        TabSpec(
            route = ScaAppScreen.SupportScreen.name,
            label = stringResource(R.string.page_support),
            icon = Icons.AutoMirrored.Outlined.HelpOutline
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ScaAppScreen.HealthCareScreen.name
    val currentScreen = when {
        currentRoute.startsWith(ScaAppScreen.ConsultationDetailsScreen.name)  ->
            ScaAppScreen.ConsultationDetailsScreen
        else -> ScaAppScreen.entries.firstOrNull { it.name == currentRoute } ?: ScaAppScreen.HealthCareScreen
    }
    val currentTab = tabs.find { it.route == currentRoute }

    fun onTabPressed(route: String) {
        navController.navigate(route) {
            // do not create a copy if at top of the stack
            launchSingleTop = true
            // restore previous state if visited before
            restoreState = true
            // pop everything above start destination
            popUpTo(tabs.first().route) { saveState = true }
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
                startDestination = ScaAppScreen.HealthCareScreen.name,
                modifier = Modifier
            ) {
                composable(route = ScaAppScreen.HealthCareScreen.name) {
                    HealthCareScreen(
                        provider = uiState.provider,
                        onViewConsultations = {
                            scope.launch {
                                val success = onFetchConsultations()
                                if (success) {
                                    navController.navigate(ScaAppScreen.ConsultationListScreen.name)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = ScaAppScreen.SupportScreen.name) {
                    SupportScreen(
                        onBack = {},
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                composable(route = ScaAppScreen.ConsultationListScreen.name) {
                    ConsultationListScreen(
                        consultations = uiState.consultations,
                        onRowClick = { consultation ->
                            navController.navigate(
                                route = "${ScaAppScreen.ConsultationDetailsScreen.name}/${consultation.id}"
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(
                    route = "${ScaAppScreen.ConsultationDetailsScreen.name}/{consultationId}",
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
                            navController.navigate(ScaAppScreen.ConsultationNewPrescriptionScreen.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
                composable(route = ScaAppScreen.ConsultationNewPrescriptionScreen.name) {
                    MedicalPrescriptionScreen(
                        modifier = Modifier
                            .fillMaxSize()
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
                        selected = currentRoute == tab.route,
                        onClick = { onTabPressed(tab.route) },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
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