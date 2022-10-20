package com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Action
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.lightGray15
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A composable which shows and gives the user to create a new status
 */

@ExperimentalMaterialApi
@Composable
fun CreateStatus(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    userDetails: User
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        focusManager.clearFocus()
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProfileImage(url = userDetails.profilePic)
        Text(
            text = stringResource(id = R.string.tap_for_status),
            style = lightGray15
        )
        Icon(
            Icons.Filled.Edit,
            contentDescription = stringResource(id = R.string.image_description),
            modifier = Modifier.size(25.dp),
            tint = Action
        )
    }
}