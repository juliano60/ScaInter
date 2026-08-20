package com.nanoporetech.scainter.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.qrcode.CodeScannerScreen

const val SCAN_RESULT = "scan_result"
const val FAMILY_ID = "familyId"

fun NavGraphBuilder.codeScannerNavigation(
    navController: NavController,
) {
    composable(route = ScaAppScreen.CodeScanner.name) {
        CodeScannerScreen(
            onScanResult = { scannedValue ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(SCAN_RESULT, scannedValue)

                navController.popBackStack()
            },
            modifier = Modifier
                .background(color = AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
