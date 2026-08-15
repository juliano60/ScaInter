package com.nanoporetech.scainter.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nanoporetech.scainter.ui.menu.ScaAppScreen
import com.nanoporetech.scainter.ui.support.SupportScreen

fun NavGraphBuilder.supportNavigation(

) {
    composable(route = ScaAppScreen.Support.name) {
        SupportScreen(
            onBack = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}