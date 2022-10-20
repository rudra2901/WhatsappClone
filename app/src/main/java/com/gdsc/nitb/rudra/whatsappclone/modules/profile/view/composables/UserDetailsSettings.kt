package com.gdsc.nitb.rudra.whatsappclone.modules.profile.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.modules.profile.viewModel.ProfileViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.gray15
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.link15
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.red30Bold

/**
 * User details composable which will be used to show the usre details and settings.
 */

@Composable
fun UserDetailsSettings(
    userDetails: User,
    splash: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    Column(
        modifier = Modifier.padding(top = 25.dp, start = 25.dp, end = 15.dp, bottom = 25.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = userDetails.name, style = black20Bold)
            IconButton(
                onClick = {}
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.image_description),
                    modifier = Modifier.size(25.dp),
                    tint = Color.Gray
                )
            }
        }
        Text(text = "@${userDetails.userName}", style = link15)
        Text(text = userDetails.status, style = gray15)
        SizedBox(height = 20)
        Divider(
            color = Color.LightGray
        )
        SizedBox(height = 20)
        Text(
            text = stringResource(id = R.string.logout),
            style = red30Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    profileViewModel.signOut(splash = splash)
                }
        )
    }
}