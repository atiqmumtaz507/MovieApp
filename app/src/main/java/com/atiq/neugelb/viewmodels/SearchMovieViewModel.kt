package com.atiq.neugelb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.models.MoviesResult
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.usecases.search.SearchMovieByNameUseCase
import com.atiq.neugelb.ui.view.search_movies.SearchAction
import com.atiq.neugelb.ui.view.search_movies.SearchScreenEvents
import com.atiq.neugelb.ui.view.search_movies.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieByNameUseCase
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private var _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    private var _events = MutableSharedFlow<SearchScreenEvents>(
        replay = 0, extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    private fun updatedSearchResult(movies: List<Movie>) {
        _state.value = _state.value.copy(searchResultCount = movies.size)
        _state.value = _state.value.copy(searchResult = movies)
    }

    private fun updateLoadingData(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    private fun updatedSearchText(text: String) {
        _state.value = _state.value.copy(searchText = text)
    }

    init {
        viewModelScope.launch {
            _query.map { it.trim() }
                .debounce(800)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .flatMapLatest {
                    updateLoadingData(true)
                    delay(300)
                    searchMovieUseCase.invoke(it)
                }
                .collect {
                    updateLoadingData(false)
                    when (it) {
                        is ApiResult.Success<MoviesResult> -> {
                            updatedSearchResult(it.data.results)
                        }

                        else -> {
                            println("Error")
                        }
                    }
                }
        }
    }

    fun onActionReceived(action: SearchAction) {
        when (action) {
            is SearchAction.MovieClicked -> {

            }

            is SearchAction.OnTextChanged -> {
                _query.value = action.newText
                updatedSearchText(action.newText)
            }

            is SearchAction.OnTextSearched -> {

            }
        }
    }
}