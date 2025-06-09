package com.moneyfoward.githubclone.github.presentation.user_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moneyfoward.githubclone.R
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.ui.theme.NeutralLight
import com.moneyfoward.githubclone.ui.theme.Typography

@Composable
fun UserInfoSection(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = user.avatar,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(
                modifier = Modifier.width(12.dp),
            )
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (user.bio.isNotEmpty()) {
            Text(
                text = user.bio,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (user.location.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(12.dp),
                )
                Text(
                    text = user.location,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 2.dp),
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
        if (user.company.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_work),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(12.dp),
                )
                Text(
                    text = user.company,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 2.dp),
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(12.dp),
            )
            Text(
                text =
                    buildAnnotatedString {
                        append(user.followers.toString())
                        append(" followers • ")
                        append(user.following.toString())
                        append(" following")
                    },
                fontSize = 10.sp,
                style = Typography.bodySmall,
                modifier = Modifier.padding(horizontal = 2.dp),
            )
        }
    }
}

@Preview
@Composable
private fun UserInfoSectionPreview() {
    MaterialTheme {
        UserInfoSection(
            modifier = Modifier.background(NeutralLight),
            user =
                User(
                    id = 1,
                    username = "Kennabila",
                    name = "Ken Nabila Setya",
                    avatar = "https://avatars.githubusercontent.com/u/174864?v=4",
                    bio = "",
                    company = "",
                    location = "",
                ),
        )
    }
}
