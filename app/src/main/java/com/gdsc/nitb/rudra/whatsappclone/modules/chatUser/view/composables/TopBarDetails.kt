package com.gdsc.nitb.rudra.whatsappclone.modules.chatUser.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.BackButton
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Action
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold

/**
 * A composable which will contain the to user details like the name and profile picture.
 */

@ExperimentalCoilApi
@Composable
fun TopBarDetails(
    selectedUserDetails: User,
    isFavorite: Boolean = false,
    changFav: () -> Unit,
    showFav: Boolean = false,
    back: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Card(
        backgroundColor = Color.White,
        shape = RoundedCornerShape(0),
        elevation = 3.dp
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = selectedUserDetails.profilePic,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.image_description_network),
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackButton(
                        back = {
                            back()
                            focusManager.clearFocus()
                        },
                        icon = Icons.Default.ArrowBack,
                        topSpace = 0
                    )
                    SizedBox(width = 15)
                    Text(text = selectedUserDetails.name, style = black20Bold)
                    if (showFav) {
                        IconButton(
                            onClick = {
                                changFav()
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = stringResource(
                                    id = R.string.image_description
                                ),
                                tint = Action
                            )
                        }
                    }
                }
                SizedBox(height = 10)
            }
        }
    }
}