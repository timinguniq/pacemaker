package com.devjj.pacemaker.features.pacemaker.historydetail

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import android.transition.Slide
import android.transition.Transition
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.core.view.size
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_history_detail.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.recyclerview_exercise_detail_item.*
import kotlinx.android.synthetic.main.recyclerview_exercise_detail_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.util.*
import javax.inject.Inject

class HistoryDetailFragment(private val intent: Intent) : BaseFragment() {
    override fun layoutId() = R.layout.fragment_history_detail

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var db: ExerciseHistoryDatabase
    @Inject
    lateinit var historyDetailAdapter: HistoryDetailAdapter
    @Inject lateinit var setting: SettingSharedPreferences


    private lateinit var historyDetailViewModel: HistoryDetailViewModel
    private lateinit var historyDetailListener : HistoryDetailListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        historyDetailViewModel = viewModel(viewModelFactory) {
            observe(historyDetails, ::renderHistoryDetails)
            observe(statisticsOneDay,::renderStatisticsOneDay)
            observe(oneDaySets, ::renderOneDaySets)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun renderOneDaySets(oneDaySets: OneDaySets?){
        fHistoryDetail_tv_totalSets.text = this.getString(R.string.unit_sets, oneDaySets!!.sets )
    }

    private fun renderStatisticsOneDay(statisticsOneDay: StatisticsOneDay?){

        //fHistoryDetail_tv_totalSets.text = this.getString(R.string.unit_sets, statisticsOneDay!!.sets )
        fHistoryDetail_tv_totalReps.text = this.getString(R.string.unit_time_hour_min, statisticsOneDay!!.totalTime/60,statisticsOneDay.totalTime%60)
        fHistoryDetail_circleView_rate.setValue(statisticsOneDay.achievementRate.toFloat())
    }

    private fun renderHistoryDetails(historyDetailViews: List<HistoryDetailView>?) {
        historyDetailAdapter.collection = historyDetailViews.orEmpty()


    }

    private fun initializeView() {
        val date: String = intent.getStringExtra("date")
        fHistoryDetail_iv_drop.setImageDrawable(activity!!.getDrawable(R.drawable.fhistorydetail_img_btn_dropdown_daytime))
        fHistoryDetail_iv_drop.tag = R.drawable.fhistorydetail_img_btn_dropdown_daytime

        historyDetailListener = HistoryDetailListener(activity!!,historyDetailAdapter)
        historyDetailListener.initListener()

        setColors()

        fHistoryDetail_circleView_rate.setTextTypeface(Typeface.DEFAULT_BOLD)
        fHistoryDetail_circleView_rate.setUnitTextTypeface(Typeface.DEFAULT_BOLD)

        fHistoryDetail_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistoryDetail_recyclerview.adapter = historyDetailAdapter

        historyDetailViewModel.loadHistoryDetails(date)
        historyDetailViewModel.loadStatisticsOneDay(date)
        historyDetailViewModel.loadOneDaySets(date)
        Dlog.d( "${date.split("-")[0]}")

        activity!!.aHistoryDetail_tv_title.text = date

    }

    private fun setColors(){
        fHistoryDetail_circleView_rate.rimColor = loadColor(activity!!,R.color.orange_FF765B_70)
        when(setting.isNightMode){
            true->{
                fHistoryDetail_circleView_rate.setBarColor(loadColor(activity!!,R.color.orange_F74938))
                fHistoryDetail_circleView_rate.setTextColor(loadColor(activity!!,R.color.orange_F74938))
                fHistoryDetail_tv_totalReps.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fHistoryDetail_tv_totalSets.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fHistoryDetail_clo_openAll.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fHistoryDetail_tv_openAll.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fHistoryDetail_iv_drop.drawable.setTint(loadColor(activity!!,R.color.white_F7FAFD))
            }
            false->{
                fHistoryDetail_circleView_rate.setBarColor(loadColor(activity!!,R.color.orange_FF765B))
                fHistoryDetail_circleView_rate.setTextColor(loadColor(activity!!,R.color.orange_FF765B))
                fHistoryDetail_tv_totalReps.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fHistoryDetail_tv_totalSets.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fHistoryDetail_clo_openAll.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9_70))
                fHistoryDetail_tv_openAll.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fHistoryDetail_iv_drop.drawable.setTint(loadColor(activity!!,R.color.black_3B4046))
            }
        }
    }
}