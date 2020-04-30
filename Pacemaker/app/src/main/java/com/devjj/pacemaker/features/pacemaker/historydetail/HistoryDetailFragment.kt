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
import androidx.core.view.isVisible
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.isVisible
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
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
        setColors()
        fHistoryDetail_circleView_rate.setTextTypeface(Typeface.DEFAULT_BOLD)
        fHistoryDetail_circleView_rate.setUnitTextTypeface(Typeface.DEFAULT_BOLD)

        historyDetailAdapter.clickListener = { view, id, date ->
            /*
            historyDetailViewModel.switchAchievementById(id)
            historyDetailViewModel.updateAchievementRateByDate(date)
            historyDetailViewModel.loadHistoryDetails(date)*/

           // view.rvExerciseItem_tv_name.startAnimation(anim)

            /*
            when(view.rvExerciseItem_clo_detail.visibility) {
                GONE-> {
                    view.rvExerciseItem_clo_detail.visibility = VISIBLE
                }
                VISIBLE-> {
                    view.animation=anim
                    view.rvExerciseItem_clo_detail.visibility = GONE
                }
            }
            */

            var transition :Transition = Slide(Gravity.BOTTOM)
            transition.duration = 500
            transition.addTarget(view.rvExerciseItem_clo_detail)
            TransitionManager.beginDelayedTransition(view.rvExerciseItem_clo_main,transition)

            when(view.rvExerciseItem_clo_detail.isVisible()){
                true->view.rvExerciseItem_clo_detail.visibility = View.GONE
                false->view.rvExerciseItem_clo_detail.visibility = View.VISIBLE
            }



        }


        fHistoryDetail_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistoryDetail_recyclerview.adapter = historyDetailAdapter


        historyDetailViewModel.loadHistoryDetails(date)
        historyDetailViewModel.loadStatisticsOneDay(date)
        historyDetailViewModel.loadOneDaySets(date)
        Log.d("dateee", "${date.split("-")[0]}")

        activity!!.aHistoryDetail_tv_title.text = date

    }

    private fun setColors(){
        fHistoryDetail_circleView_rate.rimColor = activity!!.getColor(R.color.orange_FF765B_70)
        when(setting.isNightMode){
            true->{
               fHistoryDetail_circleView_rate.setBarColor(activity!!.getColor(R.color.orange_F74938))
                fHistoryDetail_circleView_rate.setTextColor(activity!!.getColor(R.color.orange_F74938))
                fHistoryDetail_tv_totalReps.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
                fHistoryDetail_tv_totalSets.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
            }
            false->{
                fHistoryDetail_circleView_rate.setBarColor(activity!!.getColor(R.color.orange_FF765B))
                fHistoryDetail_circleView_rate.setTextColor(activity!!.getColor(R.color.orange_FF765B))
                fHistoryDetail_tv_totalReps.setTextColor(activity!!.getColor(R.color.black_3B4046))
                fHistoryDetail_tv_totalSets.setTextColor(activity!!.getColor(R.color.black_3B4046))
            }
        }
    }
}