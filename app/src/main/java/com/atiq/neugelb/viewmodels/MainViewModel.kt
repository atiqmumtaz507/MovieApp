package com.atiq.neugelb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atiq.neugelb.data.models.GenreResponse
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.models.MoviesResult
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.usecases.main.MainUseCases
import com.atiq.neugelb.ui.view.main.MainScreenAction
import com.atiq.neugelb.ui.view.main.MainScreenEvents
import com.atiq.neugelb.ui.view.main.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private var _screenState = MutableStateFlow(MainScreenState())
    val screenState = _screenState.asStateFlow()

    private var _events = MutableSharedFlow<MainScreenEvents>(
        replay = 0, extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    private var genresResponse: GenreResponse? = null
    private var currentPageForMovies = 1
    private var currentPagerPosition = 0

    init {
        loadContent(true)
        startAutoScroll()
    }

    private fun startAutoScroll() {
        viewModelScope.launch {
            while (isActive) {
                delay(3000L)
                if (
                    screenState.value.popularMovies.isNotEmpty() &&
                    screenState.value.isPagerAutoScrollPaused.not()
                    ) {
                    if (currentPagerPosition + 1 < screenState.value.popularMovies.size) {
                        currentPagerPosition += 1
                    } else {
                        currentPagerPosition = 0
                    }
                    updatePopularMoviePagerPosition(currentPagerPosition)
                }
            }
        }
    }

    private fun updateLoadingData(isLoading: Boolean) {
        _screenState.value = _screenState.value.copy(isLoadingData = isLoading)
    }

    private fun updatePagerPlay(isPlaying: Boolean) {
        _screenState.value = _screenState.value.copy(isPagerAutoScrollPaused = isPlaying)
    }

    private fun updatePopularMoviesData(movies: List<Movie>) {
        _screenState.value = _screenState.value.copy(popularMovies = movies)
    }

    private fun appendMovies(movies: List<Movie>) {
        _screenState.value = _screenState.value.copy(
            moviesList = _screenState.value.moviesList + movies
        )
    }

    private fun updatePopularMoviePagerPosition(newPosition: Int) {
        _screenState.value = _screenState.value.copy(currentPopularMoviePagerPosition = newPosition)
    }

    fun onAction(action: MainScreenAction) {
        when (action) {
            is MainScreenAction.PopularMovieClicked -> {
                println("Movie Item clicked ${action.movie.title}")
            }

            is MainScreenAction.MovieListItemPopulated -> {
                if (screenState.value.isLoadingData.not() &&
                    screenState.value.moviesList.size == action.index + 1
                ) {
                    currentPageForMovies += 1
                    loadContent(false)
                }
            }

            is MainScreenAction.UpdateManualPagerPosition -> {
                currentPagerPosition = action.position
            }

            is MainScreenAction.MovieFilterApplied -> {
                _screenState.value = _screenState.value.copy(selectedGenreId = action.filterId)
            }

            MainScreenAction.PagerPlayPauseToggleClicked -> {
                updatePagerPlay(
                    _screenState.value.isPagerAutoScrollPaused.not()
                )
            }
        }
    }

    fun loadContent(updatedPopularMoviesList: Boolean) {
        viewModelScope.launch {
            updateLoadingData(true)

            if (genresResponse == null) {
                mainUseCases.getGenresList.invoke().collect { apiResult ->
                    when (apiResult) {
                        is ApiResult.Success<GenreResponse> -> {
                            genresResponse = apiResult.data
                            val map = mutableMapOf("All Movies" to -1)
                            apiResult.data.genres.forEach {
                                map[it.name] = it.id
                            }
                            _screenState.value = _screenState.value.copy(genres = map)
                        }

                        else -> {}
                    }
                }
            }

            mainUseCases.getPopularMovies.invoke(currentPageForMovies).collect { apiResult ->
                updateLoadingData(false)
                when (apiResult) {
                    is ApiResult.Success<MoviesResult> -> {
                        if (genresResponse != null) {
                            apiResult.data.results.forEach {
                                it.genreIds?.forEach { genreId ->
                                    genresResponse?.getGenreName(genreId)?.let { genreName ->
                                        it.genreNames.add(genreName)
                                    }
                                }
                            }
                        }

                        apiResult.data.results.let {
                            if (updatedPopularMoviesList) {
                                updatePopularMoviesData(it.take(10))
                            }
                            appendMovies(it)
                        }
                    }

                    is ApiResult.HttpError -> {
                        _events.emit(
                            MainScreenEvents.ShowSnackBarMessage(
                                "HttpError ${apiResult.message}"
                            )
                        )
                    }

                    is ApiResult.NetworkError -> {
                        _events.emit(
                            MainScreenEvents.ShowSnackBarMessage(
                                "NetworkError ${apiResult.exception.message}"
                            )
                        )
                    }

                    is ApiResult.UnknownError -> {
                        _events.emit(
                            MainScreenEvents.ShowSnackBarMessage(
                                "UnknownError ${apiResult.throwable.message}"
                            )
                        )
                    }
                }
            }
        }
    }
}