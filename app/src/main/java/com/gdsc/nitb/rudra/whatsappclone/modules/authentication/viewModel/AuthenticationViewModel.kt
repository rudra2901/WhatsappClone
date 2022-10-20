package com.gdsc.nitb.rudra.whatsappclone.modules.authentication.viewModel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.models.User
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.defaultPic
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreCallbacks
import com.gdsc.nitb.rudra.whatsappclone.utils.FirestoreUtility
import com.gdsc.nitb.rudra.whatsappclone.utils.OtpSentCallbacks
import com.gdsc.nitb.rudra.whatsappclone.utils.PhoneAuthenticationUtility
import com.gdsc.nitb.rudra.whatsappclone.utils.Utility
import com.gdsc.nitb.rudra.whatsappclone.utils.VerifyOtpCallbacks
import io.github.farhanroy.cccp.CCPCountry
import io.github.farhanroy.cccp.getLibraryMasterCountriesEnglish

/**
 * A view model for the AuthenticationView which will do all the business logic and update the
 * ui state as per the requirement.
 */
class AuthenticationViewModel : ViewModel() {
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
     * Is otp is sent to the number.
     */
    private val _otpSent = MutableLiveData(false)
    val otpSent: LiveData<Boolean> = _otpSent

    /**
     * Selected country details
     */
    var country: CCPCountry = getLibraryMasterCountriesEnglish().first()

    /**
     * Phone number entered by the user.
     */
    private val _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String> = _phoneNumber

    /**
     * Whenever there is a change in the text field for phone number, then this method will
     * be used to update the value and the UI state.
     */
    fun onPhoneNumberChange(value: String) {
        _phoneNumber.value = value
    }

    /**
     * OTP entered by the user.
     */
    private val _otp = MutableLiveData("")
    val otp: LiveData<String> = _otp

    /**
     * Whenever there is a change in the text field for OTP, then this method will
     * be used to update the value and the UI state.
     */
    fun onOTPChange(value: String) {
        _otp.value = value
    }

    var storedVerificationId: String? = null

    /**
     * Initiate the OTP for the entered number.
     *
     * [currentActivity] is the current activity which will be given to phone number authenticator.
     */
    fun sendOTPToPhoneNumber(currentActivity: Activity?) {
        if (loading.value == true) return
        try {
            startPhoneOTP(currentActivity)
        } catch (exception: Exception) {
            _loading.value = false
            _showMessage.value = true
            _message.value = exception.localizedMessage ?: ""
        }
    }

    /**
     * Verify the OTP code entered by the user.
     *
     * [currentActivity] is the current activity which will be given to phone number authenticator.
     *
     * [home] is the method which will be used to redirect the user to the home page if the
     * authentication was successful
     */
    fun verifyOTP(currentActivity: Activity?, home: () -> Unit, formFill: () -> Unit) {
        if (loading.value == true) return
        try {
            startOTPVerification(currentActivity, home, formFill)
        } catch (exception: Exception) {
            _loading.value = false
            _showMessage.value = true
            _message.value = exception.localizedMessage ?: ""
        }
    }

    /**
     * Initiate the OTP for the entered number.
     *
     * [currentActivity] is the current activity which will be given to phone number authenticator.
     */
    private fun startPhoneOTP(currentActivity: Activity?) {
        val activity = getActivity(currentActivity)
        val code = getCountryCode()
        val number = getPhoneNumber(activity)

        PhoneAuthenticationUtility.sendOtp(
            phoneNumber = "$code$number",
            activity = activity,
            callback = object : OtpSentCallbacks {
                override fun onOtpSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                    message: String
                ) {
                    _loading.value = false
                    _otpSent.value = true
                    _showMessage.value = true
                    _message.value = message
                    storedVerificationId = verificationId
                }

                override fun onError(message: String) {
                    _loading.value = false
                    _showMessage.value = true
                    _message.value = message
                }

                override fun onOtpInitiated() {
                    _loading.value = true
                }
            }
        )
    }

    /**
     * Start the otp verification
     *
     * [currentActivity] is the current activity which will be given to phone number authenticator.
     *
     * [home] is the method which will be used to redirect the user to the home page if the
     * authentication was successful
     */
    private fun startOTPVerification(
        currentActivity: Activity?,
        home: () -> Unit,
        formFill: () -> Unit
    ) {
        val activity = getActivity(currentActivity)
        val verificationId = getVerificationId(activity)
        val cCode = getCountryCode()
        val number = getPhoneNumber(activity)
        val code = getOtpCode(activity, cCode, number)

        PhoneAuthenticationUtility.verifyOTP(
            verificationId = verificationId,
            code = code,
            callback = object : VerifyOtpCallbacks {
                override fun otpVerificationInitiated() {
                    _loading.value = true
                }

                override fun onVerified(user: FirebaseUser) {
                    val userDetails = User(
                        phoneNumber = number,
                        countryName = country.name,
                        countryNameCode = country.nameCode,
                        countryPhoneCode = country.phoneCode,
                        lastLoggedIn = Utility.currentTimeStamp(),
                        appVersion = Utility.applicationVersion(),
                        deviceId = Utility.getDeviceId(),
                        deviceModel = Utility.deviceModel(),
                        deviceOs = Utility.systemOS(),
                        userId = user.uid
                    )

                    checkForUserDetails(userDetails, home, formFill)
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
     * Check for user details if it's available or not
     */
    private fun checkForUserDetails(userDetails: User, home: () -> Unit, formFill: () -> Unit) {
        firestoreUtility.checksForUserDetails(
            callbacks = object : FirestoreCallbacks {
                override fun isFalse() {
                    userDetails.accountCreatedOn = Utility.currentTimeStamp()
                    userDetails.profilePic = defaultPic.shuffled().first()
                    updateUserDetails(userDetails, home, formFill, true)
                }

                override fun userDetails(user: User) {
                    user.lastLoggedIn = Utility.currentTimeStamp()
                    user.appVersion = Utility.applicationVersion()
                    user.deviceId = Utility.getDeviceId()
                    user.deviceModel = Utility.deviceModel()
                    user.deviceOs = Utility.systemOS()
                    updateUserDetails(user, home, formFill, !user.isDetailsAdded)
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
     * Update user details
     */
    private fun updateUserDetails(
        userDetails: User,
        home: () -> Unit,
        formFill: () -> Unit,
        isNewUser: Boolean
    ) {
        firestoreUtility.setUserDetails(
            data = userDetails,
            callbacks = object : FirestoreCallbacks {
                override fun isTrue() {
                    _loading.value = false
                    if (isNewUser) {
                        formFill()
                    } else {
                        home()
                    }
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
     * Returns a non-nullable activity
     *
     * Throws exception
     */
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    private fun getActivity(currentActivity: Activity?): Activity {
        return currentActivity ?: throw IllegalArgumentException()
    }

    /**
     * Returns a non-nullable country code
     */
    private fun getCountryCode(): String {
        return "+${country.phoneCode}"
    }

    /**
     * Returns a non-nullable phone number
     *
     * Throws exception
     */
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    private fun getPhoneNumber(activity: Activity): String {
        val number = _phoneNumber.value
            ?: throw IllegalArgumentException(activity.getString(R.string.phone_number_empty))
        if (number.isEmpty()) {
            throw IllegalArgumentException(activity.getString(R.string.phone_number_empty))
        }
        return number
    }

    /**
     * Returns a non-nullable verification id
     *
     * Throws exception
     */
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    private fun getVerificationId(activity: Activity): String {
        return storedVerificationId ?: throw IllegalArgumentException(
            activity.getString(
                R.string.something_went_wrong
            )
        )
    }

    /**
     * Return a non-nullable OTP value
     *
     * Throws exception
     */
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    private fun getOtpCode(activity: Activity, cCode: String, number: String): String {
        val code = _otp.value ?: throw IllegalArgumentException(
            "${
                activity.getString(
                    R.string.otp_empty
                )
            } $cCode$number"
        )
        if (code.isEmpty()) {
            throw IllegalArgumentException(
                "${
                    activity.getString(
                        R.string.otp_empty
                    )
                } $cCode$number"
            )
        }
        return code
    }
}
