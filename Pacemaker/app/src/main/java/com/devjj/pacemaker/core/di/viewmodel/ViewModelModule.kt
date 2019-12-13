package com.devjj.pacemaker.core.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule{
    @Binds
    internal abstract fun bindViewModeFactory(factory:ViewModelFactory):ViewModelProvider.Factory
}