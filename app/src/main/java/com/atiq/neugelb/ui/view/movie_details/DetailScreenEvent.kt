package com.atiq.neugelb.ui.view.movie_details

sealed class DetailScreenEvent {
    data class ShowSnackBarMessageWithId(val resourceId: Int): DetailScreenEvent()
}