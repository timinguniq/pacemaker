package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityStatisticsBinding
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsFragment
import com.google.android.gms.ads.AdRequest
import javax.inject.Inject

class StatisticsActivity : BaseActivity(){

    companion object {
        fun callingIntent(context: Context) = Intent(context,StatisticsActivity::class.java)
    }

    override var layout = R.layout.activity_statistics
    override var fragmentId = R.id.aStatistics_flo_container
    override fun fragment() = StatisticsFragment()

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var setting: SettingSharedPreferences
    @Inject lateinit var statisticsDB : StatisticsDatabase

    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_statistics)
        appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    private fun initializeView() {
        if(setting.isNightMode){
            window.statusBarColor = loadColor(this,R.color.grey_444646)
            binding.aStatisticsCloTitle.setBackgroundResource(R.drawable.img_title_background_nighttime)
            binding.aStatisticsFloContainer.setBackgroundColor(loadColor(this,R.color.grey_444646))
        }else{
            window.statusBarColor = loadColor(this,R.color.blue_5F87D6)
            binding.aStatisticsCloTitle.setBackgroundResource(R.drawable.img_title_background_daytime)
            binding.aStatisticsFloContainer.setBackgroundColor(loadColor(this,R.color.white_FFFFFF))
        }

        val adRequest = AdRequest.Builder().build()
        binding.aStatisticsAdView.loadAd(adRequest)
    }

}