package com.devjj.pacemaker.core.di

import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.core.navigation.SplashActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
}