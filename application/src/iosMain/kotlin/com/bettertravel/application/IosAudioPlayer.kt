package com.bettertravel.application

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completable
import com.badoo.reaktive.disposable.Disposable
import com.bettertravel.domain.speech.AudioPlaying
import com.bettertravel.domain.speech.SpeechFailure
import com.bettertravel.domain.speech.SpeechRecognitionException
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioPlayerDelegateProtocol
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.Foundation.NSData
import platform.Foundation.create
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class IosAudioPlayer : AudioPlaying {
    override fun play(audio: ByteArray): Completable {
        return completable { emitter ->
            var player: AVAudioPlayer? = null
            var delegate: PlaybackDelegate? = null
            var disposed = false
            emitter.setDisposable(
                object : Disposable {
                    override val isDisposed: Boolean
                        get() = disposed

                    override fun dispose() {
                        disposed = true
                        dispatch_async(dispatch_get_main_queue()) {
                            player?.stop()
                            player = null
                            delegate = null
                        }
                    }
                },
            )
            dispatch_async(dispatch_get_main_queue()) {
                if (disposed) {
                    return@dispatch_async
                }
                AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, error = null)
                AVAudioSession.sharedInstance().setActive(true, error = null)
                val data =
                    audio.usePinned { pinned ->
                        NSData.create(bytes = pinned.addressOf(0), length = audio.size.toULong())
                    }
                val created = runCatching { AVAudioPlayer(data = data, error = null) }.getOrNull()
                if (created == null) {
                    emitter.onError(SpeechRecognitionException(SpeechFailure.Failed, "Audio decoding failed"))
                    return@dispatch_async
                }
                val playbackDelegate =
                    PlaybackDelegate(
                        onFinished = { emitter.onComplete() },
                        onError = {
                            emitter.onError(
                                SpeechRecognitionException(SpeechFailure.Failed, "Audio playback failed"),
                            )
                        },
                    )
                delegate = playbackDelegate
                player = created
                created.delegate = playbackDelegate
                if (!created.play()) {
                    emitter.onError(SpeechRecognitionException(SpeechFailure.Failed, "Audio playback failed"))
                }
            }
        }
    }
}

private class PlaybackDelegate(
    private val onFinished: () -> Unit,
    private val onError: () -> Unit,
) : NSObject(),
    AVAudioPlayerDelegateProtocol {
    override fun audioPlayerDidFinishPlaying(
        player: AVAudioPlayer,
        successfully: Boolean,
    ) {
        if (successfully) onFinished() else onError()
    }
}
