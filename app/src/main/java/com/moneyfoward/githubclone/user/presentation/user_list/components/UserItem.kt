package com.moneyfoward.githubclone.user.presentation.user_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.moneyfoward.githubclone.ui.theme.GithubCloneTheme
import com.moneyfoward.githubclone.user.domain.User


@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge
            )
        }


    }

}

@Preview
@Composable
private fun UserItemPreview() {
    GithubCloneTheme {
        UserItem(
            modifier = Modifier,
            user = User(
                id = 1,
                username = "octopat",
                avatar = "https://avatars.githubusercontent.com/u/4212658?v=4"
            )
        )
    }
}