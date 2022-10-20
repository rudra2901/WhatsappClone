package com.gdsc.nitb.rudra.whatsappclone.modules.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreCallbacks
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreUtility

/**
 * A view model for the SearchView which will do all the business logic and update the
 * ui state as per the requirement.
 */
class SearchViewModel : ViewModel() {
    private val firebaseUtility: FirestoreUtility = FirestoreUtility()

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
     * List of users
     */
    private val _users = MutableLiveData(listOf<User>())
    val users: LiveData<List<User>> = _users

    /**
     * Get user list
     */
    fun getUsers() {
        firebaseUtility.allUsers(
            callbacks = object : FirestoreCallbacks {
                override fun userList(users: List<User>) {
                    _users.value = users
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