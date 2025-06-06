package com.moneyfoward.githubclone.github.presentation.user_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.moneyfoward.githubclone.github.domain.model.UserRepo
import com.moneyfoward.githubclone.github.domain.model.stars
import com.moneyfoward.githubclone.ui.theme.DeepSaffron
import com.moneyfoward.githubclone.ui.theme.GoldenYellow


@Composable
fun UserRepoItem(
    modifier: Modifier = Modifier,
    userRepo: UserRepo,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable{ onClick() }
            .padding(vertical = 4.dp)
    ) {

        Text(
            text = userRepo.name,
            style = Typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
        Text(
            text = userRepo.description,
            style = Typography.bodySmall,
            fontSize = 11.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                contentDescription = null,
                tint = GoldenYellow
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = userRepo.stars(),
                style = Typography.bodySmall,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                modifier = Modifier.size(10.dp),
                color = DeepSaffron,
                shape = CircleShape
            ) {
                Spacer(modifier = Modifier.size(12.dp))
            }
            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = userRepo.language,
                style = Typography.bodySmall,
                fontSize = 10.sp
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
        UserRepoItem(
            userRepo = dummyUserRepo,
            modifier = Modifier.background(Neutral),
            onClick = {}
        )
    }

}

private val dummyUserRepo = UserRepo(
    id = 12,
    name = "peerless",
    fullName = "krisna/peerless",
    isPrivate = false,
    description = "Peerless is standalone application which runs games on macos",
    isFork = false,
    htmlUrl = "",
    stars = 132312,
    language = "Ruby"
)

