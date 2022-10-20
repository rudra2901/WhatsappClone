package com.gdsc.nitb.rudra.whatsappclone.utils

import android.os.Build
import android.util.Log
import com.gdsc.nitb.rudra.whatsappclone.BuildConfig
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.gdsc.nitb.rudra.whatsappclone.utils.Constants.application

/**
 * An utility method which will contain the utility methods, helpful for doing the similar methods
 * throughout the application.
 *
 * This will help in better code maintenance.
 */
object Utility {
    /**
     * Show a log message in the console
     */
    fun showMessage(message: String) {
        Log.w(application, message)
    }

    /**
     * Get current time stamp
     */
    fun currentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Get application version
     */
    fun applicationVersion(): String {
        return "${BuildConfig.VERSION_NAME}:${BuildConfig.VERSION_CODE}"
    }

    /**
     * Get device id
     */
    fun getDeviceId(): String {
        return Build.ID
    }

    /**
     * Get device model
     */
    fun deviceModel(): String {
        return "${Build.MODEL} ${Build.BRAND} ${Build.DEVICE}"
    }

    /**
     * Get phone OS
     */
    fun systemOS(): String {
        return "${Build.ID} ${Build.VERSION.SDK_INT} ${Build.VERSION.CODENAME}"
    }

    /**
     * Convert timestamp to a readable format in time ago
     */
    fun getTimeAgo(time: Long): String {
        return TimeAgo.using(time = time)
    }
}
