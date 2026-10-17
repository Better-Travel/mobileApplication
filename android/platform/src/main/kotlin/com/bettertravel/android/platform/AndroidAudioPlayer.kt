package com.bettertravel.android.platform

import android.media.AudioAttributes
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completable
import com.badoo.reaktive.disposable.Disposable
import com.bettertravel.domain.speech.AudioPlaying
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException

class AndroidAudioPlayer : AudioPlaying {
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun play(audio: ByteArray): Completable {
        return completable { emitter ->
            var player: MediaPlayer? = null
            var disposed = false
            emitter.setDisposable(
                object : Disposable {
                    override val isDisposed: Boolean
                        get() = disposed

                    override fun dispose() {
                        disposed = true
                        mainHandler.post {
                            player?.release()
                            player = null
                        }
                    }
                },
            )
            mainHandler.post {
                if (disposed) {
                    return@post
                }
                val created = MediaPlayer()
                player = created
                created.setAudioAttributes(
                    AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_ASSISTANT)
                        .build(),
                )
                created.setDataSource(BytesDataSource(audio))
                created.setOnCompletionListener { emitter.onComplete() }
                created.setOnErrorListener { _, _, _ ->
                    emitter.onError(SpeechRecognitionException(SpeechFailure.Failed, "Audio playback failed"))
                    true
                }
                created.setOnPreparedListener { prepared -> prepared.start() }
                created.prepareAsync()
            }
        }
    }
}

private class BytesDataSource(
    private val bytes: ByteArray,
) : MediaDataSource() {
    override fun readAt(
        position: Long,
        buffer: ByteArray,
        offset: Int,
        size: Int,
    ): Int {
        if (position >= bytes.size) {
            return -1
        }
        val count = minOf(size, bytes.size - position.toInt())
        bytes.copyInto(
            buffer,
            destinationOffset = offset,
            startIndex = position.toInt(),
            endIndex =
                position.toInt() + count,
        )
        return count
    }

    override fun getSize(): Long {
        return bytes.size.toLong()
    }

    override fun close() = Unit
}
