package com.nanoporetech.scainter.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.qrcode.CodeScannerScreen


fun NavGraphBuilder.codeScannerNavigation(
    navController: NavController,
    onResultChanged: (String) -> Unit,
) {
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
                onResultChanged(it)

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