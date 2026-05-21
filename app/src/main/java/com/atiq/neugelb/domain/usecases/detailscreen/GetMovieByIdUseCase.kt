package com.atiq.neugelb.domain.usecases.detailscreen

import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor (val moviesRepository: MoviesRepository) {
    operator fun invoke(movieId: Long): Flow<ApiResult<Movie>> {
        return flow {
            emit(moviesRepository.getMovie(movieId))
        }
    }
}