package com.devjj.pacemaker.features.pacemaker.option

import android.app.Activity
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import kotlinx.android.synthetic.main.fragment_option.*


class OptionListener (val activity: Activity,val setting : SettingSharedPreferences, val setColors : ()->Unit){
    val MAX_TIME = 300
    fun initListener(){
        activity.fOption_swc_mode_item.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isNightMode = isChecked
            Log.d("color", "${activity!!.getColor(R.color.base_grey)}")
            setColors()
        }

        activity.fOption_iv_interval_plus.setOnClickListener {
            var timeInSec = 60* activity.fOption_txv_interval_time.text.split(":")[0].toInt()
            timeInSec += activity.fOption_txv_interval_time.text.split(":")[1].toInt()
            timeInSec += 5

            if(timeInSec>=MAX_TIME){
                activity.fOption_txv_interval_time.text = activity.getString(R.string.unit_time_min_sec,MAX_TIME/60,MAX_TIME%60)
                setting.restTime = MAX_TIME
            }else{
                activity.fOption_txv_interval_time.text = activity.getString(R.string.unit_time_min_sec,timeInSec/60,timeInSec%60)
                setting.restTime = timeInSec
            }
        }

        activity.fOption_iv_interval_minus.setOnClickListener {
            var timeInSec = 60* activity.fOption_txv_interval_time.text.split(":")[0].toInt()
            timeInSec += activity.fOption_txv_interval_time.text.split(":")[1].toInt()
            timeInSec -= 5
            if(timeInSec <=0){
                activity.fOption_txv_interval_time.text = activity.getString(R.string.unit_time_min_sec,0,0)
                setting.restTime = 0
            }else {
                activity.fOption_txv_interval_time.text =
                    activity.getString(R.string.unit_time_min_sec, timeInSec / 60, timeInSec % 60)
                setting.restTime = timeInSec
            }
        }

        activity.fOption_txv_feedback.setOnClickListener {
        }

        activity.fOption_txv_rateUs.setOnClickListener {  }

        activity.fOption_txv_share.setOnClickListener {  }
    }
}