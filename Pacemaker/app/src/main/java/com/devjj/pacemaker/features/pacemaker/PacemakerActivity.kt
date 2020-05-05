package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.CoreComponentFactory
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.round
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    @Inject lateinit var navigator: Navigator
    // TODO : ExerciseData 임시 추가를 위한 변수 나중에 삭제해야 됨.
    @Inject lateinit var db: ExerciseDatabase
    @Inject lateinit var setting: SettingSharedPreferences

    override var layout = R.layout.activity_pacemaker
    override var fragmentId = R.id.aPacemaker_flo_container

    private var FINISH_MAX_COUNT = 5
    private val FINSIH_MAX_COUNT_MIN_VALUE = 3
    private val FINSIH_MAX_COUNT_MAX_VALUE = 10
    private var backKeyTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pacemaker)
        appComponent.inject(this)
        Dlog.d( "onCreate PacemakerActivity")

        // FINSIH_MAX_COUNT 셋팅
        FINISH_MAX_COUNT = (((FINSIH_MAX_COUNT_MAX_VALUE - FINSIH_MAX_COUNT_MIN_VALUE) * Math.random()) +
                FINSIH_MAX_COUNT_MIN_VALUE).round(0).toInt()
    }

    override fun onResume() {
        super.onResume()
        initializeView()

        Dlog.d( "PacemakerActivity finish_max_count : $FINISH_MAX_COUNT")
    }

    override fun fragment() = HomeFragment()

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {

        // NavigationBottomView setting
        navigator.transitonNavigationBottomView(aPacemaker_bottom_navigation_view, supportFragmentManager,this)

        // settingImv clickListener
        aPacemaker_iv_setting.setOnClickListener {
            navigator.showOption(this)
        }

        // TODO : 여기서부터 그거 함수로 extension에 빼면 됨.
        if(!setting.isNightMode){
            // TODO : 여기로 들어오면 다크모드가 아니다.
            window.statusBarColor = getColor(R.color.blue_5F87D6)
            aPacemaker_clo_title.setBackgroundResource(R.drawable.img_title_background_daytime)
            aPacemaker_flo_container.setBackgroundColor(getColor(R.color.white_FFFFFF))
            aPacemaker_bottom_navigation_view.setBackgroundColor(getColor(R.color.grey_F9F9F9))
            aPacemaker_bottom_navigation_view.itemBackgroundResource = R.drawable.apacemaker_bottom_icon_bg_color_daytime
            aPacemaker_bottom_navigation_view.itemIconTintList = resources.getColorStateList(R.color.apacemaker_wm_bottom_icon_color,null)
        }else{
            // TODO : 여기로 들어오면 다크모드
            window.statusBarColor = getColor(R.color.grey_444646)
            aPacemaker_clo_title.setBackgroundResource(R.drawable.img_title_background_nighttime)
            aPacemaker_flo_container.setBackgroundColor(getColor(R.color.grey_606060))
            aPacemaker_bottom_navigation_view.setBackgroundColor(getColor(R.color.grey_444646))
            aPacemaker_bottom_navigation_view.itemBackgroundResource = R.drawable.apacemaker_bottom_icon_bg_color_nighttime
            aPacemaker_bottom_navigation_view.itemIconTintList = resources.getColorStateList(R.color.apacemaker_dm_bottom_icon_color,null)
        }

        // 광고 테스트 코드
        val adRequest = AdRequest.Builder().build()
        aPacemaker_adView.loadAd(adRequest)
        // TODO : 여기까지 인데. 광고 때문에 view로 매개변수 받아야 될 것 같음.
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        Dlog.d( "PacemakerActivity onBackPressed()")

        if(System.currentTimeMillis() - backKeyTime < 2000){
            Dlog.d( "PacemakerActivity if")
            setting.finishCount++
            Dlog.d( "PacemakerActivity setting.finishCount : ${setting.finishCount}")
            if(setting.finishCount >= FINISH_MAX_COUNT){
                setting.finishCount = 0
                // 전면 광고를 띄우는 메소드
                showInterstitialAd()
            }else{
                finishAffinity()
            }
        }else{
            val toastStr = resources.getString(R.string.apacemaker_tv_terminate_str)
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show()
        }
        backKeyTime = System.currentTimeMillis()
    }

    // 전면 광고를 셋팅하는 함수.
    private fun showInterstitialAd(){
        val AD_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

        // Create the InterstitialAd and set it up.
        val mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = AD_INTERSTITIAL_UNIT_ID
            adListener = (object : AdListener() {
                override fun onAdLoaded() {
                    Toast.makeText(this@PacemakerActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                    if (isLoaded) {
                        show()
                        finishAffinity()
                    }
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(this@PacemakerActivity,
                        "onAdFailedToLoad() with error code: $errorCode",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onAdClosed() {
                    Dlog.d( "onAdClosed")
                }
            })
        }

        mInterstitialAd.loadAd(AdRequest.Builder().build())
/*
        Handler().post{
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }
        }
        */
    }
}