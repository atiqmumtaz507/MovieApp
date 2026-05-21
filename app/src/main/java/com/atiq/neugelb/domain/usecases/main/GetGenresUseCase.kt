package com.atiq.neugelb.domain.usecases.main

import com.atiq.neugelb.data.models.GenreResponse
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGenresUseCase @Inject constructor (val moviesRepository: MoviesRepository) {
    operator fun invoke(): Flow<ApiResult<GenreResponse>> {
        return flow {
            emit(moviesRepository.getGenresList())
        }
    }
}