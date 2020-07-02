package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.opensource.OpenSourceFragment
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_open_source.*

class OpenSourceActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, OpenSourceActivity::class.java)
    }

    override fun fragment() = OpenSourceFragment()

    override var layout = R.layout.activity_open_source
    override var fragmentId = R.id.aOpenSource_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()
        Dlog.d( "onCreate OptionActivity")
    }

    // OptionActivity 초기화 하는 함수
    private fun initializeView() {
        val adRequest = AdRequest.Builder().build()
        aOpenSource_adView.loadAd(adRequest)
    }
}