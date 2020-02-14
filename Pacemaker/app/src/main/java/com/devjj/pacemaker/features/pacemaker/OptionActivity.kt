package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_option.*

class OptionActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, OptionActivity::class.java)
    }

    override fun fragment() = OptionFragment()

    override var layout = R.layout.activity_option
    override var fragmentId = R.id.aOption_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()
        Log.d("test", "onCreate OptionActivity")
    }

    // OptionActivity 초기화 하는 함수
    private fun initializeView() {
        val adRequest = AdRequest.Builder().build()
        aOption_adView.loadAd(adRequest)
    }
}
