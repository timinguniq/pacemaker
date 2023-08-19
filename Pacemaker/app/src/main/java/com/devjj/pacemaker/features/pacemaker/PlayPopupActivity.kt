package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityPlayPopupBinding
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupFragment
import com.devjj.pacemaker.features.pacemaker.playpopup.STOP_MODE
import com.devjj.pacemaker.features.pacemaker.playpopup.mode
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import com.google.android.gms.ads.AdRequest
import javax.inject.Inject


class PlayPopupActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PlayPopupActivity::class.java)
    }

    override var layout = R.layout.activity_play_popup
    override var fragmentId = R.id.aPlayPopup_flo_container

    @Inject lateinit var navigator: Navigator

    private lateinit var binding: ActivityPlayPopupBinding

    override fun fragment() = PlayPopupFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayPopupBinding.inflate(layoutInflater)
        appComponent.inject(this)
        initAd()
        Dlog.d( "onCreate PlayPopupActivity")
    }

    // 광고 초기화 하는 함수
    private fun initAd() {
        val adRequest = AdRequest.Builder().build()
        binding.aPlayPopupAdView.loadAd(adRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mode = STOP_MODE
        TimerService.setProgressTimer(false)
        TimerService.stopService(this)
    }
}
