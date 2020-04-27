package com.example.e_suratpermintaan.framework.fcm

import android.content.Intent
import android.util.Log
import com.example.e_suratpermintaan.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    private val tagString = "FCMSERVCE"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(tagString, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(tagString, "Message data payload: " + remoteMessage.data)

            val titleValue = remoteMessage.data.getValue("title_key")
            val bodyValue = remoteMessage.data.getValue("body_key")
            val idSpValue = remoteMessage.data.getValue("id_sp")

            val intent = Intent()
            intent.action = getString(R.string.firebase_onmessagereceived_intentfilter)
            intent.putExtra("title_value", titleValue)
            intent.putExtra("body_value", bodyValue)
            intent.putExtra("id_sp", idSpValue)
            sendBroadcast(intent)

//            if ( /* Check if data needs to be processed by long running job */true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                // scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                // handleNow()
//            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(tagString, "Message Notification Body: " + remoteMessage.notification!!.body)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onNewToken(token: String) {
        Log.d(tagString, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token)
    }

}
