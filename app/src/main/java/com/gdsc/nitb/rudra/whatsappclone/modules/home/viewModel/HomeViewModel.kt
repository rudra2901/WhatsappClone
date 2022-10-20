package com.gdsc.nitb.rudra.whatsappclone.modules.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.chat

/**
 * A view model for the HomeView which will do all the business logic and update the
 * ui state as per the requirement.
 */
class HomeViewModel : ViewModel() {
    /**
     * Message to be shown to the user.
     */
    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    /**
     * Is there is message to be shown to the user.
     */
    private val _showMessage = MutableLiveData(false)
    val showMessage: LiveData<Boolean> = _showMessage

    /**
     * Update show message and value when the snack bar is dismissed.
     *
     * This is required to let the compose know that the snackbar is being dismissed so when
     * next time the snackbar needs to be re-build it will do that instead of not re-building it
     * since the value has not changed.
     */
    fun snackbarDismissed() {
        _showMessage.value = false
        _message.value = ""
    }

    /**
     * Is a loader need to be shown to the user.
     */
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    /**
     * Current app bar title.
     */
    private val _appBarTitle = MutableLiveData(chat)
    val appBarTitle: LiveData<String> = _appBarTitle

    /**
     * Update the value of the app bar title
     */
    fun updateTitle(value: String) {
        _appBarTitle.value = value
    }
}
