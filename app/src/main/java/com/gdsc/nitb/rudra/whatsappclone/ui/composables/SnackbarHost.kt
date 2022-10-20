package com.gdsc.nitb.rudra.whatsappclone.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.actionBold
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold

/**
 * A snack bar composable which will be used to show message to a user.
 *
 * [snackbarHostState] will be used to manage the state of the snackbar.
 */

@Composable
fun Snackbar(snackbarHostState: SnackbarHostState) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (snackbarHostRef) = createRefs()

        SnackbarHost(
            modifier = Modifier
                .constrainAs(snackbarHostRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    backgroundColor = Color.White,
                    action = {
                        Text(
                            text = stringResource(id = R.string.okay),
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                },
                            style = actionBold
                        )
                    },
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text =
                        if ((snackbarHostState.currentSnackbarData?.message ?: "").isEmpty())
                            stringResource(id = R.string.something_went_wrong)
                        else
                            snackbarHostState.currentSnackbarData!!.message,
                        style = black20Bold
                    )
                }
            },
        )
    }
}