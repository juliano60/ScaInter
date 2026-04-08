package com.nanoporetech.scainter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainter.ui.AppViewModel

enum class ScaDestination {
    Login,
    TabScreen
}

@Composable
fun ScaInterApp(
    navController: NavHostController = rememberNavController(),
    model: AppViewModel = viewModel()
) {

    val isLoggedIn = false

    Scaffold(

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) ScaDestination.TabScreen.name else ScaDestination.Login.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {

        }
    }
}
