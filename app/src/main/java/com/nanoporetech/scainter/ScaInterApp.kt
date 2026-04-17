package com.nanoporetech.scainter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.AppViewModel
import com.nanoporetech.scainter.ui.AppViewModelFactory
import com.nanoporetech.scainter.ui.login.LoginScreen
import com.nanoporetech.scainter.ui.TabScreen
import com.nanoporetech.scainter.ui.UiEvent
import com.nanoporetech.scainter.ui.login.ForgottenPasswordScreen

enum class ScaDestination {
    LoginScreen,
    ForgottenPasswordScreen,
    TabScreen
}

@Composable
fun ScaInterApp(
    navController: NavHostController = rememberNavController(),
    model: AppViewModel = viewModel(
        factory = AppViewModelFactory(LocalContext.current)
    )
) {
    val uiState by model.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        model.events.collect { event ->
            when (event) {
                UiEvent.Success -> {
                    navController.navigate(route = ScaDestination.TabScreen.name) {
                        // remove LoginScreen from the stack
                        popUpTo(ScaDestination.LoginScreen.name) { inclusive = true }
                    }
                }
                UiEvent.Logout -> {
                    navController.navigate(route = ScaDestination.LoginScreen.name)
                }
                is UiEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.errorId),
                        withDismissAction = true
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = AppConstants.lightGreen,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = ScaDestination.LoginScreen.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(ScaDestination.LoginScreen.name) {
                LoginScreen(
                    username = model.username,
                    onUsernameChanged = {
                        model.username = it
                    },
                    password = model.password,
                    onPasswordChanged = {
                        model.password = it
                    },
                    onLogin = {
                        model.login()
                    },
                    isLoginError = uiState.isLoginError,
                    onForgottenPassword = {
                        navController.navigate(route = ScaDestination.ForgottenPasswordScreen.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppConstants.lightGreen)
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(ScaDestination.ForgottenPasswordScreen.name) {
                ForgottenPasswordScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(ScaDestination.TabScreen.name) {
                TabScreen(
                    onLogout = {
                        model.logout()
                    },
                    uiState = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
