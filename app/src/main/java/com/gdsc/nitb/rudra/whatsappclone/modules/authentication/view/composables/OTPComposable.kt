package com.gdsc.nitb.rudra.whatsappclone.modules.authentication.view.composables

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.modules.authentication.viewModel.AuthenticationViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.gray15
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.gray20Bold

/**
 * An OTP composable which will be used to allow the user to enter the OTP.
 */

@ExperimentalAnimationApi
@Composable
fun OTPComposable(
    home: () -> Unit,
    formFill: () -> Unit,
    authenticationViewModel: AuthenticationViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val otpSent: Boolean by authenticationViewModel.otpSent.observeAsState(initial = false)
    val otp: String by authenticationViewModel.otp.observeAsState(initial = "")

    AnimatedVisibility(
        visible = otpSent,
    ) {
        Column {
            SizedBox(height = 20)
            Text(
                stringResource(id = R.string.enter_otp),
                style = gray15
            )
            SizedBox(height = 10)
            TextField(
                value = otp,
                textStyle = black20Bold,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                onValueChange = { authenticationViewModel.onOTPChange(it) },
                placeholder = {
                    Text(
                        stringResource(id = R.string.otp_helper),
                        style = gray20Bold
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.LightGray,
                    backgroundColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (context is Activity) {
                            authenticationViewModel.verifyOTP(context, home, formFill)
                        }
                    }
                )
            )
        }
    }
}