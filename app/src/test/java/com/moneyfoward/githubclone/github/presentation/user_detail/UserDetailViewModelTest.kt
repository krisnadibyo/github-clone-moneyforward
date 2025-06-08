package com.moneyfoward.githubclone.github.presentation.user_detail

import androidx.lifecycle.SavedStateHandle
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.github.data.network.FakeRemoteGithubDataSource
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserDetailViewModelTest {

    private lateinit var viewmodel: UserDetailViewModel
    private lateinit var repository: FakeRemoteGithubDataSource
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private var collectJob: Job? = null
    private val usernameTest = "testuser"
    private val userTest = User(id = 1, usernameTest, avatar = "avatar")


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        savedStateHandle.set("username", usernameTest)
        repository = FakeRemoteGithubDataSource()
        repository.addUsers(listOf(userTest))
        viewmodel = UserDetailViewModel(
            repository,
            savedStateHandle
        )
        collectJob = testScope.launch {
            viewmodel.state.collect()
            viewmodel.events.collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        collectJob?.cancel()
        Dispatchers.resetMain()
        repository.clearData()
    }

    @Test
    fun `when initialized, should load user and nonforked repositories`() = runTest {
        // Given
        val repos = listOf(
            UserRepo(id = 1, name = "repo1", fullName = "testuser/repo1", isFork = false),
            UserRepo(id = 2, name = "repo2", fullName = "testuser/repo2", isFork = true),
            UserRepo(id = 3, name = "repo3", fullName = "testuser/repo3", isFork = false)

        )
        val expectedRepo = repos.filter { !it.isFork }
        repository.addUserRepo(repos)

        // When
        advanceUntilIdle()

        // Then
        val state = viewmodel.state.first()
        assertEquals(userTest, state.user)
        assertEquals(expectedRepo, state.repositories)
        assertFalse(state.isRefreshing)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `when loadMore is called, should append new repositories`() = runTest {
        // Given
        val initialRepos = List(20) { i ->
            UserRepo(
                id = i.toLong(),
                name = "repo$i",
                fullName = "${usernameTest}/repo$i",
                isFork = false
            )
        }
        val moreRepos = List(20) { i ->
            UserRepo(
                id = i.toLong() + 20,
                name = "repo${i + 20}",
                fullName = "${usernameTest}/repo${i + 20}",
                isFork = false
            )
        }
        advanceUntilIdle()
        repository.addUserRepo(initialRepos)

        // When - Initial load
        viewmodel.onAction(UserDetailAction.OnRefresh)
        advanceUntilIdle()


        // Then - Verify initial state
        var state = viewmodel.state.first()
        assertEquals(userTest, state.user)
        assertEquals(initialRepos, state.repositories)
        assertFalse(state.isRefreshing)
        assertTrue(viewmodel.hasNextPage)

        // When - Load more
        repository.clearRepo()
        repository.addUserRepo(moreRepos)
        viewmodel.onAction(UserDetailAction.OnScrollToBottom)
        advanceUntilIdle()

        // Then - Verify appended repositories
        state = viewmodel.state.first()
        assertEquals(initialRepos + moreRepos, state.repositories)
        assertFalse(state.isLoadingMore)
        assertFalse(state.isRefreshing)
        assertTrue(viewmodel.hasNextPage)
    }

    @Test
    fun `On Refresh then loadMore is called, and error happen, user repo list should stayed the same`() =
        runTest {
            // Given
            val initialRepos = List(20) { i ->
                UserRepo(
                    id = i.toLong(),
                    name = "repo$i",
                    fullName = "${usernameTest}/repo$i",
                    isFork = false
                )
            }
            val lastPage = 2
            advanceUntilIdle()
            repository.addUserRepo(initialRepos)

            // When - Initial load
            viewmodel.onAction(UserDetailAction.OnRefresh)
            advanceUntilIdle()


            // Then - Verify initial state
            var state = viewmodel.state.first()
            assertEquals(userTest, state.user)
            assertEquals(initialRepos, state.repositories)
            assertFalse(state.isRefreshing)
            assertTrue(viewmodel.hasNextPage)
            assertEquals(lastPage, viewmodel.currentPage)

            // When - Load more
            repository.clearRepo()
            repository.setShouldReturnError(true)
            viewmodel.onAction(UserDetailAction.OnScrollToBottom)
            advanceUntilIdle()

            // Then - Verify appended repositories
            state = viewmodel.state.first()
            assertEquals(initialRepos, state.repositories)
            assertFalse(state.isLoadingMore)
            assertFalse(state.isRefreshing)
            assertTrue(viewmodel.hasNextPage)
            assertEquals(lastPage, viewmodel.currentPage)
        }

    @Test
    fun `when loadMore is called while loading, should not trigger another load`() = runTest {
        // Given
        val repos = List(20) { i ->
            UserRepo(
                id = i.toLong(),
                name = "repo$i",
                fullName = "${usernameTest}/repo$i",
                isFork = false
            )
        }

        repository.addUserRepo(repos)
        advanceUntilIdle()


        // When - Try to load more while loading
        viewmodel.onAction(UserDetailAction.OnScrollToBottom)
        advanceTimeBy(100)
        viewmodel.onAction(UserDetailAction.OnScrollToBottom) // Call twice
        advanceUntilIdle()

        // Then - Should only load once
        val state = viewmodel.state.first()
        assertEquals(repos.size * 2, state.repositories.size)
    }

    @Test
    fun `when error occurs, should emit error event`() = runTest {
        // Given
        repository.setShouldReturnError(true, NetworkError.UNKNOWN)

        // When
        advanceUntilIdle()

        // Then
        val event = viewmodel.events.first()
        assertTrue(event is UserDetailEvent.Error)
        assertEquals(NetworkError.UNKNOWN, (event as UserDetailEvent.Error).error)
    }

    @Test
    fun `when loading more, should update loading state correctly`() = runTest {
        // Given
        val repos = List(20) { i ->
            UserRepo(
                id = i.toLong(),
                name = "repo$i",
                fullName = "${usernameTest}/repo$i",
                isFork = false
            )
        }
        repository.addUserRepo(repos)

        // When - Initial load
        advanceUntilIdle()

        // When - Load more
        viewmodel.onAction(UserDetailAction.OnScrollToBottom)
        advanceTimeBy(100)

        // Then - Should be loading
        var state = viewmodel.state.first()
        assertTrue(state.isLoadingMore)

        // When - Loading completes
        advanceUntilIdle()

        // Then - Should not be loading
        state = viewmodel.state.first()
        assertFalse(state.isLoadingMore)
    }


}