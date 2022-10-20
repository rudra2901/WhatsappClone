package com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.modules.status.viewModel.StatusViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.BackButton
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Notification
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.white20Bold
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.white20Bold60
import kotlinx.coroutines.launch

/**
 * A status sheet composable which shows the add status creator option.
 */

@ExperimentalMaterialApi
@Composable
fun StatusSheet(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    statusViewModel: StatusViewModel = viewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val status: String by statusViewModel.status.observeAsState(initial = "")

    Surface(
        color = Notification,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 50.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                BackButton(
                    back = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    },
                    icon = Icons.Filled.Close,
                    topSpace = 15
                )
                TextButton(
                    onClick = {
                        if (context is Activity) {
                            statusViewModel.uploadStatus(context)
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = white20Bold
                    )
                }
            }
            TextField(
                value = status,
                textStyle = white20Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 15.dp
                    ),
                onValueChange = { statusViewModel.updateStatus(it) },
                placeholder = {
                    Text(
                        stringResource(id = R.string.what_is_mind),
                        style = white20Bold60.copy()
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (context is Activity) {
                            statusViewModel.uploadStatus(context)
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                ),
                shape = RoundedCornerShape(size = 10.dp)
            )
        }
    }
}