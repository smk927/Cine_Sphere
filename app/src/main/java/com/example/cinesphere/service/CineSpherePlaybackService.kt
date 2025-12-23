package com.example.cinesphere.service

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Locale

@AndroidEntryPoint
class CineSpherePlaybackService : MediaSessionService(), TextToSpeech.OnInitListener {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var textToSpeech: TextToSpeech? = null
    private var pendingText: String? = null
    private var isTtsInitialized = false
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        mediaSession = player?.let { MediaSession.Builder(this, it).build() }
        textToSpeech = TextToSpeech(this, this)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val text = intent?.getStringExtra("summary_text")
        if (!text.isNullOrBlank()) {
            if (isTtsInitialized) {
                synthesizeAndPlay(text)
            } else {
                pendingText = text
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle error
            } else {
                isTtsInitialized = true
                pendingText?.let {
                    synthesizeAndPlay(it)
                    pendingText = null
                }
            }
        }
    }

    private fun synthesizeAndPlay(text: String) {
        // Stop playback before generating new audio
        mainHandler.post {
            player?.stop()
            player?.clearMediaItems()
        }

        val file = File(cacheDir, "summary_audio.wav")
        if (file.exists()) {
            file.delete()
        }
        
        val utteranceId = "summary_speech"
        
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                // Play audio on main thread
                mainHandler.post {
                    player?.let { exoPlayer ->
                        val mediaItem = MediaItem.fromUri(file.toURI().toString())
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                }
            }

            override fun onError(utteranceId: String?) {
                // Handle error
            }
        })

        textToSpeech?.synthesizeToFile(text, null, file, utteranceId)
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        player?.release()
        player = null
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}
