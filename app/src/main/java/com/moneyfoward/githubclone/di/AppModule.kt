package com.moneyfoward.githubclone.di

import com.moneyfoward.githubclone.core.data.networking.HttpClientFactory
import com.moneyfoward.githubclone.user.data.networking.RemoteUserDataSource
import com.moneyfoward.githubclone.user.domain.UserDataSource
import com.moneyfoward.githubclone.user.presentation.user_list.UserListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }

    // Data Source and Repository
    singleOf(::RemoteUserDataSource).bind<UserDataSource>()

    //Viewmodel
    viewModelOf(::UserListViewModel)

}