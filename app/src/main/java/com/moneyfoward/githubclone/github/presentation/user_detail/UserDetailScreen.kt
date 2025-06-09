package com.moneyfoward.githubclone.github.presentation.user_detail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moneyfoward.githubclone.R
import com.moneyfoward.githubclone.core.presentation.ObserverAsEvents
import com.moneyfoward.githubclone.core.presentation.toString
import com.moneyfoward.githubclone.github.presentation.user_detail.components.UserInfoSection
import com.moneyfoward.githubclone.github.presentation.user_detail.components.UserRepoItem
import com.moneyfoward.githubclone.ui.theme.Dimens
import com.moneyfoward.githubclone.ui.theme.Typography
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    modifier: Modifier,
    viewModel: UserDetailViewModel = koinViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val shouldLoadMore =
        remember {
            derivedStateOf {
                val lastVisibleItem =
                    listState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: -1
                val totalItems = listState.layoutInfo.totalItemsCount
                lastVisibleItem >= totalItems - 1 && !state.value.isLoadingMore
            }
        }
    val loadMore = {
        viewModel.onAction(UserDetailAction.OnScrollToBottom)
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !state.value.repositories.isEmpty()) {
            loadMore()
        }
    }

    ObserverAsEvents(events = viewModel.events) { event ->
        when (event) {
            is UserDetailEvent.Error -> {
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
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) {
        PullToRefreshBox(
            isRefreshing = state.value.isRefreshing,
            onRefresh = { viewModel.onAction(UserDetailAction.OnRefresh) },
            modifier =
                modifier
                    .padding(it)
                    .padding(horizontal = Dimens.ScreenPadding),
        ) {
            if (!state.value.isRefreshing) {
                LazyColumn(
                    state = listState,
                ) {
                    item {
                        UserInfoSection(
                            user = state.value.user,
                        )
                        Spacer(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                        )
                        Text(
                            text = stringResource(R.string.repositories),
                            style = Typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    items(state.value.repositories) { userRepo ->
                        UserRepoItem(
                            userRepo = userRepo,
                            onClick = { navController.navigate("repository/${userRepo.fullName}") },
                        )
                    }

                    if (state.value.isLoadingMore) {
                        item(key = "loading_more") {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
