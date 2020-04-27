package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_option.*
import javax.inject.Inject

class OptionActivity : BaseActivity() {
    @Inject
    lateinit var setting: SettingSharedPreferences

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
        setColor()
        val adRequest = AdRequest.Builder().build()
        aOption_adView.loadAd(adRequest)
    }

    fun setColor(){
        when(setting.isNightMode){
            true->{
                window.statusBarColor = getColor(R.color.grey_444646)
                aOption_clo_title.setBackgroundResource(R.drawable.img_title_background_nighttime)
                aOption_flo_container.setBackgroundColor(getColor(R.color.grey_444646))
            }
            false->{
                window.statusBarColor = getColor(R.color.blue_5C83CF)
                aOption_clo_title.setBackgroundResource(R.drawable.img_title_background_daytime)
                aOption_flo_container.setBackgroundColor(getColor(R.color.white_FFFFFF))
            }
        }
    }
}
