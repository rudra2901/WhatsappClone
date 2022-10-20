package com.gdsc.nitb.rudra.whatsappclone.modules.home.view.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black15Bold

/**
 * A home app bar which will be a composable used at the top of home screen.
 */

@Composable
fun HomeAppBar(title: String, profile: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = black15Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.9f)
                )
                IconButton(
                    onClick = {
                        profile()
                    }
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = stringResource(id = R.string.image_description),
                        modifier = Modifier.size(25.dp),
                        tint = Color.Gray
                    )
                }
            }
        },
        elevation = 0.dp,
        backgroundColor = Color.White,
    )
}
