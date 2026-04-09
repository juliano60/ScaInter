package com.nanoporetech.scainter.ui

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

@Composable
fun TabScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.app_name)
        )

        Button(
            onClick = onLogout
        ) {
            Text("Log out")
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun LTabScreenPreview() {
    ScaInterAppTheme() {
        TabScreen(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .background(AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}