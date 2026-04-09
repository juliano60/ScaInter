package com.nanoporetech.scainter.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Whatsapp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgottenPasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            ScaTopAppBar(
                onBack = onBack
            )
        },
        containerColor = AppConstants.lightGreen
    ) {  innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {

                    CardHeader(
                        title = stringResource(R.string.sca_technical_support),
                        iconImg = Icons.Outlined.HeadsetMic,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = stringResource(R.string.forgotten_password_text),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(R.string.contact_hours),
                        color = MaterialTheme.colorScheme.primary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Whatsapp,
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_small)),
                            tint = Color(0xFF2E7D32) // green
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                        Text(
                            "(+225) 0722446688",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaTopAppBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.forgotten_password_title)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        modifier = modifier
    )
}


@Preview
@Composable
fun ForgottenPasswordScreenPreview() {
    ScaInterAppTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ForgottenPasswordScreen(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}