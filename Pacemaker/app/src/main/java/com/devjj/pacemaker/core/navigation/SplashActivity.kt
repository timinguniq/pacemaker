package com.devjj.pacemaker.core.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.ApplicationComponent
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.functional.Dlog
import com.google.android.gms.ads.MobileAds
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    @Inject internal lateinit var navigator: Navigator
    @Inject internal lateinit var dbEx: ExerciseDatabase
    @Inject internal lateinit var dbExHi: ExerciseHistoryDatabase
    // TODO : 테스트 코드 나중에 삭제
    @Inject lateinit var setting: SettingSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        appComponent.inject(this)
        Log.d("test", "onCreate SplashActivity")

        //log를 위한 코드
        val app = AndroidApplication()
        AndroidApplication.DEBUG = app.isDebuggable(this)
        //

    }

    override fun onResume() {
        super.onResume()

        // TODO : 2초 뒤 화면전환 구현하기 나중에
        Handler().postDelayed({
            navigator.showMain(this)
        }, 1700)

        // 크러시 테스트 함수.
        //crashTest()

        // 테스트 광고
        MobileAds.initialize(this) {}

        // TODO : 예시 코드 나중에 지워야 됨.
        Dlog.d("test")

    }

    // 크러쉬 테스트 함수.
    fun crashTest(){
        val crashButton = Button(this)
        crashButton.text = "Crash!"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
    }
}