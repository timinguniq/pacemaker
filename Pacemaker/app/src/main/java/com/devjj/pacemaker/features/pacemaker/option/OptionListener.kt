package com.devjj.pacemaker.features.pacemaker.option

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.devjj.pacemaker.BuildConfig
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import kotlinx.android.synthetic.main.fragment_option.*
import javax.inject.Inject


class OptionListener(
    val activity: Activity,
    val setting: SettingSharedPreferences,
    val navigator: Navigator,
    val setColors: () -> Unit

) {


    val MAX_TIME = 300
    val MIN_TIME = 0
    fun initListener() {
        activity.fOption_swc_mode_item.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isNightMode = isChecked
            Dlog.d( "${loadColor(activity!!,R.color.grey_F9F9F9)}")
            setColors()
        }

        activity.fOption_swc_mode_weight.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isUpdateWeight = isChecked
        }

        activity.fOption_swc_mode_height.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isUpdateHeight = isChecked
        }

        activity.fOption_flo_interval_plus.setOnClickListener {
            var timeInSec = 60 * activity.fOption_tv_interval_time.text.split(":")[0].toInt()
            timeInSec += activity.fOption_tv_interval_time.text.split(":")[1].toInt()
            timeInSec += 5

            if (timeInSec >= MAX_TIME) {
                activity.fOption_tv_interval_time.text =
                    activity.getString(R.string.unit_time_min_sec, MAX_TIME / 60, MAX_TIME % 60)
                setting.restTime = MAX_TIME
            } else {
                activity.fOption_tv_interval_time.text =
                    activity.getString(R.string.unit_time_min_sec, timeInSec / 60, timeInSec % 60)
                setting.restTime = timeInSec
            }
        }

        activity.fOption_flo_interval_minus.setOnClickListener {
            var timeInSec = 60 * activity.fOption_tv_interval_time.text.split(":")[0].toInt()
            timeInSec += activity.fOption_tv_interval_time.text.split(":")[1].toInt()
            timeInSec -= 5
            if (timeInSec <= MIN_TIME) {
                activity.fOption_tv_interval_time.text =
                    activity.getString(R.string.unit_time_min_sec, MIN_TIME / 60, MIN_TIME % 60)
                setting.restTime = MIN_TIME
            } else {
                activity.fOption_tv_interval_time.text =
                    activity.getString(R.string.unit_time_min_sec, timeInSec / 60, timeInSec % 60)
                setting.restTime = timeInSec
            }
        }

        activity.fOption_clo_option_tutorial.setOnClickListener {
            navigator.showTutorial(activity)
        }

        activity.fOption_clo_feedback.setOnClickListener {
            var emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf("inty.ashever@gmail.com","timing.uniq@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                activity.getString(R.string.template_feedback_title)
            )
            emailIntent.putExtra(Intent.EXTRA_TEXT, "")
            activity.startActivity(emailIntent)

        }

        activity.fOption_clo_rateUs.setOnClickListener {
            val rateIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + activity.packageName)
            )
            activity.startActivity(rateIntent)
        }

        activity.fOption_clo_share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.foption_msg_name_str))
            var shareMessage = "\n${activity.getString(R.string.foption_msg_share_str)}\n\n"
            shareMessage =
                shareMessage + "http://play.google.com/store/apps/details?id=" + activity.packageName + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"))

        }

        activity.fOption_clo_openSource.setOnClickListener {
            navigator.showLicense(activity);
        }
    }
}