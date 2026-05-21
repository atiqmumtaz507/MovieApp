package com.atiq.neugelb.unittests

import com.atiq.neugelb.TestUtil
import com.atiq.neugelb.data.models.MoviesResult
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.data.remote.MoviesApi
import com.atiq.neugelb.data.repository.MoviesRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals


class MoviesRepositoryImplTest {
    private val moviesApi: MoviesApi = mockk()

    @Test
    fun test ´test´ () = runTest {
        coEvery {
            moviesApi.getPopularMovies(language = "de-DE", page = 1, region = "DE")
        } returns TestUtil.getMovieResult(1, 1, 2)

        val repo = MoviesRepositoryImpl(moviesApi)
        val response = repo.getPopularMovies(page = 1)

        assertEquals(
            ApiResult.Success(TestUtil.getMovieResult(1, 1, 2)),
            response
        )
    }
}