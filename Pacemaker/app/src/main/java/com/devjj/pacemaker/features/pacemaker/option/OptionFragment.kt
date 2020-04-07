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
import com.devjj.pacemaker.core.extension.close
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.addition.isNightMode
import com.devjj.pacemaker.features.pacemaker.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.fragment_option.*
import javax.inject.Inject

class OptionFragment : BaseFragment() {

    private lateinit var optionViewModel: OptionViewModel
    @Inject lateinit var setting: SettingSharedPreferences
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
        Toast.makeText(activity,"저장됨",Toast.LENGTH_SHORT).show()
    }

    private fun initializeView(){
        fOption_txv_weight_item.text=getString(R.string.rh_mass,50)
        fOption_txv_height_item.text = getString(R.string.rh_height,160.3)
        fOption_swc_mode_item.isChecked=setting.isNightMode
        activity!!.window.statusBarColor = activity!!.getColor(R.color.blue_bg_basic)

        fOption_swc_mode_item.setOnCheckedChangeListener { buttonView, isChecked ->
            setting.isNightMode=isChecked
            Log.d("color","${activity!!.getColor(R.color.base_grey)}")
            when(setting.isNightMode){
                true->{
                    Log.d("color","Dark Mode = ${setting.isNightMode}")
                    activity!!.window.statusBarColor = activity!!.getColor(R.color.grey_bg_thick)
                    activity!!.aOption_clo_title.setBackgroundResource(R.drawable.apacemaker_dm_title_background)
                }
                false-> {
                    activity!!.window.statusBarColor = activity!!.getColor(R.color.blue_bg_basic)
                    activity!!.aOption_clo_title.setBackgroundResource(R.drawable.apacemaker_wm_title_background)
                }
            }
        }
    }
}

