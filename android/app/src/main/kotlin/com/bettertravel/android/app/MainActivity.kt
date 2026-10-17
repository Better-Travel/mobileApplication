package com.bettertravel.android.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.bettertravel.android.platform.MicrophonePermission
import com.bettertravel.android.presentation.createRootComponent
import com.bettertravel.ui.RootContent

class MainActivity : ComponentActivity() {
    private var pendingPermissionResult: ((Boolean) -> Unit)? = null
    private val microphoneLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            pendingPermissionResult?.invoke(granted)
            pendingPermissionResult = null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        MicrophonePermission.requester = { onResult ->
            pendingPermissionResult = onResult
            microphoneLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
        val root = createRootComponent()
        setContent {
            RootContent(root)
        }
    }

    override fun onDestroy() {
        MicrophonePermission.requester = null
        super.onDestroy()
    }
}
