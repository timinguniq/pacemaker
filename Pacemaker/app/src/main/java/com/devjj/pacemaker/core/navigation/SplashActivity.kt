package com.devjj.pacemaker.core.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.ApplicationComponent

class SplashActivity : AppCompatActivity() {


    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE){
        (application as AndroidApplication).appComponent
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
