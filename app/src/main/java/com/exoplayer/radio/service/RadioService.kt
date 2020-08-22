package com.exoplayer.radio.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util


class RadioService : Service(), Player.EventListener {


    private var simpleExoPlayer: SimpleExoPlayer? = null

    private val playWhenReady = true

    private val playerNotificationManager: PlayerNotificationManager? = null

    private var mediaSessionConnector: MediaSessionConnector? = null

    private val playbackChannelId = "flutter_radio_player_channel_id"
    private val mediaSessionId = "flutter_radio_radio_media_session"

    private val playbackNotificationId = 1025




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e("Started", "Service")

        val mediaSource: MediaSource = createMediaSource(Uri.parse("http://uk7.internet-radio.com:8000/listen.pls&t=.pls"))

        simpleExoPlayer?.playWhenReady = playWhenReady;
        simpleExoPlayer?.prepare(mediaSource, false, false);


        startForegroundService()

        return START_NOT_STICKY
    }

    private fun startForegroundService() {

        val playerNotificationManager: PlayerNotificationManager


        val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {

            override fun getCurrentContentTitle(player: Player?): String {
                return "Player Media"
            }

            override fun createCurrentContentIntent(player: Player?): PendingIntent? {
            return null
            }

            override fun getCurrentContentText(player: Player?): String? {
                return "Techno Player Music"
            }

            override fun getCurrentLargeIcon(
                player: Player?,
                callback: PlayerNotificationManager.BitmapCallback?
            ): Bitmap? {

                return null
            }
        }


        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            playbackChannelId,
            R.string.app_name,
            R.string.app_name,
            playbackNotificationId,
            mediaDescriptionAdapter,
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean) {
                    simpleExoPlayer = null
                    stopSelf()
                }

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    startForeground(notificationId, notification)
                }
            })


        val mediaSession = MediaSessionCompat(this, mediaSessionId)
        mediaSession.isActive = true
        mediaSessionConnector = MediaSessionConnector(mediaSession)

        mediaSessionConnector!!.setPlayer(simpleExoPlayer)


        playerNotificationManager.setUseStopAction(true)
        playerNotificationManager.setFastForwardIncrementMs(0)
        playerNotificationManager.setRewindIncrementMs(0)
        playerNotificationManager.setUsePlayPauseActions(true)
        playerNotificationManager.setUseNavigationActions(false)
        playerNotificationManager.setUseNavigationActionsInCompactView(false)

        playerNotificationManager.setPlayer(simpleExoPlayer)
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)


    }




    override fun onCreate() {
        super.onCreate()

        initializePlayer()

    }


    private fun initializePlayer () {

        if (simpleExoPlayer == null) {
            val trackSelector = DefaultTrackSelector(this)
            val loadControl = DefaultLoadControl()
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
        }

    }

    private fun createMediaSource(uri: Uri) : MediaSource  {
        val userAgent = Util.getUserAgent(this, this.getString(R.string.app_name))
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "Audio-Foreground")
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)
        simpleExoPlayer?.release()
        simpleExoPlayer = null

    }





    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}


