package com.atiq.neugelb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavGraph(controller: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        controller,
        HomeNavigationRoute, modifier = modifier
    ) {
        HomeNavigation(controller)
    }
}