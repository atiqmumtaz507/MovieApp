package com.atiq.neugelb.domain.usecases.search

import com.atiq.neugelb.data.models.MoviesResult
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchMovieByNameUseCase @Inject constructor (val moviesRepository: MoviesRepository) {
    operator fun invoke(movieName: String): Flow<ApiResult<MoviesResult>> {
        return flow {
            emit(moviesRepository.searchMovieByName(movieName))
        }
    }
}