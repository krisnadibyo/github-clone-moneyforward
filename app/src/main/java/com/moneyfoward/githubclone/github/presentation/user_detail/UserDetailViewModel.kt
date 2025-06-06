package com.moneyfoward.githubclone.github.presentation.user_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyfoward.githubclone.core.data.networking.ConfigApi
import com.moneyfoward.githubclone.core.domain.onError
import com.moneyfoward.githubclone.core.domain.onSuccess
import com.moneyfoward.githubclone.github.domain.RepoDataSource
import com.moneyfoward.githubclone.github.domain.UserDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserDetailViewModel(
    private val userRepository: UserDataSource,
    private val userRepoRepository: RepoDataSource,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    // UI State
    private val _state = MutableStateFlow<UserDetailState>(UserDetailState())
    val state = _state
        .onStart {
            if (!isFirstLoaded) {
                isFirstLoaded = true
                savedStateHandle["isLoaded"] = true
                refresh()

            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserDetailState()
        )
    // Business State
    private var isFirstLoaded = savedStateHandle["isLoaded"] ?: false
    private val username = checkNotNull(savedStateHandle.get<String>("username"))
    private var currentPage = 1
    private var hasNextPage = true
    private var isLoading = false

    //Effect
    private val _events = Channel<UserDetailEvent>()
    val events = _events.receiveAsFlow()

    // Intent
    fun onAction(action: UserDetailAction) {
        when(action) {
            UserDetailAction.OnScrollToBottom -> {
                loadMore()
            }
            UserDetailAction.OnRefresh -> {
                refresh()
            }
        }
    }

    private fun refresh() {
        currentPage = 1
        hasNextPage = true
        fetchUser()
        fetchRepository(reset = true)
    }

    private fun loadMore() {
        if (isLoading || !hasNextPage) return
        fetchRepository()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            isLoading = true
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }
            userRepository.getUser(username)
                .onSuccess { newUser ->
                    _state.update {
                        it.copy(
                            user = newUser,
                            isRefreshing = false
                        )
                    }
                    isLoading = false
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isRefreshing = false
                        )
                    }
                    _events.send(UserDetailEvent.Error(error))
                }
        }
    }

    private fun fetchRepository(reset: Boolean = false) {
        viewModelScope.launch {
            isLoading = true
            if (reset) {
                _state.update {
                    it.copy(
                        isRefreshing = true
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoadingMore = true
                    )
                }
            }
            userRepoRepository.getUserRepos(username, currentPage)
                .onSuccess { result ->
                    val filteredResult = result.filter { !it.isFork }
                    _state.update {
                        val newRepos = if (reset) filteredResult else it.repositories.plus(filteredResult)
                        it.copy(
                            repositories = newRepos,
                            isLoadingMore = false,
                            isRefreshing = false
                        )
                    }
                    // Fetch Language Info because there's no language label on this API
                    filteredResult.forEach {
                        fetchLanguage(it.fullName)
                    }
                    currentPage += 1
                    hasNextPage = result.size >= ConfigApi.PAGE_SIZE
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            isRefreshing = false
                        )
                    }
                    _events.send(UserDetailEvent.Error(error))
                }
            isLoading = false
        }
    }

    private fun fetchLanguage(repo: String) {
        viewModelScope.launch {
            userRepoRepository.getLanguageRepo(repo)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            repositories = it.repositories.map { userRepo ->
                                if (userRepo.fullName == repo) {
                                    userRepo.copy(
                                        language = result
                                    )
                                } else {
                                    userRepo
                                }
                            }
                        )
                    }
                }
                .onError {
                    // Do Nothing
                }
        }
    }



}