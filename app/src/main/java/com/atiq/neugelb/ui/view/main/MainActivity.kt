package com.atiq.neugelb.ui.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.atiq.neugelb.ui.navigation.NavGraph
import com.atiq.neugelb.ui.theme.NeuGelbTheme
import com.atiq.neugelb.ui.view.common.SnackbarController
import dagger.hilt.android.AndroidEntryPoint

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("SnackbarController not provided")
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuGelbTheme {
                val hostState = remember { SnackbarHostState() }
                val controller = remember { SnackbarController() }

                LaunchedEffect(hostState) {
                    controller.attach(hostState)
                }

                CompositionLocalProvider(LocalSnackbarController provides controller) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState) }
                    ) { innerPadding ->
                        val navController = rememberNavController()
                        NavGraph(navController, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}