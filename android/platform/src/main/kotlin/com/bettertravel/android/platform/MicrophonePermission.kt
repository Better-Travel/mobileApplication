package com.bettertravel.android.platform

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

/**
 * Bridge between the speech recognizer (application scope) and the Activity that owns the permission launcher.
 * The Activity assigns [requester] while it is alive.
 */
object MicrophonePermission {
    var requester: ((onResult: (Boolean) -> Unit) -> Unit)? = null

    fun isGranted(context: Context): Boolean {
        return context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }
}
