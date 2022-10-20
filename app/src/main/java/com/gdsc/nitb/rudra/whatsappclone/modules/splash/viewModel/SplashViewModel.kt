package com.gdsc.nitb.rudra.whatsappclone.modules.splash.viewModel

import androidx.lifecycle.ViewModel
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreCallbacks
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreUtility
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility

/**
 * View model for  SplashView.
 *
 * This will contain the business logic related to splash
 * and also might be required to change the UI state of it.
 */
class SplashViewModel : ViewModel() {
    private val firestoreUtility: FirestoreUtility = FirestoreUtility()

    /**
     * Check if user is logged in or not
     *
     * If logged in call the [home] function, otherwise
     * call the [authentication] function.
     */
    fun checkIfUserLoggedIn(home: () -> Unit, authentication: () -> Unit, formFill: () -> Unit) {
        if (firestoreUtility.firebaseAuth.currentUser != null) {
            firestoreUtility.checksForUserDetails(
                callbacks = object : FirestoreCallbacks {
                    override fun isFalse() {
                        home()
                    }

                    override fun userDetails(user: User) {
                        user.lastLoggedIn = Utility.currentTimeStamp()
                        user.appVersion = Utility.applicationVersion()
                        user.deviceId = Utility.getDeviceId()
                        user.deviceModel = Utility.deviceModel()
                        user.deviceOs = Utility.systemOS()

                        updateUserDetails(user, home, formFill, user.isDetailsAdded)
                    }

                    override fun onError(message: String) {
                        home()
                    }
                }
            )
        } else {
            authentication()
        }
    }

    /**
     * Update the user details
     */
    private fun updateUserDetails(
        user: User,
        home: () -> Unit,
        formFill: () -> Unit,
        isUserDetailsAdded: Boolean
    ) {
        firestoreUtility.setUserDetails(
            data = user,
            callbacks = object : FirestoreCallbacks {
                override fun isTrue() {
                    navigateTo(home, formFill, isUserDetailsAdded)
                }

                override fun onError(message: String) {
                    Utility.showMessage(message = message)
                    navigateTo(home, formFill, isUserDetailsAdded)
                }
            }
        )
    }

    /**
     * Navigate to depending on [isUserDetailsAdded]
     */
    private fun navigateTo(home: () -> Unit, formFill: () -> Unit, isUserDetailsAdded: Boolean) {
        if (isUserDetailsAdded) {
            home()
        } else {
            formFill()
        }
    }
}
