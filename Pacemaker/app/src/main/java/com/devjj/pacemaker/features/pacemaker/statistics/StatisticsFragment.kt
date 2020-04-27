package com.devjj.pacemaker.features.pacemaker.statistics

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.getColor
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.platform.BaseFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_statistics.*
import javax.inject.Inject


class StatisticsFragment : BaseFragment(){
    override fun layoutId() = R.layout.fragment_statistics

    @Inject
    lateinit var statisticsDB : StatisticsDatabase
    private lateinit var statisticsViewModel: StatisticsViewModel
    @Inject lateinit var setting: SettingSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        statisticsViewModel = viewModel(viewModelFactory){
            observe(statistics,::renderStatistics)
        }
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    private fun renderStatistics(statistics: List<StatisticsView>?){
        val weightEntries: MutableList<Entry> = ArrayList()
        val weightOnBMIEntries : MutableList<Entry> = ArrayList()
        val xAxisArray = ArrayList<String>()
        when(statistics){
            null ->{
                Log.d("jayStatistics" , "no statistics")
            }
            else ->{
                var index = 0
                for( stats in statistics ){
                    Log.d("jayStatistics", "id : ${stats.id} , date : ${stats.date} , height : ${stats.height} , weight : ${stats.weight}")
                    weightEntries.add(Entry(index.toFloat(),stats.weight))
                    weightOnBMIEntries.add(Entry(index.toFloat(),25*(stats.height/100f)*(stats.height/100f)))
                    xAxisArray.add(stats.date.substring(5,stats.date.length))
                    index++
                }

            }
        }

        val weightDataSet = LineDataSet(weightEntries,"weight")
        weightDataSet.lineWidth = 5f
        weightDataSet.circleRadius = 5f
        weightDataSet.setCircleColor(getColor(activity!!,R.color.blue_5C83CF))
        weightDataSet.color = getColor(activity!!,R.color.blue_5C83CF_70)

        val weightOnBMIDataSet = LineDataSet(weightOnBMIEntries,"BMI 25")
        weightOnBMIDataSet.lineWidth = 5f
        weightOnBMIDataSet.circleRadius = 5f
        weightOnBMIDataSet.setCircleColor(getColor(activity!!,R.color.orange_FF765B))
        weightOnBMIDataSet.color = getColor(activity!!,R.color.orange_FF765B_70)

        var dataSets = ArrayList<ILineDataSet>()
        fStatistics_linechart.clear()

        when(setting.isShowbmi){
            true-> {
                dataSets.add(weightDataSet)
                dataSets.add(weightOnBMIDataSet)
            }
            false->{
                dataSets.add(weightDataSet)
            }
        }
        fStatistics_linechart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisArray)
        fStatistics_linechart.data = LineData(dataSets)
        //fStatistics_linechart.data = LineData(weightOnBMIDataSet)

    }

    private fun initializeView(){
        Log.d("jayStatistics" , "load statistics")
        val statisticsListener = StatisticsListener(activity!!,setting,statisticsViewModel)
        statisticsListener.initListener()
        statisticsViewModel.loadStatistics()
        setColor()
        fStatistics_swc_mode_bmi.isChecked = setting.isShowbmi
        fStatistics_linechart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    fun setColor(){
        when(setting.isNightMode){
            true ->{
                fStatistics_swc_mode_bmi.thumbDrawable.setTint(activity!!.getColor(R.color.orange_F74938))
                fStatistics_swc_mode_bmi.trackDrawable.setTint(activity!!.getColor(R.color.orange_FF765B))
                fStatistics_tv_mode_bmi.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
            }
            false ->{
                fStatistics_swc_mode_bmi.thumbDrawable.setTint(activity!!.getColor(R.color.blue_5F87D6))
                fStatistics_swc_mode_bmi.trackDrawable.setTint(activity!!.getColor(R.color.blue_5C83CF))
                fStatistics_tv_mode_bmi.setTextColor(activity!!.getColor(R.color.black_3B4046))
            }
        }
    }


}