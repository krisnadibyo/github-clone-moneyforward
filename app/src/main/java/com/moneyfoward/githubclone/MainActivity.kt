package com.moneyfoward.githubclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import com.moneyfoward.githubclone.ui.nav.AppNavHost
import com.moneyfoward.githubclone.ui.theme.GithubCloneTheme
import com.moneyfoward.githubclone.ui.theme.Neutral

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubCloneTheme {
                AppNavHost(
                    modifier = Modifier
                )
            }
        }
    }
}