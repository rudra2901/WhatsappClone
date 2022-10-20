package com.gdsc.nitb.rudra.whatsappclone.modules.chatUser.view.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gdsc.nitb.rudra.whatsappclone.models.MessageDetails
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Action
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.white20

/**
 * A composable which shows the details of a single message
 */

@Composable
fun SingleMessage(message: MessageDetails) {
    Row(
        horizontalArrangement = if (message.messageByCurrentUser)
            Arrangement.End else Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth(
                if (message.messageByCurrentUser) 1f
                else 0.7f
            )
            .padding(bottom = 10.dp)
    ) {
        if (message.messageByCurrentUser)
            SizedBox(widthFloat = 0.3f)
        Card(
            elevation = 3.dp,
            backgroundColor = if (message.messageByCurrentUser)
                Color.White else Action,
            shape = RoundedCornerShape(30)
        ) {
            Text(
                text = message.message,
                style = if (message.messageByCurrentUser)
                    black20 else white20,
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 15.dp,
                    bottom = 15.dp
                )
            )
        }
    }
}