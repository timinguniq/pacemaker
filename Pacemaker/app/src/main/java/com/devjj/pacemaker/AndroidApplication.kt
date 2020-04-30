package com.devjj.pacemaker

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import com.devjj.pacemaker.core.di.ApplicationComponent
import com.devjj.pacemaker.core.di.ApplicationModule
import com.devjj.pacemaker.core.di.DaggerApplicationComponent
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import com.squareup.leakcanary.LeakCanary
import javax.inject.Singleton

class AndroidApplication : Application() {

    companion object{
        // Log를 위한 Debug 변수
        var DEBUG = false
    }

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }


    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
    }


    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

    fun isDebuggable(context: Context) : Boolean{
        var debuggable = false

        val pm = context.packageManager
        try {
            val appinfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = (0 != (appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
        } catch (e: PackageManager.NameNotFoundException) {
            /* debuggable variable will remain false */
            e.message
        }
        return debuggable
    }
}