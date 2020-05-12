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
import com.devjj.pacemaker.core.extension.APP_VERSION
import com.devjj.pacemaker.core.functional.Dlog
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_splash.*
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
        Dlog.d( "onCreate SplashActivity")

        //log를 위한 코드
        val app = AndroidApplication()
        AndroidApplication.DEBUG = app.isDebuggable(this)
        //

    }

    override fun onResume() {
        super.onResume()

        Dlog.d("APP_VERSION : $APP_VERSION")
        showVersionTextView()

        // TODO : 2초 뒤 화면전환 구현하기 나중에
        /*
        Handler().postDelayed({
            navigator.showMain(this)
        }, 2000)
*/
        // 테스트 광고
        MobileAds.initialize(this) {}

    }

    // 버전 텍스트에 옮기는 함수
    private fun showVersionTextView(){
        val version = "v$APP_VERSION"
        aSplash_tv_version.text = version
    }
}