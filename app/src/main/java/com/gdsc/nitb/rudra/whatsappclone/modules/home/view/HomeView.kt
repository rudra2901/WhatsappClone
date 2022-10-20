package com.gdsc.nitb.rudra.whatsappclone.modules.home.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.modules.home.view.composables.HomeAppBar
import com.gdsc.nitb.rudra.whatsappclone.modules.home.view.composables.HomeBottomNavigation
import com.gdsc.nitb.rudra.whatsappclone.modules.home.view.composables.HomeNavigation
import com.gdsc.nitb.rudra.whatsappclone.modules.home.viewModel.HomeViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CircularIndicatorMessage
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.Snackbar
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.chat
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility
import kotlinx.coroutines.launch

/**
 * A home view which will be used to show all the main features of the application to
 * an authenticated user.
 */
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HomeView(
    profile: () -> Unit,
    userMessage: (String) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    val navController = rememberNavController()

    val loading: Boolean by homeViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by homeViewModel.showMessage.observeAsState(initial = false)
    val message: String by homeViewModel.message.observeAsState(initial = "")
    val appBarTitle: String by homeViewModel.appBarTitle.observeAsState(initial = chat)

    /**
     * Show snackbar
     */
    val showSnackbar = {
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = message,
            )) {
                SnackbarResult.Dismissed -> {
                    homeViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    homeViewModel.snackbarDismissed()
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
                topBar = {
                    HomeAppBar(title = appBarTitle, profile = profile)
                },
                bottomBar = {
                    HomeBottomNavigation(navController = navController)
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeNavigation(navController = navController, userMessage = userMessage)
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
