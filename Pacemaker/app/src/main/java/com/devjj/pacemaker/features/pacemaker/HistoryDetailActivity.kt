package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.history.HistoryView
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailFragment
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailView

class HistoryDetailActivity : BaseActivity() {
    companion object{
        fun callingIntent(context: Context ,date: String) : Intent{
        val intent = Intent(context,HistoryDetailActivity::class.java)
            intent.putExtra("date",date)
            return intent
        }
    }

    override var layout = R.layout.activity_history_detail
    override var fragmentId = R.id.aHistoryDetail_flo_container

    override fun fragment() = HistoryDetailFragment(intent)
}
