package com.moneyfoward.githubclone.di

import com.moneyfoward.githubclone.core.data.networking.HttpClientFactory
import com.moneyfoward.githubclone.github.data.networking.RemoteGithubDataSource
import com.moneyfoward.githubclone.github.domain.GithubDataSource
import com.moneyfoward.githubclone.github.presentation.user_detail.UserDetailViewModel
import com.moneyfoward.githubclone.github.presentation.user_list.UserListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }

    // Data Source and Repository
    singleOf(::RemoteGithubDataSource).bind<GithubDataSource>()

    //Viewmodel
    viewModelOf(::UserListViewModel)
    viewModel { UserDetailViewModel(get(), get()) }

}