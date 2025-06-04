package com.moneyfoward.githubclone.user.presentation.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyfoward.githubclone.core.domain.onError
import com.moneyfoward.githubclone.core.domain.onSuccess
import com.moneyfoward.githubclone.user.domain.UserDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserListViewModel (
    private val repository: UserDataSource
) : ViewModel() {

    //State
    private val _state = MutableStateFlow<UserListState>(UserListState())
    val state = _state
        .onStart {
            getUsers()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserListState()
        )

    //Effect
    private val _event = Channel<UserListState>()
    val event = _event.receiveAsFlow()

    //Intent
    fun onAction(action: UserListAction) {
        when(action) {
            is UserListAction.onUserClick -> {

            }

            UserListAction.onRefresh -> {

            }
            UserListAction.onScrollToBottom -> {

            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            repository
                .getUsers(0)
                .onSuccess { users ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            users = users
                        )
                    }
                }
                .onError {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
        }
    }




}