package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardHeaderDrawable
import com.nanoporetech.scainter.ui.components.CardRow
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

@Composable
fun ConsultationCard(
    modifier: Modifier = Modifier,
    onNewConsultation: () -> Unit = {},
    onViewConsultations: () -> Unit = {},
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
            CardHeaderDrawable(
                title = stringResource(R.string.consultation_menu_title),
                iconImg = painterResource(R.drawable.stethoscope),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                modifier = Modifier
            )

            CardRow(
                title = stringResource(R.string.new_care_sheet),
                iconImg = Icons.Outlined.Search,
                onClickButton = onNewConsultation
            )

            CardRow(
                title = stringResource(R.string.view_care_sheet),
                iconImg = Icons.AutoMirrored.Outlined.Assignment,
                onClickButton = onViewConsultations
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun ConsultationCardPreview() {
    ScaInterAppTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            ConsultationCard(
            )
        }
    }
}