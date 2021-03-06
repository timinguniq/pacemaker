package com.devjj.pacemaker.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devjj.pacemaker.features.pacemaker.addition.AdditionViewModel
import com.devjj.pacemaker.features.pacemaker.history.HistoryViewModel
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailViewModel
import com.devjj.pacemaker.features.pacemaker.home.HomeViewModel
import com.devjj.pacemaker.features.pacemaker.option.OptionViewModel
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupViewModel
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsViewModel
import com.devjj.pacemaker.features.pacemaker.tutorial.TutorialViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule{
    @Binds
    internal abstract fun bindViewModelFactory(factory:ViewModelFactory):ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindsHistoryViewModel(historyViewModel: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OptionViewModel::class)
    abstract fun bindsOptionViewModel(optionViewModel: OptionViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdditionViewModel::class)
    abstract fun bindsAdditionViewModel(additionViewModel: AdditionViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryDetailViewModel::class)
    abstract fun bindsHistoryDetailViewModel(historyDetailViewModel : HistoryDetailViewModel) :ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayPopupViewModel::class)
    abstract fun bindsPlayPopupViewModel(playPopupViewModel: PlayPopupViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun bindsStatisticsViewModel(statisticsViewModel: StatisticsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TutorialViewModel::class)
    abstract fun bindsTutorialViewModel(tutorialViewModel: TutorialViewModel) : ViewModel

}