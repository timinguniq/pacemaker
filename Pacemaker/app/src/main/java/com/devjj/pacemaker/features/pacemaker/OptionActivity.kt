package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityOptionBinding
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment
import com.google.android.gms.ads.AdRequest
import javax.inject.Inject

class OptionActivity : BaseActivity() {
    companion object {
        fun callingIntent(context: Context) = Intent(context, OptionActivity::class.java)
    }

    @Inject
    lateinit var setting: SettingSharedPreferences

    private lateinit var binding: ActivityOptionBinding

    override fun fragment() = OptionFragment()

    override var layout = R.layout.activity_option
    override var fragmentId = R.id.aOption_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appComponent.inject(this)
        initializeView()
        Dlog.d( "onCreate OptionActivity")
    }

    // OptionActivity 초기화 하는 함수
    private fun initializeView() {
        setColor()
        val adRequest = AdRequest.Builder().build()
        binding.aOptionAdView.loadAd(adRequest)
    }

    fun setColor(){
        when(setting.isNightMode){
            true->{
                window.statusBarColor = loadColor(this,R.color.grey_444646)
                binding.aOptionCloTitle.setBackgroundResource(R.drawable.img_title_background_nighttime)
                binding.aOptionFloContainer.setBackgroundColor(loadColor(this,R.color.grey_444646))
            }
            false->{
                window.statusBarColor = loadColor(this,R.color.blue_5C83CF)
                binding.aOptionCloTitle.setBackgroundResource(R.drawable.img_title_background_daytime)
                binding.aOptionFloContainer.setBackgroundColor(loadColor(this,R.color.white_FFFFFF))
            }
        }
    }

    fun getBinding() = binding
}
