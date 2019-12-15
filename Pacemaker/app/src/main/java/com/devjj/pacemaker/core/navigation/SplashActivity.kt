package com.devjj.pacemaker.core.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.ApplicationComponent
import com.google.android.gms.ads.MobileAds
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {


    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE){
        (application as AndroidApplication).appComponent
    }

    @Inject internal lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appComponent.inject(this)

        // TODO : 2초 뒤 화면전환 구현하기 나중에
        Handler().postDelayed({
            navigator.showMain(this)
        },2000)

        // 테스트 광고
        MobileAds.initialize(this) {}
    }
}
