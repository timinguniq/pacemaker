package com.devjj.pacemaker.features.pacemaker.statistics

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.gone
import com.devjj.pacemaker.core.extension.visible
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsListener (val activity: Activity, val setting : SettingSharedPreferences,val statisticsViewModel :StatisticsViewModel){
    val MAX_BMI = 50
    val MIN_BMI = 0
    fun initListener(){

        activity.fStatistics_swc_monthly.setOnCheckedChangeListener{ buttonView, isChecked ->
            when(isChecked){
                true->{
                    setting.isMonthly = isChecked
                }
                false->{
                    setting.isMonthly = isChecked
                }
            }
            statisticsViewModel.loadStatistics()
        }

        activity.fStatistics_swc_mode_bmi.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true ->{
                    setting.isShowbmi = isChecked
                }
                false ->{
                    setting.isShowbmi = isChecked
                }
            }
            statisticsViewModel.loadStatistics()
        }

        activity.fStatistics_iv_bmi_plus.setOnClickListener {
            var bmi = activity.fStatistics_tv_bmi.text.toString().toInt()
            bmi++

            if(bmi >= MAX_BMI){
                activity.fStatistics_tv_bmi.text = MAX_BMI.toString()
                setting.bmi = MAX_BMI
            }else{
                activity.fStatistics_tv_bmi.text = bmi.toString()
                setting.bmi = bmi
            }
            activity.fStatistics_tv_mode_bmi.text = activity.getString(R.string.template_bmi_str,setting.bmi)

            if(setting.isShowbmi)
                statisticsViewModel.loadStatistics()
        }

        activity.fStatistics_iv_bmi_minus.setOnClickListener {
            var bmi = activity.fStatistics_tv_bmi.text.toString().toInt()
            bmi--

            if(bmi <= MIN_BMI ){
                activity.fStatistics_tv_bmi.text = MIN_BMI.toString()
                setting.bmi = MIN_BMI
            }else{
                activity.fStatistics_tv_bmi.text = bmi.toString()
                setting.bmi = bmi
            }
            activity.fStatistics_tv_mode_bmi.text = activity.getString(R.string.template_bmi_str,setting.bmi)

            if(setting.isShowbmi)
                statisticsViewModel.loadStatistics()
        }

        activity.fStatistics_tv_bmi_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.template_bmi_link_uri)))
            activity.startActivity(intent)
        }

    }
}