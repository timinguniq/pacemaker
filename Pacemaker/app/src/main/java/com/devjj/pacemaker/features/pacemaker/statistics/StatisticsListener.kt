package com.devjj.pacemaker.features.pacemaker.statistics

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.databinding.FragmentStatisticsBinding

class StatisticsListener (val activity: Activity,
                          val binding: FragmentStatisticsBinding,
                          val setting : SettingSharedPreferences,val statisticsViewModel :StatisticsViewModel){
    val MAX_BMI = 50
    val MIN_BMI = 0

    fun initListener(){

        binding.fStatisticsSwcMonthly.setOnCheckedChangeListener{ buttonView, isChecked ->
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

        binding.fStatisticsSwcModeBmi.setOnCheckedChangeListener { buttonView, isChecked ->
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

        binding.fStatisticsFloBmiPlus.setOnClickListener {
            var bmi = binding.fStatisticsTvBmi.text.toString().toInt()
            bmi++

            if(bmi >= MAX_BMI){
                binding.fStatisticsTvBmi.text = MAX_BMI.toString()
                setting.bmi = MAX_BMI
            }else{
                binding.fStatisticsTvBmi.text = bmi.toString()
                setting.bmi = bmi
            }
            binding.fStatisticsTvModeBmi.text = activity.getString(R.string.template_bmi_str,setting.bmi)

            if(setting.isShowbmi)
                statisticsViewModel.loadStatistics()
        }

        binding.fStatisticsFloBmiMinus.setOnClickListener {
            var bmi = binding.fStatisticsTvBmi.text.toString().toInt()
            bmi--

            if(bmi <= MIN_BMI ){
                binding.fStatisticsTvBmi.text = MIN_BMI.toString()
                setting.bmi = MIN_BMI
            }else{
                binding.fStatisticsTvBmi.text = bmi.toString()
                setting.bmi = bmi
            }
            binding.fStatisticsTvModeBmi.text = activity.getString(R.string.template_bmi_str,setting.bmi)

            if(setting.isShowbmi)
                statisticsViewModel.loadStatistics()
        }

        binding.fStatisticsTvBmiLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.template_bmi_link_uri)))
            activity.startActivity(intent)
        }

    }
}