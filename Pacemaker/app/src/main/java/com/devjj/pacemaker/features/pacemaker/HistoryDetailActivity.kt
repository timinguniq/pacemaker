package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.functional.Dlog
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
        Dlog.d("${setting.isNightMode}")
            when(setting.isNightMode){
                true->{
                    window.statusBarColor = getColor(R.color.grey_444646)
                    aHistoryDetail_clo_title.setBackgroundResource(R.drawable.img_title_background_nighttime)
                    aHistoryDetail_flo_container.setBackgroundColor(getColor(R.color.grey_444646))
                }
                false->{
                    window.statusBarColor = getColor(R.color.blue_5F87D6)
                    aHistoryDetail_clo_title.setBackgroundResource(R.drawable.img_title_background_daytime)
                    aHistoryDetail_flo_container.setBackgroundColor(getColor(R.color.white_FFFFFF))
                }
            }
    }

    override var layout = R.layout.activity_history_detail
    override var fragmentId = R.id.aHistoryDetail_flo_container

    override fun fragment() = HistoryDetailFragment(intent)
}
