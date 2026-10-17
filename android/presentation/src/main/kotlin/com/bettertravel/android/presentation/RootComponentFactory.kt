package com.bettertravel.android.presentation

import androidx.activity.ComponentActivity
import com.arkivanov.decompose.retainedComponent
import com.bettertravel.presentation.navigation.DefaultRootComponent
import com.bettertravel.presentation.navigation.RootComponent

fun ComponentActivity.createRootComponent(): RootComponent {
    return retainedComponent { context -> DefaultRootComponent(context) }
}
