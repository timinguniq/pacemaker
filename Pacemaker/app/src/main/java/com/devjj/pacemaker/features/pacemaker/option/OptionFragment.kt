package com.devjj.pacemaker.features.pacemaker.option

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.platform.BaseFragment
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.fragment_option.*
import javax.inject.Inject

class OptionFragment : BaseFragment() {

    private lateinit var optionViewModel: OptionViewModel
    @Inject
    lateinit var setting: SettingSharedPreferences

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
        Toast.makeText(activity, "저장됨", Toast.LENGTH_SHORT).show()
    }

    private fun initializeView() {
        setColors()
        fOption_tv_weight_item.isFocusable=false
        fOption_tv_height_item.isFocusable=false
        fOption_tv_weight_item.setText(getString(R.string.rh_tv_unit_mass_str, setting.weight))
        fOption_tv_height_item.setText(getString(R.string.rh_tv_unit_height_str, setting.height))

        fOption_swc_mode_item.isChecked = setting.isNightMode

        fOption_swc_mode_item.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isNightMode = isChecked
            Log.d("color", "${activity!!.getColor(R.color.base_grey)}")
            setColors()
        }

        fOption_tv_weight_item.setOnClickListener { v ->
            Log.d("jayOption","weight on click")
            v.isFocusable=true
            v.requestFocus()
        }

        fOption_tv_weight_item.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true->{
                    Log.d("jayOption","weight focus gained")
                }
                false->{
                    Log.d("jayOption","weight focus lost")
                }
            }
        }


        fOption_tv_height_item.setOnClickListener { v ->
            Log.d("jayOption","height on click")
            v.isFocusable=true
            v.requestFocus()
        }

        fOption_tv_height_item.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true->{
                    Log.d("jayOption","height focus gained")
                }
                false->{
                    Log.d("jayOption","height focus lost")
                }
            }
        }
    }

    private fun setColors() {
        when (setting.isNightMode) {
            true -> {
                Log.d("color", "Dark Mode = ${setting.isNightMode}")
                activity!!.window.statusBarColor = activity!!.getColor(R.color.grey_bg_thickest)
                activity!!.aOption_clo_title.setBackgroundResource(R.drawable.img_title_background_nighttime)
                activity!!.aOption_flo_container.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thickest))

                fOption_swc_mode_item.thumbDrawable.setTint(activity!!.getColor(R.color.orange_bg_thick))
                fOption_swc_mode_item.trackDrawable.setTint(activity!!.getColor(R.color.orange_bg_basic))

                fOption_clo_profile.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thick))
                fOption_clo_language.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thick))
                fOption_clo_supportUs.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thick))

                fOption_tv_profile.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_language.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_supportUs.setTextColor(activity!!.getColor(R.color.black_txt_thick))

                fOption_rg_korean.buttonDrawable!!.setTint(activity!!.getColor(R.color.orange_bg_basic))
                fOption_rg_english.buttonDrawable!!.setTint(activity!!.getColor(R.color.orange_bg_basic))

                fOption_tv_mode_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))

                fOption_tv_weight_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_height_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_weight_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_tv_height_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))


                fOption_tv_interval_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_interval_time.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_iv_interval_minus.setImageResource(R.drawable.img_rest_minus_nighttime)
                fOption_iv_interval_plus.setImageResource(R.drawable.img_rest_plus_nighttime)

                fOption_tv_korean.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_english.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_rateUs.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_feedback.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_tv_share.setTextColor(activity!!.getColor(R.color.white_txt_thick))

            }
            false -> {
                activity!!.window.statusBarColor = activity!!.getColor(R.color.blue_bg_basic)
                activity!!.aOption_clo_title.setBackgroundResource(R.drawable.img_title_background_daytime)
                activity!!.aOption_flo_container.setBackgroundColor(activity!!.getColor(R.color.white_bg_basic))
                fOption_swc_mode_item.thumbDrawable.setTint(activity!!.getColor(R.color.blue_bg_thick))
                fOption_swc_mode_item.trackDrawable.setTint(activity!!.getColor(R.color.blue_bg_basic))

                fOption_clo_profile.setBackgroundColor(activity!!.getColor(R.color.grey_bg_basic))
                fOption_clo_language.setBackgroundColor(activity!!.getColor(R.color.grey_bg_basic))
                fOption_clo_supportUs.setBackgroundColor(activity!!.getColor(R.color.grey_bg_basic))

                fOption_tv_profile.setTextColor(activity!!.getColor(R.color.grey_txt_thick))
                fOption_tv_language.setTextColor(activity!!.getColor(R.color.grey_txt_thick))
                fOption_tv_supportUs.setTextColor(activity!!.getColor(R.color.grey_txt_thick))

                fOption_rg_korean.buttonDrawable!!.setTint(activity!!.getColor(R.color.blue_bg_basic))
                fOption_rg_english.buttonDrawable!!.setTint(activity!!.getColor(R.color.blue_bg_basic))

                fOption_tv_weight_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_height_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_weight_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_tv_height_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))


                fOption_tv_interval_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_interval_time.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_iv_interval_minus.setImageResource(R.drawable.img_rest_minus_daytime)
                fOption_iv_interval_plus.setImageResource(R.drawable.img_rest_plus_daytime)

                fOption_tv_mode_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_korean.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_english.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_rateUs.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_feedback.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_tv_share.setTextColor(activity!!.getColor(R.color.black_txt_thick))
            }
        }
    }
}

