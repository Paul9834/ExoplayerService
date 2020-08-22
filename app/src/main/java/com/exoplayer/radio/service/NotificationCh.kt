package com.exoplayer.radio.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


class NotificationCh : Application() {

    companion object {
        final val CHANNEL_ID = "Audio Service"
        final val CHANNEL_ID_PLAYER = "Audio PlayBack"

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel();
    }


    fun createNotificationChannel () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Audio Service"
            val description = "Play audio media"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
                this.enableLights(true)
                this.enableVibration(true)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}