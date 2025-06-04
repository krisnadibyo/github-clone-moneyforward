package com.moneyfoward.githubclone

import android.app.Application
import com.moneyfoward.githubclone.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GithubCloneApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GithubCloneApp)
            androidLogger()
            modules(appModule)
        }
    }
}