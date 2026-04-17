package com.nanoporetech.scainter.ui.hospitalisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardRow
import com.nanoporetech.scainter.ui.examination.ExaminationCard
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

@Composable
fun HospitalisationCard(
    modifier: Modifier = Modifier,
    onNewHospitalisation: () -> Unit = {},
    onViewHospitalisation: () -> Unit = {},
) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = ScaInterTheme.extendedColors.mainGreen.color
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
        //.border(1.dp, color = Color.Red)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vertical_spacing_small)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            CardHeader(
                title = stringResource(R.string.hospitalisation_menu_title),
                iconImg = Icons.Filled.Bed,
                color = Color.White,
                modifier = Modifier
            )

            CardRow(
                title = stringResource(R.string.new_hospitalisation),
                iconImg = Icons.Outlined.Search,
                onClickButton = onNewHospitalisation
            )

            CardRow(
                title = stringResource(R.string.view_hospitalisations),
                iconImg = Icons.AutoMirrored.Outlined.Assignment,
                onClickButton = onViewHospitalisation
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun CommonUiPreview() {
    ScaInterAppTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            HospitalisationCard(
            )
        }
    }
}