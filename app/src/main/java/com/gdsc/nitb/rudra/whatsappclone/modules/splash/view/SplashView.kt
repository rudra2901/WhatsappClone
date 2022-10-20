package com.gdsc.nitb.rudra.whatsappclone.modules.splash.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.modules.splash.viewModel.SplashViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Action
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.white30Bold

/**
 * The splash view which will be used to show the splash screen to the user while some
 * operations to be performed before we move forward.
 *
 * Like setting up the environment, listeners, etc.
 *
 * [splashViewModel] will be used to do all the business logic and update the UI.
 */

@Composable
fun SplashView(
    home: () -> Unit,
    authentication: () -> Unit,
    formFill: () -> Unit,
    splashViewModel: SplashViewModel = viewModel()
) {
    splashViewModel.checkIfUserLoggedIn(
        home = home,
        authentication = authentication,
        formFill = formFill
    )

    WhatsAppCloneTheme {
        Surface(color = Action) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = white30Bold,
                )
            }
        }
    }
}
