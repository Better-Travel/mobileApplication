package com.bettertravel.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bettertravel.android.presentation.createRootComponent
import com.bettertravel.ui.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val root = createRootComponent()
        setContent {
            RootContent(root)
        }
    }
}
