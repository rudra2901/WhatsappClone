package com.gdsc.nitb.rudra.whatsappclone.nav

import androidx.navigation.NavHostController
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Authentication
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.FormFill
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Home
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Profile
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination.Splash
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants

/**
 * A set of destination used in the whole application
 */
object Destination {
    const val Splash = "splash"
    const val Authentication = "authentication"
    const val Home = "home"
    const val FormFill = "formFill"
    const val Profile = "profile"
    const val Chat = "chat"
    const val Search = "search"
    const val Status = "status"
    const val ChatUser = "chatUser/{${Constants.userId}}"
}

/**
 * Set of routes which will be passed to different composable so that
 * the routes which are required can be taken.
 */
class Action(navController: NavHostController) {
    val splash: () -> Unit = { navController.navigate(Splash) }
    val authentication: () -> Unit = {
        navController.navigate(Authentication) {
            popUpTo(Splash)
        }
    }
    val home: () -> Unit = {
        navController.navigate(Home) {
            popUpTo(Splash)
            popUpTo(Authentication)
        }
    }
    val formFill: () -> Unit = {
        navController.navigate(FormFill) {
            popUpTo(Splash)
            popUpTo(Authentication)
        }
    }
    val profile: () -> Unit = { navController.navigate(Profile) }
    val chatUser: (userId: String) -> Unit =
        { userId -> navController.navigate("chatUser/$userId") }
    val navigateBack: () -> Unit = { navController.popBackStack() }
}
