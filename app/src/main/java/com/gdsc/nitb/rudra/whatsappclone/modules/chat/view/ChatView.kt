package com.gdsc.nitb.rudra.whatsappclone.modules.chat.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.gdsc.nitb.rudra.whatsappclone.models.ChatDetails
import com.gdsc.nitb.rudra.whatsappclone.modules.chat.view.composables.SingleChatView
import com.gdsc.nitb.rudra.whatsappclone.modules.chat.view.composables.SingleFavChatView
import com.gdsc.nitb.rudra.whatsappclone.modules.chat.viewModel.ChatViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CircularIndicatorMessage
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.Snackbar
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility
import kotlinx.coroutines.launch

/**
 * A chat view which will allow user to see the list of chats and fav chats
 */

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
fun ChatView(userMessage: (String) -> Unit, chatViewModel: ChatViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val loading: Boolean by chatViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by chatViewModel.showMessage.observeAsState(initial = false)
    val message: String by chatViewModel.message.observeAsState(initial = "")
    val chats: List<ChatDetails> by chatViewModel.chats.observeAsState(initial = emptyList())
    val favChats: List<ChatDetails> by chatViewModel.favChats.observeAsState(initial = emptyList())

    if (!chatViewModel.firstCallDone) {
        chatViewModel.getChatList()
        chatViewModel.firstCallDone = true
    }

    /**
     * Show snackbar
     */
    val showSnackbar = {
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = message,
            )) {
                SnackbarResult.Dismissed -> {
                    chatViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    chatViewModel.snackbarDismissed()
                }
            }
        }
    }

    if (showMessage) {
        Utility.showMessage(message = message)
        showSnackbar()
    }

    WhatsAppCloneTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (favChats.isNotEmpty()) {
                            Text(
                                text = stringResource(id = R.string.favourites),
                                style = black20Bold
                            )
                            SizedBox(height = 10)
                            LazyVerticalGrid(
                                cells = GridCells.Fixed(3)
                            ) {
                                items(favChats) { singleChat ->
                                    SingleFavChatView(
                                        userMessage = userMessage,
                                        singleChat = singleChat
                                    )
                                }
                            }
                            SizedBox(height = 20)
                        }
                        if (chats.isNotEmpty()) {
                            Text(
                                text = stringResource(id = R.string.recents),
                                style = black20Bold
                            )
                            SizedBox(height = 10)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 50.dp)
                            ) {
                                items(chats) { singleChat ->
                                    SingleChatView(
                                        userMessage = userMessage,
                                        singleChat = singleChat
                                    )
                                }
                            }
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