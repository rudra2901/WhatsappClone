package com.gdsc.nitb.rudra.whatsappclone.modules.authentication.view

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import com.gdsc.nitb.rudra.whatsappclone.modules.authentication.view.composables.OTPComposable
import com.gdsc.nitb.rudra.whatsappclone.modules.authentication.viewModel.AuthenticationViewModel
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CircularIndicatorMessage
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.CountryCodePicker
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.Snackbar
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.WhatsAppCloneTheme
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black20Bold
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.gray15
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.gray20Bold
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.white20Bold
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility.showMessage
import kotlinx.coroutines.launch

/**
 * The authentication view which will be used to give the user an option to authenticate
 * themselves and start using the application.
 *
 * Will be using phone number login to authenticate the user. This will be similar to WhatsApp.
 *
 * [authenticationViewModel] will be used to do all the business logic and update the view
 * state when required.
 */

@ExperimentalAnimationApi
@Composable
fun AuthenticationView(
    formFill: () -> Unit,
    home: () -> Unit,
    authenticationViewModel: AuthenticationViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val loading: Boolean by authenticationViewModel.loading.observeAsState(initial = false)
    val showMessage: Boolean by authenticationViewModel.showMessage.observeAsState(initial = false)
    val otpSent: Boolean by authenticationViewModel.otpSent.observeAsState(initial = false)
    val message: String by authenticationViewModel.message.observeAsState(initial = "")
    val phoneNumber: String by authenticationViewModel.phoneNumber.observeAsState(initial = "")

    /**
     * Show snackbar
     */
    val showSnackbar = {
        coroutineScope.launch {
            when (snackbarHostState.showSnackbar(
                message = message,
            )) {
                SnackbarResult.Dismissed -> {
                    authenticationViewModel.snackbarDismissed()
                }
                SnackbarResult.ActionPerformed -> {
                    authenticationViewModel.snackbarDismissed()
                }
            }
        }
    }

    if (showMessage) {
        showMessage(message = message)
        showSnackbar()
    }

    WhatsAppCloneTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp, vertical = 30.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.welcome_to)
                                    + " " +
                                    stringResource(id = R.string.app_name),
                            style = black20Bold
                        )
                        Text(
                            stringResource(id = R.string.insert_phone_number),
                            style = gray15
                        )
                        SizedBox(
                            sizeFloat = 0.1f
                        )
                        TextField(
                            value = phoneNumber,
                            textStyle = black20Bold,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            onValueChange = { authenticationViewModel.onPhoneNumberChange(it) },
                            placeholder = {
                                Text(
                                    stringResource(id = R.string.phone_helper),
                                    style = gray20Bold
                                )
                            },
                            leadingIcon = {
                                Row {
                                    CountryCodePicker(pickedCountry = {
                                        authenticationViewModel.country = it
                                    })
                                    SizedBox(width = 10)
                                }
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
                                        authenticationViewModel.sendOTPToPhoneNumber(context)
                                    }
                                }
                            )
                        )
                        OTPComposable(
                            home = home,
                            formFill = formFill
                        )
                        SizedBox(height = 50)
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            onClick = {
                                focusManager.clearFocus()
                                if (context is Activity) {
                                    if (otpSent) {
                                        authenticationViewModel.verifyOTP(context, home, formFill)
                                    } else {
                                        authenticationViewModel.sendOTPToPhoneNumber(context)
                                    }
                                }
                            }
                        ) {
                            Text(
                                if (otpSent)
                                    stringResource(id = R.string.verify).uppercase()
                                else
                                    stringResource(id = R.string.continue_button).uppercase(),
                                style = white20Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    if (loading) {
                        CircularIndicatorMessage(
                            message = stringResource(id = R.string.please_wait)
                        )
                    }
                }
                Snackbar(snackbarHostState)
            }
        }
    }
}
