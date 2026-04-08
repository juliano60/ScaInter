package com.nanoporetech.scainter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.AppViewModel
import com.nanoporetech.scainter.ui.LoginScreen
import com.nanoporetech.scainter.ui.TabScreen

enum class ScaDestination {
    Login,
    TabScreen
}

@Composable
fun ScaInterApp(
    navController: NavHostController = rememberNavController(),
    model: AppViewModel = viewModel()
) {

    Scaffold(
        containerColor = AppConstants.lightGreen
    ) { innerPadding ->

        val uiState by model.uiState.collectAsState()
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

                        if (isLoggedIn) {
                            navController.navigate(route = ScaDestination.TabScreen.name)
                        }
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
