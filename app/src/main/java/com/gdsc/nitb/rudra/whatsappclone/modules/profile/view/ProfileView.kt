package com.gdsc.nitb.rudra.whatsappclone.modules.profile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.modules.UserViewModel
import com.gdsc.nitb.rudra.whatsappclone.modules.profile.view.composables.UserDetailsSettings
import com.gdsc.nitb.rudra.whatsappclone.modules.profile.viewModel.ProfileViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.BackButton
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CircularIndicatorMessage
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.Snackbar
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility
import kotlinx.coroutines.launch

/**
 * A profile view composable which will be used to show the profile details
 */

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun ProfileView(
    back: () -> Unit,
    splash: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val loading: Boolean by profileViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by profileViewModel.showMessage.observeAsState(initial = false)
    val message: String by profileViewModel.message.observeAsState(initial = "")

    /**
     * States from user view model
     */
    val loadingUser: Boolean by userViewModel.loading.observeAsState(initial = false)
    val showMessageUser: Boolean by userViewModel.showMessage.observeAsState(initial = false)
    val messageUser: String by userViewModel.message.observeAsState(initial = "")
    val userDetails: User by userViewModel.userDetails.observeAsState(initial = User())

    userViewModel.getUserDetails()

    /**
     * Show snackbar
     */
    val showSnackbar = { messageData: String ->
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = messageData,
            )) {
                SnackbarResult.Dismissed -> {
                    profileViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    profileViewModel.snackbarDismissed()
                }
            }
        }
    }

    if (showMessage) {
        Utility.showMessage(message = message)
        showSnackbar(message)
    }
    if (showMessageUser) {
        Utility.showMessage(message = messageUser)
        showSnackbar(messageUser)
    }

    WhatsAppCloneTheme {
        Surface(color = MaterialTheme.colors.background) {
            BottomSheetScaffold(
                modifier = Modifier.fillMaxSize(),
                sheetPeekHeight = 500.dp,
                sheetContent = {
                    UserDetailsSettings(userDetails = userDetails, splash = splash)
                },
                sheetBackgroundColor = Color.White,
                sheetShape = RoundedCornerShape(topStartPercent = 5, topEndPercent = 5),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = userDetails.profilePic,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = stringResource(id = R.string.image_description_network),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        contentScale = ContentScale.Crop
                    )
                    BackButton(back = back, icon = Icons.Filled.Close)
                    if (loading || loadingUser) {
                        CircularIndicatorMessage(
                            message = stringResource(id = R.string.please_wait)
                        )
                    }
                }
                Snackbar(snackbarHostState)
            }
        }
    }
}