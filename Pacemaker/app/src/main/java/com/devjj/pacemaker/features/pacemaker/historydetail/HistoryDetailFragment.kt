package com.devjj.pacemaker.features.pacemaker.historydetail


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import at.grabner.circleprogress.TextMode
import at.grabner.circleprogress.UnitPosition
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_history_detail.*
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        historyDetailViewModel = viewModel(viewModelFactory) {
            observe(historyDetails, ::renderHistoryDetails)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun renderHistoryDetails(historyDetailViews: List<HistoryDetailView>?) {
        historyDetailAdapter.collection = historyDetailViews.orEmpty()

        fHistoryDetail_circleView_rate.setValue(historyDetailViews.orEmpty()[0].achievementRate.toFloat())

        var sets = 0
        var reps = 0
        for (historyDetailView in historyDetailViews!!) {
            sets += historyDetailView.set
            reps += historyDetailView.rep
        }
        fHistoryDetail_tv_totalSets.text = this.getString(R.string.fhistorydetail_tv_set_str,sets)
        fHistoryDetail_tv_totalReps.text = this.getString(R.string.fhistorydetail_tv_rep_str,reps)
    }

    private fun initializeView() {
        setColors()
        fHistoryDetail_circleView_rate.setTextTypeface(Typeface.DEFAULT_BOLD)
        fHistoryDetail_circleView_rate.setUnitTextTypeface(Typeface.DEFAULT_BOLD)
        historyDetailAdapter.clickListener = { id, date ->
            historyDetailViewModel.switchAchievementById(id)
            historyDetailViewModel.updateAchievementRateByDate(date)
            historyDetailViewModel.loadHistoryDetails(date)
        }



        fHistoryDetail_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistoryDetail_recyclerview.adapter = historyDetailAdapter

        val date: String = intent.getStringExtra("date")
        historyDetailViewModel.loadHistoryDetails(date)
        Log.d("dateee", "${date.split("-")[0]}")
        activity!!.aHistoryDetail_tv_title.text = this.getString(
            R.string.ahistorydetail_tv_title_str,
            date.split("-")[1],
            date.split("-")[2]
        )
    }

    private fun setColors(){
        fHistoryDetail_circleView_rate.rimColor = activity!!.getColor(R.color.orange_bg_transparent)
        when(setting.isNightMode){
            true->{
               fHistoryDetail_circleView_rate.setBarColor(activity!!.getColor(R.color.orange_bg_thick))
                fHistoryDetail_circleView_rate.setTextColor(activity!!.getColor(R.color.orange_bg_thick))
                fHistoryDetail_tv_totalReps.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fHistoryDetail_tv_totalSets.setTextColor(activity!!.getColor(R.color.white_txt_thick))
            }
            false->{
                fHistoryDetail_circleView_rate.setBarColor(activity!!.getColor(R.color.orange_bg_basic))
                fHistoryDetail_circleView_rate.setTextColor(activity!!.getColor(R.color.orange_bg_basic))
                fHistoryDetail_tv_totalReps.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fHistoryDetail_tv_totalSets.setTextColor(activity!!.getColor(R.color.black_txt_thick))
            }
        }
    }
}