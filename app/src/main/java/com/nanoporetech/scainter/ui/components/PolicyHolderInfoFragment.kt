package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

@Composable
fun PolicyHolderInfoFragment(
    imageUrl: String,
    name: String,
    internalId: String,
    subscriberName: String,
    coverPercent: String,
    contractType: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        PolicyHolderAvatar(
            imgUrl = imageUrl,
            modifier = Modifier
            //.border(1.dp, color = Color.Yellow)
        )

        Spacer(modifier.height(dimensionResource(R.dimen.padding_medium)))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (internalId.isNotBlank()) {
                Text(
                    text = internalId,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = stringResource(R.string.coverage_label_and_value, coverPercent),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            Text(
                text = "$subscriberName ($contractType)",
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun PolicyHolderAvatar(
    imgUrl: String,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(R.dimen.profile_icon_size)),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.profile_image),
            contentScale = ContentScale.Crop,
            loading = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )
            },
            error = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )
            },
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun PolicyHolderInfoFragmentPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            PolicyHolderInfoFragment(
                name = "Test1",
                internalId = "Internal ID 1",
                imageUrl = "url1",
                subscriberName = "Subscriber 1",
                coverPercent = "100%",
                contractType = "Contract Type 1",
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}