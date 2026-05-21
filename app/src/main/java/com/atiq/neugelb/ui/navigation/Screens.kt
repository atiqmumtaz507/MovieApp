package com.atiq.neugelb.ui.navigation

sealed class Screens(val route: String) {
    object Main : Screens(MainScreenRoute)
    object Detail : Screens(DetailScreenRoute)

    object Search : Screens(SearchScreenRoute)
}
