package com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.StatusDivision
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Action

/**
 * A single status composable which show details.
 */

@ExperimentalCoilApi
@Composable
fun SingleStatus(
    singleStatus: StatusDivision
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(150.dp),
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            backgroundColor = Action,
            elevation = 4.dp,
        ) {
            Image(
                painter = rememberImagePainter(
                    data = singleStatus.userDetails.profilePic,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.image_description_network),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(142.dp),
                contentScale = ContentScale.Crop
            )
        }
        SizedBox(height = 5)
        Indicator(length = singleStatus.status.size)
    }
}