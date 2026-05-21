package com.atiq.neugelb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atiq.neugelb.R
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.usecases.detailscreen.GetMovieByIdUseCase
import com.atiq.neugelb.ui.view.movie_details.DetailScreenEvent
import com.atiq.neugelb.ui.view.movie_details.MovieDetailScreenAction
import com.atiq.neugelb.ui.view.movie_details.MovieDetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieByIdUseCase
) : ViewModel() {
    var movieId: Long = 0

    private var _screenState = MutableStateFlow(MovieDetailsScreenState())
    val screenState = _screenState.asStateFlow()

    private var _events = MutableSharedFlow<DetailScreenEvent>(
        replay = 0, extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun attachMovieId(movieId: Long) {
        this.movieId = movieId
        loadDetailMovie(movieId)
    }

    private fun updateLoadingData(isLoading: Boolean) {
        _screenState.value = _screenState.value.copy(isLoading = isLoading)
    }

    private fun updateDetailMovie(movie: Movie) {
        _screenState.value = _screenState.value.copy(movie = movie)
    }

    fun onActionReceived(action: MovieDetailScreenAction) {
        when(action) {
            MovieDetailScreenAction.BookMarkClicked -> {
                viewModelScope.launch {
                    _events.emit(
                        DetailScreenEvent.ShowSnackBarMessageWithId(
                            R.string.not_implemented
                        )
                    )
                }
            }
        }
    }

    private fun loadDetailMovie(movieId: Long) {
        viewModelScope.launch {
            updateLoadingData(true)
            getMovieUseCase.invoke(movieId).collect { apiResult ->
                updateLoadingData(false)
                when (apiResult) {
                    is ApiResult.Success<Movie> -> {
                        println(apiResult.data.title)
                        updateDetailMovie(apiResult.data)
                    }

                    else -> {
                        println("Error while fetching the movie detail !!")
                        _events.emit(
                            DetailScreenEvent.ShowSnackBarMessageWithId(
                                R.string.movie_fetch_error
                            )
                        )
                    }
                }
            }
        }
    }
}