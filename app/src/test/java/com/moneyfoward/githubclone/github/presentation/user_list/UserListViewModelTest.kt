package com.moneyfoward.githubclone.github.presentation.user_list

import androidx.lifecycle.SavedStateHandle
import com.moneyfoward.githubclone.core.data.networking.ConfigApi
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.github.data.network.FakeRemoteGithubDataSource
import com.moneyfoward.githubclone.github.domain.model.User
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


class UserListViewModelTest {
    private lateinit var viewmodel: UserListViewModel
    private lateinit var repository: FakeRemoteGithubDataSource
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private var collectJob: Job? = null


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        repository = FakeRemoteGithubDataSource()
        viewmodel = UserListViewModel(
            repository,
            savedStateHandle
        )
        collectJob = testScope.launch {
            viewmodel.state.collect()
            viewmodel.events.collect()
        }
        runTest {
            // After init the refresh function is called
            advanceUntilIdle()
        }

    }

    @After
    fun tearDown() {
        collectJob?.cancel()
        Dispatchers.resetMain()
        repository.clearData()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onRefresh should fetch users success and update state`() = runTest {
        // Given
        val fakeUsers = listOf(User(1, "krisna", "avatar.com"))
        repository.addUsers(fakeUsers)

        // When
        viewmodel.onAction(UserListAction.OnRefresh)
        advanceUntilIdle()

        // Then
        val state = viewmodel.state.value
        assertEquals(fakeUsers, state.users)
        assertFalse(state.isRefreshing)
        assertFalse(viewmodel.hasNextPage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onRefresh should fetch users success with has next and update state`() = runTest {
        // Given
        val fakeUsers = mutableListOf<User>()
        val pageSize = ConfigApi.PAGE_SIZE
        repeat(pageSize) {
            fakeUsers.add(User(it, "user-${it}", "user $it"))
        }
        repository.addUsers(fakeUsers)

        // When
        viewmodel.onAction(UserListAction.OnRefresh)
        advanceUntilIdle()

        // Then
        val state = viewmodel.state.value
        assertEquals(fakeUsers, state.users)
        assertFalse(state.isRefreshing)
        assertTrue(viewmodel.hasNextPage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onRefresh should fetch users empty and update state`() = runTest {
        // Given
        val emptyUser = emptyList<User>()

        // When
        viewmodel.onAction(UserListAction.OnRefresh)
        advanceUntilIdle()

        // Then
        val state = viewmodel.state.value
        assertEquals(emptyUser, state.users)
        assertFalse(state.isRefreshing)
        assertFalse(viewmodel.hasNextPage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onRefresh should fetch users Error, emit event and update state`() = runTest {
        // Given
        repository.setShouldReturnError(true, NetworkError.UNKNOWN)
        val error = NetworkError.UNKNOWN

        // When
        viewmodel.onAction(UserListAction.OnRefresh)
        advanceUntilIdle()

        // Then
        val event = viewmodel.events.first()
        val state = viewmodel.state.value

        assertFalse(state.isRefreshing)
        assertTrue(event is UserListEvent.Error)
        assertEquals(error, (event as UserListEvent.Error).error)
    }


    @Test
    fun `when search query is entered, should debounce 300ms and update state with query`() {
        runTest {
            // given
            val query = "kris"
            val fakeUsers = listOf(User(1, "krisna", "avatar.com"))
            repository.addUsers(fakeUsers)
            val shouldOnlyCalledOnce = 1


            // Act - simulate rapid input
            viewmodel.onAction(UserListAction.OnUserSearch("k"))
            viewmodel.onAction(UserListAction.OnUserSearch("kr"))
            viewmodel.onAction(UserListAction.OnUserSearch("kri"))
            viewmodel.onAction(UserListAction.OnUserSearch("kris"))

            // Wait for debounce to expire (300ms)
            advanceTimeBy(300)

            // Advance coroutine tasks
            advanceUntilIdle()

            // Assert
            val state = viewmodel.state.value
            assertEquals(fakeUsers, state.users)
            assertEquals(query, state.query)
            assertEquals(shouldOnlyCalledOnce, repository.searchCount)
        }
    }

    @Test
    fun `when load more user is called, should append new users to existing list`() = runTest {
        // Given
        val initialUsers = mutableListOf<User>()
        val pageSize = ConfigApi.PAGE_SIZE
        repeat(pageSize) {
            initialUsers.add(User(it, "user-${it}", "avatar $it"))
        }

        val moreUsers = listOf(
            User(id = 11, username = "user11", avatar = "avatar11"),
            User(id = 12, username = "user12", avatar = "avatar12")
        )
        repository.addUsers(initialUsers)

        // When - First load
        viewmodel.onAction(UserListAction.OnRefresh)
        advanceUntilIdle()

        // Then - Verify initial load
        var state = viewmodel.state.first()
        assertEquals(initialUsers, state.users)
        assertTrue(viewmodel.hasNextPage)

        // When - Load more
        repository.clearUser() // Clear previous users
        repository.addUsers(moreUsers) // Add new users for next page
        viewmodel.onAction(UserListAction.OnScrollToBottom)
        advanceUntilIdle()

        // Then - Verify appended users
        state = viewmodel.state.first()
        assertEquals(initialUsers + moreUsers, state.users)
        assertFalse(viewmodel.hasNextPage)
    }

    @Test
    fun `when load more user is called, and get error`() = runTest {
        // Given
        val initialUsers = mutableListOf<User>()
        val pageSize = ConfigApi.PAGE_SIZE
        repeat(pageSize) {
            initialUsers.add(User(it, "user-${it}", "avatar $it"))
        }

        repository.addUsers(initialUsers)

        // When - First load
        viewmodel.onAction(UserListAction.OnRefresh)
        advanceUntilIdle()

        // Then - Verify initial load
        var state = viewmodel.state.first()
        assertEquals(initialUsers, state.users)
        assertTrue(viewmodel.hasNextPage)

        // When - Load more
        repository.clearUser()
        repository.setShouldReturnError(true)
        viewmodel.onAction(UserListAction.OnScrollToBottom)
        advanceUntilIdle()

        // Then - Verify appended users
        state = viewmodel.state.first()
        assertEquals(initialUsers, state.users)
        assertTrue(viewmodel.hasNextPage)

        val event = viewmodel.events.first()
        assertTrue(event is UserListEvent.Error)
    }

    @Test
    fun `when load more search user is called, should append new users to existing list`() =
        runTest {
            // Given
            val initialUsers = mutableListOf<User>()
            val pageSize = ConfigApi.PAGE_SIZE
            val query = "any search"
            repeat(pageSize) {
                initialUsers.add(User(it, "user-${it}", "avatar $it"))
            }

            val moreUsers = listOf(
                User(id = 11, username = "user11", avatar = "avatar11"),
                User(id = 12, username = "user12", avatar = "avatar12")
            )
            repository.addUsers(initialUsers)

            // When - First load
            viewmodel.onAction(UserListAction.OnUserSearch(query))
            advanceUntilIdle()

            // Then - Verify initial load
            var state = viewmodel.state.first()
            assertEquals(initialUsers, state.users)
            assertTrue(viewmodel.hasNextPage)

            // When - Load more
            repository.clearUser() // Clear previous users
            repository.addUsers(moreUsers) // Add new users for next page
            viewmodel.onAction(UserListAction.OnScrollToBottom)
            advanceUntilIdle()

            // Then - Verify appended users
            state = viewmodel.state.first()
            assertEquals(initialUsers + moreUsers, state.users)
            assertEquals(query, state.query)
            assertFalse(viewmodel.hasNextPage)
        }

    @Test
    fun `when load more search user is called, and get error`() = runTest {
        // Given
        val initialUsers = mutableListOf<User>()
        val pageSize = ConfigApi.PAGE_SIZE
        val query = "any search"
        val lastPage = 2
        repeat(pageSize) {
            initialUsers.add(User(it, "user-${it}", "avatar $it"))
        }

        repository.addUsers(initialUsers)

        // When - First load
        viewmodel.onAction(UserListAction.OnUserSearch(query))
        advanceUntilIdle()

        // Then - Verify initial load
        var state = viewmodel.state.first()
        assertEquals(initialUsers, state.users)
        assertTrue(viewmodel.hasNextPage)
        assertEquals(lastPage, viewmodel.currentPage)

        // When - Load more
        repository.clearUser()
        repository.setShouldReturnError(true)
        viewmodel.onAction(UserListAction.OnScrollToBottom)
        advanceUntilIdle()

        // Then - Verify appended users
        state = viewmodel.state.first()
        assertEquals(initialUsers, state.users)
        assertEquals(lastPage, viewmodel.currentPage)
        assertTrue(viewmodel.hasNextPage)
        assertEquals(query, state.query)

        val event = viewmodel.events.first()
        assertTrue(event is UserListEvent.Error)
    }

}