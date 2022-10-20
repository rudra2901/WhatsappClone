package com.gdsc.nitb.rudra.whatsappclone

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.gdsc.nitb.rudra.whatsappclone.modules.authentication.view.AuthenticationView
import com.gdsc.nitb.rudra.whatsappclone.modules.chatUser.view.ChatViewUser
import com.gdsc.nitb.rudra.whatsappclone.modules.formFill.view.FormFillView
import com.gdsc.nitb.rudra.whatsappclone.modules.home.view.HomeView
import com.gdsc.nitb.rudra.whatsappclone.modules.profile.view.ProfileView
import com.gdsc.nitb.rudra.whatsappclone.modules.splash.view.SplashView
import com.gdsc.nitb.rudra.whatsappclone.nav.Action
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Authentication
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.ChatUser
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.FormFill
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Home
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Profile
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Splash
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants

/**
 * The main Navigation composable which will handle all the navigation stack.
 */
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun NavComposeApp() {
    val navController = rememberNavController()
    val actions = remember(navController) { Action(navController) }
    WhatsAppCloneTheme {
        NavHost(
            navController = navController,
            startDestination = Splash
        ) {
            composable(Splash) {
                SplashView(
                    home = actions.home,
                    authentication = actions.authentication,
                    formFill = actions.formFill
                )
            }
            composable(Authentication) {
                AuthenticationView(
                    home = actions.home,
                    formFill = actions.formFill
                )
            }
            composable(Home) {
                HomeView(
                    profile = actions.profile,
                    userMessage = actions.chatUser
                )
            }
            composable(FormFill) {
                FormFillView(home = actions.home)
            }
            composable(Profile) {
                ProfileView(
                    back = actions.navigateBack,
                    splash = actions.splash
                )
            }
            composable(ChatUser) {
                val userId = it.arguments?.getString(Constants.userId) ?: ""
                ChatViewUser(
                    userId = userId,
                    back = actions.navigateBack
                )
            }
        }
    }
}
