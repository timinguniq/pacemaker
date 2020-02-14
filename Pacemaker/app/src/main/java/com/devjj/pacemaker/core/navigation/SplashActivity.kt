package com.devjj.pacemaker.core.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.util.Log
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.ApplicationComponent
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import com.google.android.gms.ads.MobileAds
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    @Inject
    internal lateinit var navigator: Navigator
    @Inject
    internal lateinit var dbEx: ExerciseDatabase
    @Inject
    internal lateinit var dbExHi: ExerciseHistoryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appComponent.inject(this)
        Log.d("test", "onCreate SplashActivity")

    }

    override fun onResume() {
        super.onResume()

        // TODO : 2초 뒤 화면전환 구현하기 나중에
        Handler().postDelayed({
            navigator.showMain(this)
        }, 1500)

        // 테스트 광고
        MobileAds.initialize(this) {}
    }
}