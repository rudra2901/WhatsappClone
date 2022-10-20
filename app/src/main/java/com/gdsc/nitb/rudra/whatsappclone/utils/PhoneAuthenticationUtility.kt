package com.gdsc.nitb.rudra.whatsappclone.utils

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.gdsc.nitb.rudra.whatsappclone.R
import java.util.concurrent.TimeUnit

/**
 * A phone authentication utility method which will be helpful for doing all the authentication
 * related to phone login.
 */
object PhoneAuthenticationUtility {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Send the OTP.
     *
     * This method will initiate the OTP from the firebase.
     *
     * [phoneNumber] will be the number to which the OTP will be sent
     *
     * [activity] will be the current activity of the screen
     *
     * [callback] will be used to update the code if needed to
     */
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        callback: OtpSentCallbacks
    ) {
        callback.onOtpInitiated()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                callback.onOtpSent(
                    verificationId = verificationId,
                    token = token,
                    message = "${activity.getString(R.string.otp_sent)} $phoneNumber"
                )
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                callback.onError(exception.localizedMessage ?: "")
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(
                callbacks
            )
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Verify the OTP.
     *
     * After OTP is sent this method will be used to check if the OTP entered by the user is
     * valid or not.
     *
     * [verificationId] will be the ID which was generated when the OTP was going to be
     * initiated
     *
     * [code] is the OTP entered by the User
     *
     * [callback] will be used to update the code if needed to
     */
    fun verifyOTP(
        verificationId: String,
        code: String,
        callback: VerifyOtpCallbacks
    ) {
        callback.otpVerificationInitiated()

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        callback.onVerified(user = user)
                    } else {
                        callback.onError(message = "")
                    }
                } else {
                    callback.onError(message = task.exception?.localizedMessage ?: "")
                }
            }
    }
}
