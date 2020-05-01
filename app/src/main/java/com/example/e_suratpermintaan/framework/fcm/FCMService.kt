package com.example.e_suratpermintaan.framework.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.example.e_suratpermintaan.App
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.PreferenceConstants
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity.Companion.ID_SP_EXTRA_KEY
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class FCMService : FirebaseMessagingService() {

    private val tagString = "FCMSERVICE"

    fun getProfile(context: Context): DataProfile? {
        val prefs =
            context.getSharedPreferences(
                PreferenceConstants.PROFILE_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        val profileJson = prefs!!.getString(PreferenceConstants.PROFILE_PREFS_STRING_KEY, null)
        val gson = Gson()
        return gson.fromJson(profileJson, DataProfile::class.java)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(tagString, "From: " + remoteMessage.from)

        val idUser = getProfile(applicationContext)?.id.toString()

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty() && idUser.isNotEmpty()) {
            Log.d(tagString, "Message data payload: " + remoteMessage.data)

            val notificationTitle = remoteMessage.data.getValue("notification_title")
            val notificationBody = remoteMessage.data.getValue("notification_body")
            val dataBody = remoteMessage.data.getValue("data_body_key")
            val idSpValue = remoteMessage.data.getValue("id_sp")

            if (App.wasInForeground) {
                val intent = Intent()
                intent.action = getString(R.string.firebase_onmessagereceived_intentfilter)
                intent.putExtra("notification_title", notificationTitle)
                intent.putExtra("notification_body", notificationBody)
                intent.putExtra("data_body_key", dataBody)
                intent.putExtra("id_sp", idSpValue)
                sendBroadcast(intent)
            }

            showNotification(
                baseContext,
                notificationTitle,
                notificationBody,
                idSpValue
            )

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

    private fun showNotification(
        context: Context,
        title: String?,
        message: String,
        idSp: String
    ) {

        val channelId = "Channel_E-SuratPermintaan"
        val channelName = "Notif Surat Permintaan"

        val intent = Intent(context, DetailSuratPermintaanActivity::class.java)
        intent.putExtra(ID_SP_EXTRA_KEY, idSp)

        // Dengan membuat requestCode maka pendingIntent untuk aplikasi akan unik semua
        // Kalau dibuat sama, misal kita buat 0 aja, maka kita akan menggunakan pendingIntent yang
        // pertama kali diciptakan (create), misal ada notifikasi pertama dengan id_sp 116
        // kalau ada notifikasi dengan id 117, maka dia akan memakai pendingIntent untuk 116
        // Untuk membuat dia tidak menggunakan pendingIntent yang lama, maka pakai flags :
        // PendingIntent.FLAG_UPDATE_CURRENT
        // atau dengan menggunakan currentTimeMilis yang unik
        // Di bawah memakai keduanya untuk memastikan bahwa tidak ada yang sama untuk idSp yang berbeda
        // referensi solusi :
        // https://stackoverflow.com/questions/16004006/how-can-i-get-the-intent-extras-for-a-pendingintent-that-is-already-pending

        val requestCode = System.currentTimeMillis().toInt()
        // val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            //.setVibrate(longArrayOf(100, 100, 100, 100, 100))
            .setSound(defaultSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            //channel.enableVibration(true)
            //channel.vibrationPattern = longArrayOf(100, 100, 100, 100, 100)
            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        //val notificationId = idSp.toInt()
        val notificationId = System.currentTimeMillis().toInt()

        notificationManagerCompat.notify(notificationId, notification)
    }


    override fun onNewToken(token: String) {
        Log.d(tagString, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token)
    }

}
