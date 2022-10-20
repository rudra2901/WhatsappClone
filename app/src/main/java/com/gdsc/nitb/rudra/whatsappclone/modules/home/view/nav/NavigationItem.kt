package com.gdsc.nitb.rudra.whatsappclone.modules.home.view.nav

import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.nav.Destination
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.chat
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.search
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.status

/**
 * A navigator item class which will be used as a navigation for bottom bar in home screen
 */
sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Chat : NavigationItem(Destination.Chat, R.mipmap.ic_chat, chat)
    object Search : NavigationItem(Destination.Search, R.mipmap.ic_search, search)
    object Status : NavigationItem(Destination.Status, R.mipmap.ic_status, status)
}
