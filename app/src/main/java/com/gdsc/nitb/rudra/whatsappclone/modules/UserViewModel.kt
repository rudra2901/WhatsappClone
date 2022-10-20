package com.gdsc.nitb.rudra.whatsappclone.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreCallbacks
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreUtility

/**
 * A view model which will handel all the user related business logic
 */
class UserViewModel : ViewModel() {
    private val firestoreUtility: FirestoreUtility = FirestoreUtility()

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
     * Current user details
     */
    private val _userDetails = MutableLiveData(User())
    val userDetails: LiveData<User> = _userDetails

    /**
     * Get user details
     */
    fun getUserDetails() {
        _loading.value = true
        firestoreUtility.userDetailsListener(
            callbacks = object : FirestoreCallbacks {
                override fun userDetails(user: User) {
                    _loading.value = false
                    _userDetails.value = user
                }

                override fun onError(message: String) {
                    _loading.value = false
                    _showMessage.value = true
                    _message.value = message
                }
            }
        )
    }
}