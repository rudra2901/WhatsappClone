package com.gdsc.nitb.rudra.whatsappclone.models

import com.google.firebase.firestore.DocumentReference

/**
 * User details
 */
data class User(
    var accountCreatedOn: Long = 0,
    var appVersion: String = "",
    var deviceId: String = "",
    var deviceModel: String = "",
    var deviceOs: String = "",
    var lastLoggedIn: Long = 0,
    var profilePic: String = "",
    var userId: String = "",
    var userName: String = "",
    var name: String = "",
    var status: String = "",
    var phoneNumber: String = "",
    var countryNameCode: String = "",
    var countryPhoneCode: String = "",
    var countryName: String = "",
    var isDetailsAdded: Boolean = false,
    var emailId: String = ""
)

/**
 * Status details for firestore
 */
data class StatusFirestore(
    var statusMessage: String = "",
    var createdBy: DocumentReference? = null,
    var appVersion: String = "",
    var deviceId: String = "",
    var deviceModel: String = "",
    var deviceOs: String = "",
    var createdOn: Long = 0
)

/**
 * Status details
 */
data class Status(
    var statusMessage: String = "",
    var createdBy: DocumentReference? = null,
    var appVersion: String = "",
    var deviceId: String = "",
    var deviceModel: String = "",
    var deviceOs: String = "",
    var createdOn: Long = 0,
    var userDetails: User = User()
)

/**
 * Status division
 */
data class StatusDivision(
    var createdBy: DocumentReference? = null,
    var status: MutableList<Status> = mutableListOf(),
    var userDetails: User = User()
)

/**
 * Chat details for firestore
 */
data class ChatDetailsFirestore(
    var lastMessage: String = "",
    var lastMessageSentOn: Long = 0,
    var lastMessageSentBy: DocumentReference? = null,
    var chatCreatedOn: Long = 0,
    var chatCreatedBy: DocumentReference? = null,
    var chatIsFavourite: Boolean = false,
    var chatLastMessageRead: Boolean = false,
    var members: List<DocumentReference>? = null,
    var chatIsAGroup: Boolean = false
)

/**
 * Chat details
 */
data class ChatDetails(
    var lastMessage: String = "",
    var lastMessageSentOn: Long = 0,
    var lastMessageSentBy: DocumentReference? = null,
    var chatCreatedOn: Long = 0,
    var chatCreatedBy: DocumentReference? = null,
    var chatIsFavourite: Boolean = false,
    var chatLastMessageRead: Boolean = false,
    var members: List<DocumentReference>? = null,
    var chatIsAGroup: Boolean = false,
    var lastMessageSentOnString: String = "",
    var isLastMessageByCurrentUser: Boolean = false,
    var membersUser: ArrayList<User> = ArrayList(),
    var singleChatListDetails: User = User()
)

/**
 * Message details for the firestore
 */
data class MessageDetailsFirestore(
    var message: String = "",
    var sentOn: Long = 0,
    var sentBy: DocumentReference? = null
)

/**
 * Message details
 */
data class MessageDetails(
    var message: String = "",
    var sentOn: Long = 0,
    var sentBy: DocumentReference? = null,
    var messageByCurrentUser: Boolean = false
)
