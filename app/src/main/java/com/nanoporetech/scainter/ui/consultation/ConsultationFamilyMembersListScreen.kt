package com.nanoporetech.scainter.ui.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.FamilyMember
import com.nanoporetech.scainter.ui.components.SubHeader
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

@Composable
fun ConsultationFamilyMembersListScreen(
    familyId: String,
    modifier: Modifier = Modifier,
    viewModel: NewConsultationViewModel = viewModel(
        factory = NewConsultationViewModel.provideFactory(familyId)
    ),
    onMemberSelected: () -> Unit = {},
    onScanQrCode: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    ConsultationFamilyMembersListContent(
        members = state.familyMembers,
        onMemberSelected = onMemberSelected,
        onScanQrCode = onScanQrCode,
        modifier = modifier
    )
}

@Composable
fun ConsultationFamilyMembersListContent(
    members: List<FamilyMember>,
    modifier: Modifier = Modifier,
    onMemberSelected: () -> Unit = {},
    onScanQrCode: () -> Unit = {},
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                //.border(1.dp, color = Color.Red)
        ) {
            Card(
                shape = RectangleShape,
                onClick = { onScanQrCode() },
                colors = CardDefaults.cardColors(
                    containerColor = AppConstants.lightGreen
                ),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.scanner_icon_size))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCodeScanner,
                        contentDescription = stringResource(R.string.scan_qr_code),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(80.dp)
                    )
                }
            }

            Text(
                text = stringResource(R.string.scan_qr_code),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        // SUB HEADER SECTION
        SubHeader(
            title = stringResource(R.string.policy_members),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // FAMILY MEMBERS
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(members) { member ->
                MemberRowItem(
                    member = member,
                    onMemberSelected = onMemberSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
    }
}

@Composable
fun MemberRowItem(
    member: FamilyMember,
    modifier: Modifier = Modifier,
    onMemberSelected: () -> Unit = {},
) {
    Card(
        shape = RectangleShape,
        onClick = onMemberSelected,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))

                    Text(
                        text = member.fullname,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = member.link,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier
                    .weight(0.1f)
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun ConsultationFamilyMembersListContentPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ConsultationFamilyMembersListContent(
                members = DataSource.policyHolderFamilyMembers(),
                modifier = Modifier
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}