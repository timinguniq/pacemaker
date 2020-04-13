package com.devjj.pacemaker.core.di

import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.core.di.viewmodel.ViewModelModule
import com.devjj.pacemaker.core.navigation.SplashActivity
import com.devjj.pacemaker.features.pacemaker.*
import com.devjj.pacemaker.features.pacemaker.addition.AdditionFragment
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.history.HistoryListener
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(splashActivity: SplashActivity)
    fun inject(pacemakerActivity: PacemakerActivity)
    fun inject(optionActivity: OptionActivity)
    fun inject(additionActivity: AdditionActivity)
    fun inject(playPopupActivity: PlayPopupActivity)
    fun inject(historyDetailActivity: HistoryDetailActivity)

    fun inject(homeFragment: HomeFragment)
    fun inject(historyFragment: HistoryFragment)
    fun inject(optionFragment: OptionFragment)
    fun inject(additionFragment: AdditionFragment)
    fun inject(playPopupFragment: PlayPopupFragment)
    fun inject(historyDetailFragment: HistoryDetailFragment)

}