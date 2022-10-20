package com.gdsc.nitb.rudra.whatsappclone.modules.home.view.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.gdsc.nitb.rudra.whatsappclone.modules.chat.view.ChatView
import com.gdsc.nitb.rudra.whatsappclone.modules.home.view.nav.NavigationItem
import com.gdsc.nitb.rudra.whatsappclone.modules.search.view.SearchView
import com.gdsc.nitb.rudra.whatsappclone.modules.status.view.StatusView

/**
 * The navigation controller of the home screen which will route the bottom navigation
 * bar.
 */

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HomeNavigation(userMessage: (String) -> Unit, navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Chat.route) {
        composable(NavigationItem.Chat.route) {
            ChatView(userMessage = userMessage)
        }
        composable(NavigationItem.Search.route) {
            SearchView(userMessage = userMessage)
        }
        composable(NavigationItem.Status.route) {
            StatusView()
        }
    }
}