package com.nanoporetech.scainter.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nanoporetech.scainter.R

private const val TAG = "MyFirebaseMessagingService"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Assuming you have access to userId
    private val userId = "userId"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // FCM token could change anytime, this method will be called,
        // when there is new updated token for this user, so always update user's
        // fcm token in your database.
        // Store fcm token in user's record, so that later
        // could be used to send notification to same user.
        Log.d(TAG, "Token: $token")

        // Save or update token.
        //updateTokenInFirestore(userId, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // This method will be called everytime FCM service sends message.
        if (remoteMessage.data.isNotEmpty()) {
            showNotification(
                remoteMessage.data["title"],
                remoteMessage.data["body"]
            )
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "default_channel"
        val channelName = "Default Channel"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: getString(R.string.app_name))
            .setContentText(body ?: "")
            .setSmallIcon(R.drawable.ic_notifications)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(0, notification)
    }
}