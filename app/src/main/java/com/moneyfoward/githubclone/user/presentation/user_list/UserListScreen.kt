package com.moneyfoward.githubclone.user.presentation.user_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneyfoward.githubclone.ui.theme.GithubCloneTheme
import com.moneyfoward.githubclone.user.presentation.user_list.components.UserItem
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserListScreen(
    modifier: Modifier,
    viewmodel: UserListViewModel = koinViewModel()
) {
    val state = viewmodel.state.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Github Clone"
                    )
                }
            )
        }

    ) {
        PullToRefreshBox(
            isRefreshing = state.value.isLoading,
            onRefresh = {},
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(state.value.users) {
                    UserItem(
                        user = it
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UserListScreenPreview() {
    GithubCloneTheme {
        UserListScreen(
            modifier = Modifier,
            viewmodel = koinViewModel()
        )
    }
}