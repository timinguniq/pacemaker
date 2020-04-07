package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.graphics.toColor
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.dmStatusBarColor
import com.devjj.pacemaker.core.extension.wmStatusBarColor
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.addition.isNightMode
import com.devjj.pacemaker.features.pacemaker.history.HistoryView
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailFragment
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailView
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
                    window.statusBarColor = dmStatusBarColor
                    aHistoryDetail_clo_title.setBackgroundResource(R.drawable.apacemaker_dm_title_background)
                    aHistoryDetail_flo_container.setBackgroundColor(getColor(R.color.grey_bg_thickest))
                }
                false->{
                    window.statusBarColor = wmStatusBarColor
                    aHistoryDetail_clo_title.setBackgroundResource(R.drawable.apacemaker_wm_title_background)
                    aHistoryDetail_flo_container.setBackgroundColor(getColor(R.color.white))
                }
            }
    }

    override var layout = R.layout.activity_history_detail
    override var fragmentId = R.id.aHistoryDetail_flo_container

    override fun fragment() = HistoryDetailFragment(intent)
}
