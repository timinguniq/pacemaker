package com.devjj.pacemaker.core.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.ApplicationComponent
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.APP_VERSION
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.databinding.ActivityPacemakerBinding
import com.devjj.pacemaker.databinding.ActivitySplashBinding
import com.google.android.gms.ads.MobileAds
import javax.inject.Inject
import com.google.firebase.crashlytics.FirebaseCrashlytics

class SplashActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    @Inject internal lateinit var navigator: Navigator
    @Inject lateinit var setting: SettingSharedPreferences

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appComponent.inject(this)
        Dlog.d( "onCreate SplashActivity")

        // Dlog를 위한 코드
        val app = AndroidApplication()
        AndroidApplication.DEBUG = app.isDebuggable(this)
        //

        // 정렬모드 초기화.
        setting.isEditMode = false

    }

    override fun onResume() {
        super.onResume()

        Dlog.d("APP_VERSION : $APP_VERSION")
        showVersionTextView()

        // TODO : 2초 뒤 화면전환 구현하기 나중에
        Handler().postDelayed({
            navigator.showMain(this)
        }, 2000)


        // Operations on FirebaseCrashlytics.
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setUserId("Pacemaker")

        // OPTIONAL: If crash reporting has been explicitly disabled previously, add:
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // 테스트 광고
        MobileAds.initialize(this) {}
    }

    // 버전 텍스트에 옮기는 함수
    private fun showVersionTextView(){
        val version = "v$APP_VERSION"
        binding.aSplashTvVersion.text = version
    }
}