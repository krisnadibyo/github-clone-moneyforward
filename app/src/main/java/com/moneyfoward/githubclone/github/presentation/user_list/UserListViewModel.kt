package com.moneyfoward.githubclone.github.presentation.user_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyfoward.githubclone.core.data.networking.ConfigApi
import com.moneyfoward.githubclone.core.domain.onError
import com.moneyfoward.githubclone.core.domain.onSuccess
import com.moneyfoward.githubclone.github.domain.GithubDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: GithubDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // UI State
    private val searchQueryFlow = MutableStateFlow("")
    private val _state = MutableStateFlow<UserListState>(UserListState())
    val state =
        _state
            .onStart {
                if (!isFirstLoaded) {
                    isFirstLoaded = true
                    savedStateHandle["isLoaded"] = true
                    refresh()
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                UserListState(),
            )

    // Business State
    private var isFirstLoaded = savedStateHandle["isLoaded"] ?: false
    var currentPage = 1
        private set
    var hasNextPage = true
        private set
    private var sinceId = 0
    private var isLoading = false

    // Effect
    private val _events = Channel<UserListEvent>()
    val events = _events.receiveAsFlow()

    // Intent
    fun onAction(action: UserListAction) {
        when (action) {
            is UserListAction.OnRefresh -> {
                refresh()
            }

            is UserListAction.OnScrollToBottom -> {
                loadMore()
            }

            is UserListAction.OnUserSearch -> {
                onSearch(action.query)
            }
        }
    }

    init {
        observeSearchQuery()
    }

    private fun onSearch(query: String) {
        searchQueryFlow.value = query
        _state.update {
            it.copy(
                query = query,
            )
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    currentPage = 1
                    sinceId = 0
                    fetchUsers(reset = true)
                }
        }
    }

    private fun refresh() {
        _state.update {
            it.copy(
                query = null,
            )
        }
        currentPage = 1
        sinceId = 0
        hasNextPage = true
        fetchUsers(reset = true)
    }

    private fun loadMore() {
        if (isLoading || !hasNextPage) return
        fetchUsers()
    }

    private fun fetchUsers(reset: Boolean = false) {
        viewModelScope.launch {
            isLoading = true
            val query = _state.value.query
            val isSearchMode = !query.isNullOrEmpty()
            if (reset) {
                _state.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoadingMore = true,
                    )
                }
            }
            if (isSearchMode) {
                repository
                    .getSearchUsers(query, currentPage)
                    .onSuccess { result ->
                        _state.update {
                            val newUsers = if (reset) result else it.users.plus(result)
                            it.copy(
                                users = newUsers,
                                isRefreshing = false,
                                isLoadingMore = false,
                            )
                        }
                        currentPage += 1
                        hasNextPage = result.size >= ConfigApi.PAGE_SIZE
                    }.onError { error ->
                        _state.update {
                            it.copy(
                                isRefreshing = false,
                                isLoadingMore = false,
                            )
                        }
                        _events.send(UserListEvent.Error(error))
                    }
            } else {
                repository
                    .getUsers(sinceId)
                    .onSuccess { result ->
                        _state.update {
                            val newUsers = if (reset) result else it.users.plus(result)
                            it.copy(
                                users = newUsers,
                                isRefreshing = false,
                                isLoadingMore = false,
                            )
                        }
                        sinceId =
                            if (_state.value.users.isNotEmpty()) {
                                _state.value.users
                                    .last()
                                    .id
                            } else {
                                0
                            }
                        hasNextPage = result.size >= ConfigApi.PAGE_SIZE
                    }.onError { error ->
                        _state.update {
                            it.copy(
                                isRefreshing = false,
                                isLoadingMore = false,
                            )
                        }
                        _events.send(UserListEvent.Error(error))
                    }
            }
            isLoading = false
        }
    }
}
