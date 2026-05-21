package com.atiq.neugelb

import app.cash.turbine.test
import com.atiq.neugelb.data.remote.ApiResult
import com.atiq.neugelb.domain.usecases.search.SearchMovieByNameUseCase
import com.atiq.neugelb.ui.view.search_movies.SearchAction
import com.atiq.neugelb.viewmodels.SearchMovieViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SearchViewModelTest {
    private val usecase: SearchMovieByNameUseCase = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `updates query and searchText immediately, without calling use case`() = runTest {
        val apiSuccess = ApiResult.Success(TestUtil.getMovieResult(2))
        coEvery { usecase.invoke("batman") } returns flowOf(apiSuccess)

        val vm = SearchMovieViewModel(usecase)
        vm.onActionReceived(SearchAction.OnTextChanged("batman"))

        assertEquals("batman", vm.query.value)
        assertEquals("batman", vm.state.value.searchText)

        coVerify(exactly = 0) { usecase.invoke(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Typing trigers loading after debounce`() = runTest {
        val apiSuccess = ApiResult.Success(TestUtil.getMovieResult(2))
        coEvery { usecase.invoke("batman") } returns flowOf(apiSuccess)

        val vm = SearchMovieViewModel(usecase)
        vm.state.test {
            val initial = awaitItem()
            assertFalse(initial.isLoading)
            assertEquals(-1, initial.searchResultCount)

            vm.onActionReceived(SearchAction.OnTextChanged("batman"))
            val afterText = awaitItem()
            assertEquals("batman", afterText.searchText)

            advanceTimeBy(799)
            coVerify(exactly = 0) { usecase.invoke(any()) }

            advanceTimeBy(1)
            val stateForloadingTrue = awaitItem()
            assertTrue(stateForloadingTrue.isLoading)

            advanceTimeBy(299)
            coVerify(exactly = 0) { usecase.invoke(any()) }

            //
            advanceTimeBy(900)
            coVerify(exactly = 1) { usecase.invoke("batman") }

            val loadingFalse = awaitItem()
            assertFalse(loadingFalse.isLoading)

            val resultsUpdated1 = awaitItem()
            assertEquals(2, resultsUpdated1.searchResultCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `blank search query does not trigger network call`() = runTest {
        val vm = SearchMovieViewModel(usecase)

        vm.onActionReceived(SearchAction.OnTextChanged("   "))

        advanceTimeBy(2000)
        coVerify(exactly = 0) { usecase.invoke(any()) }
        confirmVerified(usecase)
    }
}