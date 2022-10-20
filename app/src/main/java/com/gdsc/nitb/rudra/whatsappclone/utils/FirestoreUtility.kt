package com.gdsc.nitb.rudra.whatsappclone.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.gdsc.nitb.rudra.whatsappclone.models.ChatDetails
import com.gdsc.nitb.rudra.whatsappclone.models.ChatDetailsFirestore
import com.gdsc.nitb.rudra.whatsappclone.models.MessageDetails
import com.gdsc.nitb.rudra.whatsappclone.models.MessageDetailsFirestore
import com.gdsc.nitb.rudra.whatsappclone.models.Status
import com.gdsc.nitb.rudra.whatsappclone.models.StatusFirestore
import com.gdsc.nitb.rudra.whatsappclone.models.User

/**
 * A utility class which will connect to Firestore and perform all the operation required.
 *
 * All the communication will happen from here only. This way it will be easy to maintain and
 * modify the database operations.
 */
class FirestoreUtility {
    private val db = Firebase.firestore
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Check for user details
     */
    fun checksForUserDetails(callbacks: FirestoreCallbacks) {
        if (firebaseAuth.currentUser != null) {
            val userId = getCurrentUserId()
            db.collection(DBConstants.Collection.users).document(userId).get()
                .addOnCompleteListener { task ->
                    if (task.exception != null) {
                        callbacks.onError(task.exception?.localizedMessage ?: "")
                        return@addOnCompleteListener
                    }
                    if (task.result.exists()) {
                        val userDetails = task.result.toObject<User>()
                        if (userDetails != null) {
                            callbacks.userDetails(user = userDetails)
                        } else {
                            callbacks.isFalse()
                        }
                    } else {
                        callbacks.isFalse()
                    }
                }
        }
    }

    /**
     * Set current user details
     */
    fun setUserDetails(data: User, callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.users).document(getCurrentUserId())
            .set(data, SetOptions.merge()).addOnCompleteListener { task ->
                if (task.exception != null) {
                    callbacks.onError(task.exception?.localizedMessage ?: "")
                    return@addOnCompleteListener
                }
                callbacks.isTrue()
            }
    }

    /**
     * Get the current user id from firebase
     */
    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    /**
     * Get current user details as a listener
     */
    fun userDetailsListener(callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.users).document(getCurrentUserId())
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    callbacks.onError(e.localizedMessage ?: "")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val data = snapshot.toObject<User>()
                    if (data != null) {
                        callbacks.userDetails(user = data)
                    } else {
                        callbacks.onError("")
                    }
                } else {
                    callbacks.onError("")
                }
            }
    }

    /**
     * Create a status by the current user
     */
    fun createStatus(statusFirestore: StatusFirestore, callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.status).document().set(statusFirestore)
            .addOnCompleteListener { task ->
                if (task.exception != null) {
                    callbacks.onError(task.exception?.localizedMessage ?: "")
                    return@addOnCompleteListener
                }
                callbacks.isTrue()
            }
    }

    /**
     * Get current user reference
     */
    fun currentUserReference(): DocumentReference {
        return getUserReference(getCurrentUserId())
    }

    /**
     * Create a listener for status in the application
     */
    fun allStatus(callbacks: FirestoreCallbacks) {
        allUsers(
            getCurrentAlso = true,
            callbacks = object : FirestoreCallbacks {
                override fun onError(message: String) {
                    callbacks.onError(message)
                }

                override fun userList(users: List<User>) {
                    db.collection(DBConstants.Collection.status)
                        .orderBy(DBConstants.DocumentField.createdBy, Query.Direction.DESCENDING)
                        .addSnapshotListener { statusValue, statusException ->
                            if (statusException != null) {
                                callbacks.onError(statusException.localizedMessage ?: "")
                            } else {
                                val status = ArrayList<Status>()
                                if (statusValue != null) {
                                    for (doc in statusValue) {
                                        val currentStatus = doc.toObject<Status>()
                                        val searchUser =
                                            users.find { it.userId == currentStatus.createdBy?.id }
                                        if (searchUser != null) {
                                            currentStatus.userDetails = searchUser
                                        }
                                        status.add(currentStatus)
                                    }

                                    callbacks.status(newStatus = status)
                                } else {
                                    callbacks.onError("")
                                }
                            }
                        }
                }
            }
        )
    }

    /**
     * Get all user lists except the current user
     */
    fun allUsers(getCurrentAlso: Boolean = false, callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.users)
            .whereNotEqualTo(
                DBConstants.DocumentField.userId,
                if (getCurrentAlso) "" else getCurrentUserId()
            )
            .addSnapshotListener { value, e ->
                if (e != null) {
                    callbacks.onError(e.localizedMessage ?: "")
                    return@addSnapshotListener
                }

                val users = ArrayList<User>()
                if (value != null) {
                    for (doc in value) {
                        users.add(doc.toObject())
                    }

                    users.sortBy { it.name }

                    callbacks.userList(users = users)
                } else {
                    callbacks.onError("")
                }
            }
    }

    /**
     * Check if the chat exists already or not
     *
     * [toUserId] will be used to check if any chat ever happened between the current user and the
     * [toUserId].
     */
    fun isChatAvailable(toUserId: String, callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
            .collection(DBConstants.Collection.chats).document(toUserId)
            .addSnapshotListener { currentTask, currentException ->
                if (currentException != null) {
                    callbacks.onError(currentException.localizedMessage ?: "")
                    return@addSnapshotListener
                }
                if (currentTask != null) {
                    if (currentTask.exists()) {
                        val currentChatDetails = currentTask.toObject<ChatDetails>()
                        val currentChatDetailsFirestore =
                            currentTask.toObject<ChatDetailsFirestore>()
                        if (currentChatDetails != null && currentChatDetailsFirestore != null) {

                            // Get other user chat details
                            db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
                                .collection(DBConstants.Collection.chats).document(toUserId).get()
                                .addOnCompleteListener { otherTask ->
                                    if (otherTask.exception != null) {
                                        callbacks.onError(otherTask.exception?.localizedMessage ?: "")
                                    } else {
                                        if (otherTask.result.exists()) {
                                            val otherChatDetails =
                                                otherTask.result.toObject<ChatDetails>()
                                            val otherChatDetailsFirestore =
                                                otherTask.result.toObject<ChatDetailsFirestore>()
                                            if (otherChatDetails != null && otherChatDetailsFirestore != null) {
                                                callbacks.chatDetails(
                                                    currentChatDetails = currentChatDetails,
                                                    currentChatDetailsFirestore = currentChatDetailsFirestore,
                                                    otherChatDetails = otherChatDetails,
                                                    otherChatDetailsFirestore = otherChatDetailsFirestore
                                                )
                                            }
                                        } else {
                                            callbacks.isFalse()
                                        }
                                    }
                                }
                        } else {
                            callbacks.onError("")
                        }
                    } else {
                        callbacks.isFalse()
                    }
                } else {
                    callbacks.onError("")
                }
            }
    }

    /**
     * Get list of messages from the [userId] to the current user
     */
    fun getMessages(userId: String, callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
            .collection(DBConstants.Collection.chats).document(userId)
            .collection(DBConstants.Collection.messages)
            .orderBy(DBConstants.DocumentField.sentOn, Query.Direction.DESCENDING)
            .addSnapshotListener { task, e ->
                if (e != null) {
                    callbacks.onError(e.localizedMessage ?: "")
                    return@addSnapshotListener
                }
                if (task != null) {
                    val messages = ArrayList<MessageDetails>()
                    for (message in task.documents) {
                        val messageDetails = message.toObject<MessageDetails>()
                        if (messageDetails != null) {
                            val sentBy = messageDetails.sentBy
                            if (sentBy != null) {
                                messageDetails.messageByCurrentUser =
                                    sentBy.id == getCurrentUserId()
                            }
                            messages.add(messageDetails)
                        }
                    }

                    callbacks.messages(messages = messages)
                } else {
                    callbacks.onError("")
                }
            }
    }

    /**
     * Get user details for the [userId]
     */
    fun getUserDetails(userId: String, callbacks: FirestoreCallbacks) {
        db.collection(DBConstants.Collection.users).document(userId).get()
            .addOnCompleteListener { task ->
                if (task.exception != null) {
                    callbacks.onError(task.exception?.localizedMessage ?: "")
                    return@addOnCompleteListener
                }
                if (task.result.exists()) {
                    val userDetails = task.result.toObject<User>()
                    if (userDetails != null) {
                        callbacks.userDetails(user = userDetails)
                    } else {
                        callbacks.onError("")
                    }
                } else {
                    callbacks.onError("")
                }
            }
    }

    /**
     * Get user reference from an id
     */
    fun getUserReference(userId: String): DocumentReference {
        return db.collection(DBConstants.Collection.users).document(userId)
    }

    /**
     * Send messages to [toUserId]
     */
    fun sendPersonalMessage(
        toUserId: String,
        currentChatDetailsFirestore: ChatDetailsFirestore,
        otherChatDetailsFirestore: ChatDetailsFirestore,
        messageDetailsFirestore: MessageDetailsFirestore? = null,
        callbacks: FirestoreCallbacks
    ) {
        /**
         * Update current user message option
         */
        val currentChatRef =
            db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
                .collection(DBConstants.Collection.chats).document(toUserId)
        val currentMessageRef =
            db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
                .collection(DBConstants.Collection.chats).document(toUserId)
                .collection(DBConstants.Collection.messages).document()

        /**
         * Update to user message details
         */
        val toChatRef = db.collection(DBConstants.Collection.chats).document(toUserId)
            .collection(DBConstants.Collection.chats).document(getCurrentUserId())
        val toMessageRef = db.collection(DBConstants.Collection.chats).document(toUserId)
            .collection(DBConstants.Collection.chats).document(getCurrentUserId())
            .collection(DBConstants.Collection.messages).document()

        /**
         * Update the details in batch
         */
        db.runBatch {
            it.set(currentChatRef, currentChatDetailsFirestore)
            it.set(toChatRef, otherChatDetailsFirestore)

            if (messageDetailsFirestore != null) {
                it.set(currentMessageRef, messageDetailsFirestore)
                it.set(toMessageRef, messageDetailsFirestore)
            }
        }.addOnCompleteListener { task ->
            if (task.exception != null) {
                callbacks.onError(message = task.exception?.localizedMessage ?: "")
                return@addOnCompleteListener
            }
            if (task.isSuccessful) {
                callbacks.isTrue()
            } else {
                callbacks.onError(message = "")
            }
        }
    }

    /**
     * Update current user chat details only
     */
    fun updateChatDetails(
        userId: String,
        chatDetailsFirestore: ChatDetailsFirestore,
        callbacks: FirestoreCallbacks
    ) {
        db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
            .collection(DBConstants.Collection.chats).document(userId).set(chatDetailsFirestore)
            .addOnCompleteListener { task ->
                if (task.exception != null) {
                    callbacks.onError(message = task.exception?.localizedMessage ?: "")
                    return@addOnCompleteListener
                }
                if (task.isSuccessful) {
                    callbacks.isTrue()
                } else {
                    callbacks.onError(message = "")
                }
            }
    }

    /**
     * Listen to current user message lists
     */
    fun currentUserChatList(callbacks: FirestoreCallbacks) {
        allUsers(
            getCurrentAlso = true,
            callbacks = object : FirestoreCallbacks {
                override fun onError(message: String) {
                    callbacks.onError(message)
                }

                override fun userList(users: List<User>) {
                    db.collection(DBConstants.Collection.chats).document(getCurrentUserId())
                        .collection(DBConstants.Collection.chats)
                        .orderBy(
                            DBConstants.DocumentField.lastMessageSentOn,
                            Query.Direction.DESCENDING
                        )
                        .addSnapshotListener { chatsValue, chatException ->
                            if (chatException != null) {
                                callbacks.onError(chatException.localizedMessage ?: "")
                            } else {
                                if (chatsValue != null) {
                                    val chatList = ArrayList<ChatDetails>()
                                    val favChatList = ArrayList<ChatDetails>()
                                    for (doc in chatsValue) {
                                        val chatDetails = doc.toObject<ChatDetails>()

                                        // Update the values which will be needed while showing the data
                                        chatDetails.lastMessageSentOnString =
                                            Utility.getTimeAgo(chatDetails.lastMessageSentOn)

                                        // If last message was sent by current user
                                        if (chatDetails.lastMessageSentBy != null) {
                                            chatDetails.isLastMessageByCurrentUser =
                                                chatDetails.lastMessageSentBy!!.id == getCurrentUserId()
                                        }

                                        // Is last message read
                                        chatDetails.chatLastMessageRead =
                                            !(!chatDetails.chatLastMessageRead && !chatDetails.isLastMessageByCurrentUser)

                                        // Get the users list from the list of all users
                                        // which are part of the current chat in the loop.
                                        val usersReference = chatDetails.members
                                        if (usersReference != null) {
                                            for (singleReference in usersReference) {
                                                val searchUser =
                                                    users.find { it.userId == singleReference.id }
                                                if (searchUser != null) {
                                                    chatDetails.membersUser.add(searchUser)

                                                    if (searchUser.userId != getCurrentUserId()) {
                                                        chatDetails.singleChatListDetails =
                                                            searchUser
                                                    }
                                                }
                                            }
                                        }
                                        if (chatDetails.chatIsFavourite) {
                                            favChatList.add(chatDetails)
                                        } else {
                                            chatList.add(chatDetails)
                                        }
                                    }

                                    callbacks.chatList(
                                        chatList = chatList,
                                        favChatList = favChatList
                                    )
                                } else {
                                    callbacks.onError("")
                                }
                            }
                        }
                }
            }
        )
    }
}