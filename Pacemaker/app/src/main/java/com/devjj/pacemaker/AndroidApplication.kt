package com.devjj.pacemaker

import android.app.Application

class AndroidApplication : Application() {
/*
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
*/

    override fun onCreate() {
        super.onCreate()
        //this.injectMembers()
        //this.initializeLeakDetection()
    }

    //private fun injectMembers() = appComponent.inject(this)
/*
    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }
    */
}