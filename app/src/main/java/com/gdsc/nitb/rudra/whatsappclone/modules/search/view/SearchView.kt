package com.gdsc.nitb.rudra.whatsappclone.modules.search.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.modules.search.view.composables.SingleUserCard
import com.gdsc.nitb.rudra.whatsappclone.modules.search.viewModel.SearchViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CircularIndicatorMessage
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.Snackbar
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility
import kotlinx.coroutines.launch

/**
 * A chat view which will allow user to see the list of chats and fav chats
 */

@Composable
fun SearchView(userMessage: (String) -> Unit, searchViewModel: SearchViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val loading: Boolean by searchViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by searchViewModel.showMessage.observeAsState(initial = false)
    val message: String by searchViewModel.message.observeAsState(initial = "")
    val users: List<User> by searchViewModel.users.observeAsState(initial = emptyList())

    /**
     * Show snackbar
     */
    val showSnackbar = {
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = message,
            )) {
                SnackbarResult.Dismissed -> {
                    searchViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    searchViewModel.snackbarDismissed()
                }
            }
        }
    }

    if (showMessage) {
        Utility.showMessage(message = message)
        showSnackbar()
    }

    searchViewModel.getUsers()

    WhatsAppCloneTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                    ) {
                        items(users) { singleUser ->
                            SizedBox(height = 5)
                            SingleUserCard(
                                profileImage = singleUser.profilePic,
                                title = singleUser.name,
                                subTitle = singleUser.userName,
                                thirdLine = singleUser.status,
                                userId = singleUser.userId,
                                userMessage = userMessage,
                            )
                            SizedBox(height = 5)
                        }
                    }
                    if (loading) {
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