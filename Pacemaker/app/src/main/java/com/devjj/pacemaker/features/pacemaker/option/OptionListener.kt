package com.devjj.pacemaker.features.pacemaker.option

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.databinding.FragmentOptionBinding


class OptionListener(
    val activity: Activity,
    val binding: FragmentOptionBinding,
    val setting: SettingSharedPreferences,
    val navigator: Navigator,
    val setColors: () -> Unit

) {
    val MAX_TIME = 300
    val MIN_TIME = 0
    fun initListener() {
        binding.fOptionSwcModeItem.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isNightMode = isChecked
            Dlog.d( "${loadColor(activity!!,R.color.grey_F9F9F9)}")
            setColors()
        }

        binding.fOptionSwcModeWeight.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isUpdateWeight = isChecked
        }

        binding.fOptionSwcModeHeight.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isUpdateHeight = isChecked
        }

        binding.fOptionFloIntervalPlus.setOnClickListener {
            var timeInSec = 60 * binding.fOptionTvIntervalTime.text.split(":")[0].toInt()
            timeInSec += binding.fOptionTvIntervalTime.text.split(":")[1].toInt()
            timeInSec += 5

            if (timeInSec >= MAX_TIME) {
                binding.fOptionTvIntervalTime.text =
                    activity.getString(R.string.unit_time_min_sec, MAX_TIME / 60, MAX_TIME % 60)
                setting.restTime = MAX_TIME
            } else {
                binding.fOptionTvIntervalTime.text =
                    activity.getString(R.string.unit_time_min_sec, timeInSec / 60, timeInSec % 60)
                setting.restTime = timeInSec
            }
        }

        binding.fOptionFloIntervalMinus.setOnClickListener {
            var timeInSec = 60 * binding.fOptionTvIntervalTime.text.split(":")[0].toInt()
            timeInSec += binding.fOptionTvIntervalTime.text.split(":")[1].toInt()
            timeInSec -= 5
            if (timeInSec <= MIN_TIME) {
                binding.fOptionTvIntervalTime.text =
                    activity.getString(R.string.unit_time_min_sec, MIN_TIME / 60, MIN_TIME % 60)
                setting.restTime = MIN_TIME
            } else {
                binding.fOptionTvIntervalTime.text =
                    activity.getString(R.string.unit_time_min_sec, timeInSec / 60, timeInSec % 60)
                setting.restTime = timeInSec
            }
        }

        binding.fOptionCloOptionTutorial.setOnClickListener {
            navigator.showTutorial(activity)
        }

        binding.fOptionCloFeedback.setOnClickListener {
            var emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf("inty.ashever@gmail.com","timing.uniq@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                activity.getString(R.string.template_feedback_title)
            )
            emailIntent.putExtra(Intent.EXTRA_TEXT, "")
            activity.startActivity(emailIntent)

        }

        binding.fOptionCloRateUs.setOnClickListener {
            val rateIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + activity.packageName)
            )
            activity.startActivity(rateIntent)
        }

        binding.fOptionCloShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.foption_msg_name_str))
            var shareMessage = "\n${activity.getString(R.string.foption_msg_share_str)}\n\n"
            shareMessage =
                shareMessage + "http://play.google.com/store/apps/details?id=" + activity.packageName + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"))

        }

        binding.fOptionCloOpenSource.setOnClickListener {
            navigator.showLicense(activity);
        }
    }
}