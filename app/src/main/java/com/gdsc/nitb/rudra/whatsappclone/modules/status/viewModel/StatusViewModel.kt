package com.gdsc.nitb.rudra.whatsappclone.modules.status.viewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.Status
import com.gdsc.nitb.rudra.whatsappclone.models.StatusDivision
import com.gdsc.nitb.rudra.whatsappclone.models.StatusFirestore
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreCallbacks
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreUtility
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility

/**
 * A view model for the StatusView which will do all the business logic and update the
 * ui state as per the requirement.
 */
class StatusViewModel : ViewModel() {
    private val firestoreUtility: FirestoreUtility = FirestoreUtility()

    var firstCallDone: Boolean = false

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
     * Status entered by the user.
     */
    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    /**
     * Update status entered by the user
     */
    fun updateStatus(value: String) {
        _status.value = value
    }

    /**
     * Status in the database
     */
    val allStatus = MutableLiveData(listOf<StatusDivision>())
    val currentUserStatus = MutableLiveData(listOf<StatusDivision>())

    /**
     * Listen to status changes
     */
    fun getStatus() {
        _loading.value = true
        firestoreUtility.allStatus(
            callbacks = object : FirestoreCallbacks {
                override fun status(newStatus: List<Status>) {
                    val division: MutableList<StatusDivision> = mutableListOf()
                    val userStatus: MutableList<StatusDivision> = mutableListOf()

                    for (status in newStatus.indices) {
                        val createdBy = newStatus[status].createdBy
                        val statusDivision = StatusDivision()
                        statusDivision.createdBy = createdBy
                        statusDivision.userDetails = newStatus[status].userDetails
                        if (firestoreUtility.currentUserReference() != createdBy) {
                            statusDivision.status.add(newStatus[status])
                            if (division.isNotEmpty() &&
                                division.last().createdBy == newStatus[status].createdBy
                            ) {
                                division.last().status.add(newStatus[status])
                            } else {
                                division.add(statusDivision)
                            }
                        } else {
                            if (userStatus.isEmpty()) {
                                statusDivision.status.add(newStatus[status])
                                userStatus.add(statusDivision)
                            } else {
                                userStatus.last().status.add(newStatus[status])
                            }
                        }
                    }

                    division.forEach { it ->
                        it.status.sortBy { it.createdOn }
                    }
                    userStatus.forEach { it ->
                        it.status.sortBy { it.createdOn }
                    }

                    currentUserStatus.value = userStatus
                    allStatus.value = division
                    _loading.value = false
                }

                override fun onError(message: String) {
                    _loading.value = false
                    _showMessage.value = true
                    _message.value = message
                }
            }
        )
    }

    /**
     * Upload status on DB
     */
    fun uploadStatus(currentActivity: Activity?) {
        if (loading.value == true) return
        try {
            startUploadStatus(currentActivity)
        } catch (exception: Exception) {
            Utility.showMessage(exception.localizedMessage ?: "")
        }
    }

    /**
     * Start uploading the status
     */
    private fun startUploadStatus(currentActivity: Activity?) {
        val activity = getActivity(currentActivity)
        val statusMessage = getStatus(activity)

        _loading.value = true

        val status = StatusFirestore(
            statusMessage = statusMessage,
            createdBy = firestoreUtility.currentUserReference(),
            appVersion = Utility.applicationVersion(),
            deviceId = Utility.getDeviceId(),
            deviceModel = Utility.deviceModel(),
            deviceOs = Utility.systemOS(),
            createdOn = Utility.currentTimeStamp(),
        )

        firestoreUtility.createStatus(
            statusFirestore = status,
            callbacks = object : FirestoreCallbacks {
                override fun isTrue() {
                    _loading.value = false
                    _status.value = ""
                }

                override fun onError(message: String) {
                    _status.value = ""
                    _loading.value = false
                    Utility.showMessage(message = message)
                }

            }
        )
    }

    /**
     * Returns a non-nullable activity
     *
     * Throws exception
     */
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    private fun getActivity(currentActivity: Activity?): Activity {
        return currentActivity ?: throw IllegalArgumentException()
    }

    /**
     * Get status value
     *
     * Throws exception
     */
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    private fun getStatus(activity: Activity): String {
        val status = _status.value
            ?: throw IllegalArgumentException(activity.getString(R.string.user_status_empty))
        if (status.isEmpty()) {
            throw IllegalArgumentException(activity.getString(R.string.user_status_empty))
        }
        return status
    }
}