package com.nanoporetech.scainter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

enum class ScaDestination {
    Login,
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

    LaunchedEffect(Unit) {
        model.events.collect { event ->
            when (event) {
                UiEvent.Success -> {
                    navController.navigate(ScaDestination.TabScreen.name) {
                        // remove LoginScreen from the stack
                        popUpTo(ScaDestination.Login.name) { inclusive = true }
                    }
                }
                else -> {
                    // display snack
                }
            }
        }
    }

    Scaffold(
        containerColor = AppConstants.lightGreen
    ) { innerPadding ->


        val isLoggedIn = uiState.isLoggedIn

        NavHost(
            navController = navController,
            startDestination = ScaDestination.Login.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(ScaDestination.Login.name) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            composable(ScaDestination.TabScreen.name) {
                TabScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}
