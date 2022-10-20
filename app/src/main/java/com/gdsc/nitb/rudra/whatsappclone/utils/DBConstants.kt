package com.gdsc.nitb.rudra.whatsappclone.utils

/**
 * A database constants which will contain all the keys required to fetch the data from database
 */
object DBConstants {
    /**
     * Constants related to collections
     */
    object Collection {
        const val users = "users"
        const val status = "status"
        const val chats = "chats"
        const val messages = "messages"
    }

    /**
     * Document field
     */
    object DocumentField {
        const val createdBy = "createdBy"
        const val name = "name"
        const val userId = "userId"
        const val lastMessageSentOn = "lastMessageSentOn"
        const val sentOn = "sentOn"
    }
}