package com.moneyfoward.githubclone.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.moneyfoward.githubclone.github.presentation.repository_detail.RepositoryDetailScreen
import com.moneyfoward.githubclone.github.presentation.user_detail.UserDetailScreen
import com.moneyfoward.githubclone.github.presentation.user_list.UserListScreen


@Composable
fun AppNavHost(
    modifier: Modifier
) {
    val navController = rememberNavController()
    val graph = navController.createGraph(startDestination = ScreenRoute.UserList.route) {
        composable(route = ScreenRoute.UserList.route) {
            UserListScreen(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController
            )
        }
        composable(
            route = ScreenRoute.UserDetail.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) {
            UserDetailScreen(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController
            )
        }

        composable(
            route = ScreenRoute.RepositoryDetail.route
        ) { backstack ->
            val username = backstack.arguments?.getString("username") ?: ""
            val repo = backstack.arguments?.getString("repo") ?: ""
            RepositoryDetailScreen(
                repoName = "${username}/${repo}",
                navController = navController
            )

        }
    }
    NavHost(
        navController = navController,
        graph = graph,
        modifier = modifier
    )

}