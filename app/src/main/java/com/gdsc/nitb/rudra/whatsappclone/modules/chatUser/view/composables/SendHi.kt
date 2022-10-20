package com.gdsc.nitb.rudra.whatsappclone.modules.chatUser.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.action25Bold

/**
 * A composable which show the user to send the first message to the opened chat user.
 */

@Composable
fun SendHi(sendMessage: () -> Unit) {
    Text(
        text = stringResource(id = R.string.send_hi),
        style = action25Bold,
        modifier = Modifier.clickable {
            sendMessage()
        }
    )
}