package com.devjj.pacemaker.features.pacemaker

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupFragment
import com.devjj.pacemaker.features.pacemaker.playpopup.STOP_MODE
import com.devjj.pacemaker.features.pacemaker.playpopup.mode
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_addition.*
import kotlinx.android.synthetic.main.activity_play_popup.*
import javax.inject.Inject
import kotlin.math.roundToInt


class PlayPopupActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PlayPopupActivity::class.java)
    }

    override var layout = R.layout.activity_play_popup
    override var fragmentId = R.id.aPlayPopup_flo_container

    @Inject lateinit var navigator: Navigator

    override fun fragment() = PlayPopupFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initAd()
        Log.d("test", "onCreate PlayPopupActivity")
    }

    // 광고 초기화 하는 함수
    private fun initAd() {
        val adRequest = AdRequest.Builder().build()
        aPlayPopup_adView.loadAd(adRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mode = STOP_MODE
        TimerService.setProgressTimer(false)
        TimerService.stopService(this)
    }
}
