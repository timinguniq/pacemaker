package com.devjj.pacemaker.features.pacemaker.option


import android.content.Intent
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.fragment_option.*
import java.util.*
import javax.inject.Inject

class OptionFragment : BaseFragment() {

    private lateinit var optionViewModel: OptionViewModel
    @Inject
    lateinit var setting: SettingSharedPreferences
    @Inject
    lateinit var navigator: Navigator

    override fun layoutId() = R.layout.fragment_option

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, getString(R.string.foption_toast_saved), Toast.LENGTH_SHORT).show()
    }

    private fun initializeView() {
        setColors()

        fOption_swc_mode_item.isChecked = setting.isNightMode
        fOption_swc_mode_weight.isChecked = setting.isUpdateWeight
        fOption_swc_mode_height.isChecked = setting.isUpdateHeight

        val optionListener = OptionListener(activity!!,setting,::setColors, navigator)
        optionListener.initListener()
        fOption_tv_interval_time.text = getString(R.string.unit_time_min_sec,setting.restTime/60,setting.restTime%60)
    }

    private fun setColors() {
        when (setting.isNightMode) {
            true -> {
                Dlog.d( "Dark Mode = ${setting.isNightMode}")
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.grey_444646)
                activity!!.aOption_clo_title.setBackgroundResource(R.drawable.img_title_background_nighttime)
                activity!!.aOption_flo_container.setBackgroundColor(loadColor(activity!!,R.color.grey_444646))

                fOption_swc_mode_item.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                fOption_swc_mode_item.trackDrawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                fOption_swc_mode_weight.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                fOption_swc_mode_weight.trackDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                fOption_swc_mode_height.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                fOption_swc_mode_height.trackDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))

                fOption_clo_profile.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_supportUs.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_option.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))

                fOption_tv_profile.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_supportUs.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_option.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fOption_tv_mode_title.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                fOption_tv_interval_title.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_interval_time.setTextColor(loadColor(activity!!,R.color.grey_B2B2B2))
                fOption_iv_interval_minus.setImageResource(R.drawable.img_rest_minus_nighttime)
                fOption_iv_interval_plus.setImageResource(R.drawable.img_rest_plus_nighttime)

                fOption_tv_rateUs.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_feedback.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_share.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_openSource.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_mode_tutorial.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_mode_weight.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOption_tv_mode_height.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                fOption_clo_line_01.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_line_02.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_line_03.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_line_04.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_line_05.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOption_clo_line_06.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))

                fOption_tv_UI_Design_by.setTextColor(loadColor(activity!!,R.color.grey_B2B2B2))
            }
            false -> {
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.blue_5C83CF)
                activity!!.aOption_clo_title.setBackgroundResource(R.drawable.img_title_background_daytime)
                activity!!.aOption_flo_container.setBackgroundColor(loadColor(activity!!,R.color.white_FFFFFF))

                fOption_swc_mode_item.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                fOption_swc_mode_item.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                fOption_swc_mode_weight.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                fOption_swc_mode_weight.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                fOption_swc_mode_height.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                fOption_swc_mode_height.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))

                fOption_clo_profile.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_supportUs.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_option.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))

                fOption_tv_profile.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                fOption_tv_supportUs.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                fOption_tv_option.setTextColor(loadColor(activity!!,R.color.grey_87888A))


                fOption_tv_interval_title.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_interval_time.setTextColor(loadColor(activity!!,R.color.grey_B2B2B2))
                fOption_iv_interval_minus.setImageResource(R.drawable.img_rest_minus_daytime)
                fOption_iv_interval_plus.setImageResource(R.drawable.img_rest_plus_daytime)

                fOption_tv_mode_title.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_rateUs.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_feedback.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_share.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_openSource.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_mode_tutorial.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_mode_weight.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOption_tv_mode_height.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fOption_clo_line_01.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_line_02.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_line_03.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_line_04.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_line_05.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOption_clo_line_06.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))

                fOption_tv_UI_Design_by.setTextColor(loadColor(activity!!,R.color.grey_87888A))
            }
        }
    }
}

