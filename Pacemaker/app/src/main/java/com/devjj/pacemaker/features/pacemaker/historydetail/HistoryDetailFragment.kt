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
            observe(oneDaySummary,::renderOneDaySummary)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun renderOneDaySummary(oneDaySummary: OneDaySummary?){

        fHistoryDetail_tv_totalSets.text = this.getString(R.string.unit_sets, oneDaySummary!!.sets )
        fHistoryDetail_tv_totalReps.text = this.getString(R.string.unit_time_hour_min, oneDaySummary.times/60,oneDaySummary.times%60)
    }

    private fun renderHistoryDetails(historyDetailViews: List<HistoryDetailView>?) {
        historyDetailAdapter.collection = historyDetailViews.orEmpty()
        fHistoryDetail_circleView_rate.setValue(historyDetailViews.orEmpty()[0].achievementRate.toFloat())

    }

    private fun initializeView() {
        val date: String = intent.getStringExtra("date")
        setColors()
        historyDetailViewModel.updateAchievementRateByDate(date)
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

            historyDetailViewModel.updateAchievementRateByDate(date)
            var transition :Transition = Slide(Gravity.BOTTOM)
            transition.duration = 500
            transition.addTarget(view.rvExerciseItem_clo_detail)
            TransitionManager.beginDelayedTransition(view.rvExerciseItem_clo_main,transition)

            when(view.rvExerciseItem_clo_detail.isVisible()){
                true->view.rvExerciseItem_clo_detail.visibility = View.GONE
                false->view.rvExerciseItem_clo_detail.visibility = View.VISIBLE
            }

            /*
            when(view.rvExerciseItem_clo_detail.visibility){
                GONE->{
                    var transition :Transition = Slide(Gravity.TOP)
                    transition.setDuration(1000)
                    transition.addTarget(view.rvExerciseItem_clo_detail)
                    TransitionManager.beginDelayedTransition(view.rvExerciseItem_clo_main,transition)
                 /*   view.rvExerciseItem_clo_detail.animate()
                        .setDuration(5000)
                        .alpha(1.0f)
                        .translationY(view.rvExerciseItem_clo_detail.height.toFloat())*/
                    view.rvExerciseItem_clo_detail.visibility = VISIBLE
                }
                VISIBLE->{
                    var transition :Transition = Slide(Gravity.BOTTOM)
                    transition.setDuration(1000)
                    transition.addTarget(view.rvExerciseItem_clo_detail)
              /*      view.rvExerciseItem_clo_detail.animate()
                        .setDuration(5000)
                        .alpha(0.0f)
                        .translationY(0f)*/
                    TransitionManager.beginDelayedTransition(view.rvExerciseItem_clo_main,transition)
                    view.rvExerciseItem_clo_detail.visibility = GONE
                }
            }
        */

        }


        fHistoryDetail_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistoryDetail_recyclerview.adapter = historyDetailAdapter


        historyDetailViewModel.loadHistoryDetails(date)
        historyDetailViewModel.loadOneDaySummary(date)
        Log.d("dateee", "${date.split("-")[0]}")

        activity!!.aHistoryDetail_tv_title.text = date
        /*
        activity!!.aHistoryDetail_tv_title.text = this.getString(
            R.string.ahistorydetail_tv_title_str,
            date.split("-")[1],
            date.split("-")[2]
        )*/
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