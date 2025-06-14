package com.moneyfoward.githubclone.github.presentation.user_list

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moneyfoward.githubclone.core.presentation.ObserverAsEvents
import com.moneyfoward.githubclone.core.presentation.toString
import com.moneyfoward.githubclone.github.presentation.user_list.components.InfiniteUserList
import com.moneyfoward.githubclone.github.presentation.user_list.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserListScreen(
    modifier: Modifier,
    viewmodel: UserListViewModel = koinViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val state = viewmodel.state.collectAsStateWithLifecycle()
    ObserverAsEvents(events = viewmodel.events) { event ->
        when (event) {
            is UserListEvent.Error -> {
                Toast
                    .makeText(
                        context,
                        event.error.toString(context),
                        Toast.LENGTH_LONG,
                    ).show()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchBar(
                        query = state.value.query ?: "",
                        onTextChange = { query ->
                            viewmodel.onAction(UserListAction.OnUserSearch(query))
                        },
                    )
                }
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.value.isRefreshing,
            onRefresh = { viewmodel.onAction(UserListAction.OnRefresh) },
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            InfiniteUserList(
                items = state.value.users,
                isLoadingMore = state.value.isLoadingMore,
                onLoadMore = { viewmodel.onAction(UserListAction.OnScrollToBottom) },
                onClickItem = { user ->
                    navController.navigate("user/${user.username}") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
