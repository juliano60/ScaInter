package com.nanoporetech.scainter.ui

import android.R.attr.fontWeight
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.theme.ExtendedColorScheme
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(modifier) {
        // HEADER SECTION
        HeaderAndLogo(

        )

        Spacer(modifier = Modifier.height(mediumPadding))

        // WELCOME MESSAGE SECTION
    }
}

@Composable
fun HeaderAndLogo(
    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = Color.Red)
    ) {
        Image(
            painter = painterResource(R.drawable.sca_logo_no_title),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_large))
        )

        Spacer(modifier = Modifier.width(mediumPadding))

        Text(
            text = stringResource(R.string.company_name),
            style = MaterialTheme.typography.displaySmall,
            color = ScaInterTheme.extendedColors.mainGreen.color,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ScaInterAppTheme() {
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}