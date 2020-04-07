package com.devjj.pacemaker.features.pacemaker.option

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        fOption_txv_weight_item.isFocusable=false
        fOption_txv_height_item.isFocusable=false
        fOption_txv_weight_item.setText(getString(R.string.rh_mass, setting.weight))
        fOption_txv_height_item.setText(getString(R.string.rh_height, setting.height))

        fOption_swc_mode_item.isChecked = setting.isNightMode
        activity!!.window.statusBarColor = activity!!.getColor(R.color.blue_bg_basic)

        fOption_swc_mode_item.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isNightMode = isChecked
            Log.d("color", "${activity!!.getColor(R.color.base_grey)}")
            setColors()
        }

        fOption_txv_weight_item.setOnClickListener { v ->
            Log.d("jayOption","weight on click")
            v.isFocusable=true
            v.requestFocus()
        }

        fOption_txv_weight_item.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true->{
                    Log.d("jayOption","weight focus gained")
                }
                false->{
                    Log.d("jayOption","weight focus lost")
                }
            }
        }



        fOption_txv_height_item.setOnClickListener { v ->
            Log.d("jayOption","height on click")
            v.isFocusable=true
            v.requestFocus()
        }

        fOption_txv_height_item.setOnFocusChangeListener { v, hasFocus ->
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
                activity!!.aOption_clo_title.setBackgroundResource(R.drawable.apacemaker_dm_title_background)
                activity!!.aOption_flo_container.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thickest))

                fOption_swc_mode_item.thumbDrawable.setTint(activity!!.getColor(R.color.orange_bg_thick))
                fOption_swc_mode_item.trackDrawable.setTint(activity!!.getColor(R.color.orange_bg_basic))

                fOption_clo_profile.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thick))
                fOption_clo_language.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thick))
                fOption_clo_supportUs.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thick))

                fOption_txv_profile.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_language.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_supportUs.setTextColor(activity!!.getColor(R.color.black_txt_thick))

                fOption_rg_korean.buttonDrawable!!.setTint(activity!!.getColor(R.color.orange_bg_basic))
                fOption_rg_english.buttonDrawable!!.setTint(activity!!.getColor(R.color.orange_bg_basic))

                fOption_txv_mode_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))

                fOption_txv_weight_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_height_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_weight_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_txv_height_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))


                fOption_txv_interval_title.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_interval_time.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_iv_interval_minus.setImageResource(R.drawable.faddition_dm_rest_minus_img)
                fOption_iv_interval_plus.setImageResource(R.drawable.faddition_dm_rest_plus_img)

                fOption_txv_korean.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_english.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_rateUs.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_feedback.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fOption_txv_share.setTextColor(activity!!.getColor(R.color.white_txt_thick))

            }
            false -> {
                activity!!.window.statusBarColor = activity!!.getColor(R.color.blue_bg_basic)
                activity!!.aOption_clo_title.setBackgroundResource(R.drawable.apacemaker_wm_title_background)
                activity!!.aOption_flo_container.setBackgroundColor(activity!!.getColor(R.color.white_bg_basic))
                fOption_swc_mode_item.thumbDrawable.setTint(activity!!.getColor(R.color.blue_bg_thick))
                fOption_swc_mode_item.trackDrawable.setTint(activity!!.getColor(R.color.blue_bg_basic))

                fOption_clo_profile.setBackgroundColor(activity!!.getColor(R.color.grey_bg_basic))
                fOption_clo_language.setBackgroundColor(activity!!.getColor(R.color.grey_bg_basic))
                fOption_clo_supportUs.setBackgroundColor(activity!!.getColor(R.color.grey_bg_basic))

                fOption_txv_profile.setTextColor(activity!!.getColor(R.color.grey_txt_thick))
                fOption_txv_language.setTextColor(activity!!.getColor(R.color.grey_txt_thick))
                fOption_txv_supportUs.setTextColor(activity!!.getColor(R.color.grey_txt_thick))

                fOption_rg_korean.buttonDrawable!!.setTint(activity!!.getColor(R.color.blue_bg_basic))
                fOption_rg_english.buttonDrawable!!.setTint(activity!!.getColor(R.color.blue_bg_basic))

                fOption_txv_weight_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_height_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_weight_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_txv_height_item.setTextColor(activity!!.getColor(R.color.grey_txt_light))


                fOption_txv_interval_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_interval_time.setTextColor(activity!!.getColor(R.color.grey_txt_light))
                fOption_iv_interval_minus.setImageResource(R.drawable.faddition_wm_rest_minus_img)
                fOption_iv_interval_plus.setImageResource(R.drawable.faddition_wm_rest_plus_img)

                fOption_txv_mode_title.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_korean.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_english.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_rateUs.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_feedback.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fOption_txv_share.setTextColor(activity!!.getColor(R.color.black_txt_thick))
            }
        }
    }
}

