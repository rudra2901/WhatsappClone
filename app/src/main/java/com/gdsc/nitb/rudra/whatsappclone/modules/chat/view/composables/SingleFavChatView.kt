package com.gdsc.nitb.rudra.whatsappclone.modules.chat.view.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.ChatDetails
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Action
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Notification
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black15
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold

/**
 * A single fav chat list view which will be used to show the fav chats to the current
 * user
 */

@ExperimentalCoilApi
@Composable
fun SingleFavChatView(
    userMessage: (String) -> Unit,
    singleChat: ChatDetails,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (singleChat.singleChatListDetails.userId.isNotEmpty()) {
                    userMessage(singleChat.singleChatListDetails.userId)
                }
            }
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = Action,
                elevation = 4.dp,
                border = if (!singleChat.chatLastMessageRead) BorderStroke(
                    width = 1.dp,
                    color = Notification
                ) else null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 5.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = singleChat.singleChatListDetails.profilePic,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = stringResource(id = R.string.image_description_network),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
            }
            if (!singleChat.chatLastMessageRead) {
                Card(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .padding(start = 10.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = singleChat.lastMessage,
                        style = black15,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
        SizedBox(height = 10)
        Text(
            text = singleChat.singleChatListDetails.name,
            style = black20Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}