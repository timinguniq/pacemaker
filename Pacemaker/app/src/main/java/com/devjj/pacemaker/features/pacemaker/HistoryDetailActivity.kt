package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailFragment
import kotlinx.android.synthetic.main.activity_history_detail.*
import javax.inject.Inject

class HistoryDetailActivity : BaseActivity() {
    @Inject
    lateinit var setting: SettingSharedPreferences

    companion object{
        fun callingIntent(context: Context ,date: String) : Intent{
        val intent = Intent(context,HistoryDetailActivity::class.java)
            intent.putExtra("date",date)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()
    }

    private fun initializeView() {
        Log.d("setting","${setting.isNightMode}")
            when(setting.isNightMode){
                true->{
                    window.statusBarColor = getColor(R.color.grey_bg_thickest)
                    aHistoryDetail_clo_title.setBackgroundResource(R.drawable.apacemaker_dm_title_background)
                    aHistoryDetail_flo_container.setBackgroundColor(getColor(R.color.grey_bg_thickest))
                }
                false->{
                    window.statusBarColor = getColor(R.color.blue_bg_thick)
                    aHistoryDetail_clo_title.setBackgroundResource(R.drawable.apacemaker_wm_title_background)
                    aHistoryDetail_flo_container.setBackgroundColor(getColor(R.color.white))
                }
            }
    }

    override var layout = R.layout.activity_history_detail
    override var fragmentId = R.id.aHistoryDetail_flo_container

    override fun fragment() = HistoryDetailFragment(intent)
}
