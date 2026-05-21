package com.atiq.neugelb

import com.atiq.neugelb.data.models.Movie
import com.atiq.neugelb.data.models.MoviesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

object TestUtil {
    fun getMovieDummy(
        title: String = "batman",
        id: Long = 123
    ): Movie {
        return Movie(
            false,
            listOf(1),
            backdropPath = "",
            overview = "",
            originalTitle = "",
            originalLanguage = "De",
            id = id,
            popularity = 1.1,
            posterPath = "",
            voteCount = 1,
            voteAverage = 0.0,
            video = false,
            title = title
        )
    }

    fun getMovieResult(movieItems: Int, page: Int = 1, totalPages: Int = 5): MoviesResult {
        val movies = mutableListOf<Movie>()
        repeat(movieItems) {
            movies.add(getMovieDummy())
        }
        return MoviesResult(page, movies, totalPages)
    }
}

class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}