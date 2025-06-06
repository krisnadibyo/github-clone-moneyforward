package com.moneyfoward.githubclone.user.presentation.user_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneyfoward.githubclone.R
import com.moneyfoward.githubclone.ui.theme.Neutral
import com.moneyfoward.githubclone.ui.theme.Typography
import com.moneyfoward.githubclone.user.domain.UserRepo
import com.moneyfoward.githubclone.user.domain.dummyUserRepo
import com.moneyfoward.githubclone.user.domain.stars


@Composable
fun UserRepositorySection(
    modifier: Modifier = Modifier,
    userRepos: List<UserRepo>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onClickRepo: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 1 && !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !userRepos.isEmpty()) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState
    ) {
        item(key = "header") {
            Text(
                text = "Repositories",
                style = Typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(userRepos) { userRepo ->
            UserRepoCard(
                userRepo = userRepo,
                onClick = { onClickRepo(userRepo.htmlUrl) }
            )
        }

        if (isLoadingMore) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

        }
    }

}

@Composable
fun UserRepoCard(
    modifier: Modifier = Modifier,
    userRepo: UserRepo,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable{ onClick() }
            .padding(8.dp)
    ) {

        Text(
            text = userRepo.name,
            style = Typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = userRepo.description,
            style = Typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                contentDescription = null,
                tint = Color.Yellow
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = userRepo.stars(),
                style = Typography.bodySmall,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                modifier = Modifier.size(12.dp),
                color = Color.Red,
                shape = CircleShape
            ) {
                Spacer(modifier = Modifier.size(12.dp))
            }
            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = userRepo.languageLink,
                style = Typography.bodySmall,
                fontSize = 11.sp,
                color = Color.Gray
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 2.dp),
            thickness = 0.25.dp,
            color = Color.LightGray

        )

    }



}

@Preview
@Composable
private fun UserRepoCardPreview() {
    MaterialTheme {
        UserRepoCard(
            userRepo = dummyUserRepo,
            modifier = Modifier.background(Neutral),
            onClick = {}
        )
    }

}
