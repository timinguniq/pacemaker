package com.devjj.pacemaker.features.pacemaker.historydetail


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
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

    private fun renderHistoryDetails(historyDetailView: List<HistoryDetailView>?) {
        historyDetailAdapter.collection = historyDetailView.orEmpty()
        fHistoryDetail_tv_achievementRate.text =
            getString(R.string.rh_rate, historyDetailView.orEmpty()[0].achievementRate)

        fHistoryDetail_charts_pieChart.centerText = getString(R.string.rh_rate, historyDetailView.orEmpty()[0].achievementRate)
        Log.d("Rate check", "${fHistoryDetail_charts_pieChart.data.dataSet.getEntryForIndex(0).value}")

        fHistoryDetail_charts_pieChart.data = getDataSet(historyDetailView.orEmpty()[0].achievementRate.toFloat())
        fHistoryDetail_charts_pieChart.animateXY(3000,3000)
        Log.d("Rate check", "begin")
        for (text in historyDetailView!!) {
            Log.d("Rate check", " ${text.achievementRate}")
        }
    }

    private fun getDataSet(rate :Float) : PieData{
        val dataSet = PieDataSet(listOf(PieEntry(rate,"test1"),PieEntry(100-rate,"test2")),"test3")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 0f
        dataSet.iconsOffset = MPPointF(0F,0F)
        dataSet.selectionShift = 0f
        dataSet.setColors(Color.argb(0xFF,0xFF,0x76,0x5b),Color.argb(0xCF,0xFF,0x76,0x5b))
        val data = PieData(dataSet)
        data.setDrawValues(false)
        //data.setValueTextSize(0f)
        //data.setValueTextColor(Color.RED)

        return data
    }

    private fun initializeView() {

        fHistoryDetail_charts_pieChart.setCenterTextColor(Color.argb(0xFF,0xFF,0x76,0x5b))
        fHistoryDetail_charts_pieChart.setDrawEntryLabels(false)
        fHistoryDetail_charts_pieChart.setCenterTextSize(30f)
        fHistoryDetail_charts_pieChart.description.isEnabled = false
        fHistoryDetail_charts_pieChart.legend.isEnabled = false
        fHistoryDetail_charts_pieChart.scrollBarSize = 10

        var rate = 75f

        fHistoryDetail_charts_pieChart.data = getDataSet(rate)
        fHistoryDetail_charts_pieChart.setDrawEntryLabels(false)
        fHistoryDetail_charts_pieChart.highlightValues(null)
        fHistoryDetail_charts_pieChart.invalidate()
        fHistoryDetail_charts_pieChart.setHoleColor(Color.TRANSPARENT)
        fHistoryDetail_charts_pieChart.setBackgroundColor(Color.BLACK)
        fHistoryDetail_charts_pieChart.animateXY(3000,3000)



        //PieDataSet(listOf(PieEntry(0.5f,"no1"),PieEntry(0.2f,"no2")),"label")
        // fHistoryDetail_charts_pieChart.animateY(1400, Easing.EaseInOutQuad)



        historyDetailAdapter.clickListener = { id, date ->
            when (fHistoryDetail_floating_action_btn.isSelected) {
                true -> {
                    Log.d("clicked", "clicked do action")
                    historyDetailViewModel.switchAchievementById(id)
                    historyDetailViewModel.updateAchievementRateByDate(date)
                    historyDetailViewModel.loadHistoryDetails(date)
                  /*  Flowable.just(historyDetailViewModel.updateAchievementRateByDate(date))

                    .observeOn(Schedulers.io())
                    .subscribe {

                    }.isDisposed
*/


                    /*
                    var mutex = Mutex()
                    runBlocking {
                        mutex.lock()
                        historyDetailViewModel.switchAchievementById(id)
                        mutex.unlock()
                        Log.d("Thread :" , Thread.currentThread().name+" 1")
                    }
                    runBlocking {
                        mutex.lock()
                        val b=historyDetailViewModel.updateAchievementRateByDate(date)
                        mutex.unlock()
                        Log.d("Thread :" , Thread.currentThread().name+" 2")
                    }
                    runBlocking {
                        mutex.lock()
                        val c= historyDetailViewModel.loadHistoryDetails(date)
                        mutex.unlock()
                        Log.d("Thread :" , Thread.currentThread().name+" 3")
                    }
                    */

                }
                false -> Log.d("clicked", "clicked no action")
            }
        }


        fHistoryDetail_floating_action_btn?.setOnClickListener {
            if (fHistoryDetail_floating_action_btn.isSelected) {
                fHistoryDetail_floating_action_btn.setImageResource(android.R.drawable.ic_menu_edit)
                fHistoryDetail_floating_action_btn.isSelected = false
                Log.d("clicked", "changes saved")
            } else {
                fHistoryDetail_floating_action_btn.setImageResource(android.R.drawable.ic_menu_save)
                fHistoryDetail_floating_action_btn.isSelected = true
                Log.d("clicked", "entered edit mode")
            }
        }

        fHistoryDetail_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistoryDetail_recyclerview.adapter = historyDetailAdapter

        val date: String = intent.getStringExtra("date")
        historyDetailViewModel.loadHistoryDetails(date)
    }
}

