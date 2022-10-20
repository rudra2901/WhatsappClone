package com.gdsc.nitb.rudra.whatsappclone.modules.status.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.StatusDivision
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.modules.UserViewModel
import com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables.CreateStatus
import com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables.SingleStatus
import com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables.StatusSheet
import com.gdsc.nitb.rudra.whatsappclone.modules.status.viewModel.StatusViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CircularIndicatorMessage
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.Snackbar
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility
import kotlinx.coroutines.launch

/**
 * A chat view which will allow user to see their status which they have uploaded till now.
 */

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun StatusView(
    statusViewModel: StatusViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    val loading: Boolean by statusViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by statusViewModel.showMessage.observeAsState(initial = false)
    val message: String by statusViewModel.message.observeAsState(initial = "")
    val allStatus: List<StatusDivision> by statusViewModel.allStatus.observeAsState(initial = emptyList())

    /**
     * States from user view model
     */
    val loadingUser: Boolean by userViewModel.loading.observeAsState(initial = false)
    val showMessageUser: Boolean by userViewModel.showMessage.observeAsState(initial = false)
    val messageUser: String by userViewModel.message.observeAsState(initial = "")
    val userDetails: User by userViewModel.userDetails.observeAsState(initial = User())

    if (!statusViewModel.firstCallDone) {
        userViewModel.getUserDetails()
        statusViewModel.getStatus()
        statusViewModel.firstCallDone = true
    }

    /**
     * Show snackbar
     */
    val showSnackbar = { messageData: String ->
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = messageData,
            )) {
                SnackbarResult.Dismissed -> {
                    statusViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    statusViewModel.snackbarDismissed()
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
                scaffoldState = bottomSheetScaffoldState,
                sheetContent = {
                    StatusSheet(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                    )
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(id = R.string.my_status),
                            style = black20Bold
                        )
                        SizedBox(height = 15)
                        CreateStatus(
                            coroutineScope = coroutineScope,
                            bottomSheetScaffoldState = bottomSheetScaffoldState,
                            userDetails = userDetails
                        )
                        SizedBox(height = 15)
                        Text(
                            text = stringResource(id = R.string.recent_status),
                            style = black20Bold
                        )
                        SizedBox(height = 15)
                        LazyVerticalGrid(
                            cells = GridCells.Fixed(2),
                            contentPadding = PaddingValues(bottom = 50.dp)
                        ) {
                            items(allStatus) {
                                SingleStatus(
                                    singleStatus = it
                                )
                            }
                        }
                    }
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
