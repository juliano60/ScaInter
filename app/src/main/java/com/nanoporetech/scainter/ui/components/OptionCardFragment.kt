package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

@Composable
fun OptionCard(
    iconImg: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    foregroundColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Icon(
                painter = iconImg,
                contentDescription = null,
                tint = foregroundColor
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))

            Column(modifier = Modifier
                .weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,

                    color = foregroundColor
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,

                tint = foregroundColor
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun OptionCardPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                OptionCard(
                    iconImg = painterResource(R.drawable.ecg_heart),
                    title = stringResource(R.string.add_same_day_examination_button),
                    onClick = {},
                    modifier = Modifier
                        .wrapContentHeight(),
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    foregroundColor = ScaInterTheme.extendedColors.mainGreen.color
                )

                OptionCard(
                    iconImg = painterResource(R.drawable.ecg_heart),
                    title = stringResource(R.string.add_same_day_examination_button),
                    onClick = {},
                    modifier = Modifier
                        .wrapContentHeight(),
                    backgroundColor = ScaInterTheme.extendedColors.mainGreen.color,
                    foregroundColor = Color.White
                )
            }

        }
    }
}