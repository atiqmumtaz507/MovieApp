package com.atiq.neugelb.ui.view.main

sealed class MainScreenEvents {
    data class ShowSnackBarMessage(val message: String): MainScreenEvents()
}
