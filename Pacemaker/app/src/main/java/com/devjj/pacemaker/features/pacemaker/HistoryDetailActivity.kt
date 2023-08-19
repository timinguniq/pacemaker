package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityHistoryDetailBinding
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailFragment
import javax.inject.Inject

class HistoryDetailActivity : BaseActivity() {
    @Inject
    lateinit var setting: SettingSharedPreferences

    private lateinit var binding: ActivityHistoryDetailBinding

    companion object{
        fun callingIntent(context: Context ,date: String) : Intent{
        val intent = Intent(context,HistoryDetailActivity::class.java)
            intent.putExtra("date",date)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        appComponent.inject(this)
        val view = binding.root
        setContentView(view)
        initializeView()
    }

    private fun initializeView() {
        Dlog.d("${setting.isNightMode}")
            when(setting.isNightMode){
                true->{
                    window.statusBarColor = loadColor(this,R.color.grey_444646)
                    binding.aHistoryDetailCloTitle.setBackgroundResource(R.drawable.img_title_background_nighttime)
                    binding.aHistoryDetailFloContainer.setBackgroundColor(loadColor(this,R.color.grey_444646))
                }
                false->{
                    window.statusBarColor = loadColor(this,R.color.blue_5F87D6)
                    binding.aHistoryDetailCloTitle.setBackgroundResource(R.drawable.img_title_background_daytime)
                    binding.aHistoryDetailFloContainer.setBackgroundColor(loadColor(this,R.color.white_FFFFFF))
                }
            }
    }

    override var layout = R.layout.activity_history_detail
    override var fragmentId = R.id.aHistoryDetail_flo_container

    override fun fragment() = HistoryDetailFragment(intent)

    fun getBinding() = binding
}
