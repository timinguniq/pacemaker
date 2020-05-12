package com.devjj.pacemaker.features.pacemaker.statistics

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.getColor
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_statistics.*
import javax.inject.Inject


class StatisticsFragment : BaseFragment(){
    override fun layoutId() = R.layout.fragment_statistics

    @Inject lateinit var statisticsDB : StatisticsDatabase
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
                for( (index , stats) in statistics.withIndex() ){
                    Dlog.d( "id : stats.id , date : ${stats.date} , height : ${stats.height} , weight : ${stats.weight}")
                    weightEntries.add(Entry(index.toFloat(),stats.weight))
                    weightOnBMIEntries.add(Entry(index.toFloat(),setting.bmi.toFloat()*(stats.height/100f)*(stats.height/100f)))
                    xAxisArray.add(stats.date)
                }
            }
        }

        val weightDataSet = LineDataSet(weightEntries,"weight")
        weightDataSet.lineWidth = 5f
        weightDataSet.circleRadius = 5f
        weightDataSet.setCircleColor(loadColor(activity!!,R.color.blue_5C83CF))
        weightDataSet.valueTextSize = 10f
        //weightDataSet

        when(setting.isNightMode){
            true-> {
                weightDataSet.color = loadColor(activity!!, R.color.blue_A3BCE8)
                weightDataSet.valueTextColor = loadColor(activity!!, R.color.white_F7FAFD)
            }
            false-> {
                weightDataSet.color = loadColor(activity!!, R.color.blue_5C83CF_70)
                weightDataSet.valueTextColor = loadColor(activity!!, R.color.black_3B4046)
            }
        }

        val weightOnBMIDataSet = LineDataSet(weightOnBMIEntries,getString(R.string.template_bmi_str,setting.bmi))
        weightOnBMIDataSet.lineWidth = 5f
        weightOnBMIDataSet.circleRadius = 5f
        weightOnBMIDataSet.setCircleColor(loadColor(activity!!,R.color.orange_FF765B))
        weightOnBMIDataSet.valueTextSize = 10f

        when(setting.isNightMode){
            true-> {
                weightOnBMIDataSet.color = loadColor(activity!!,R.color.orange_FF765B_70)
                weightOnBMIDataSet.valueTextColor = loadColor(activity!!,R.color.white_F7FAFD)
            }
            false-> {
                weightOnBMIDataSet.color = loadColor(activity!!,R.color.orange_FF765B_70)
                weightOnBMIDataSet.valueTextColor = loadColor(activity!!,R.color.black_3B4046)
            }
        }

        var dataSets = ArrayList<ILineDataSet>()
        fStatistics_linechart.clear()

        when(setting.isShowbmi){
            true-> {
                dataSets.add(weightDataSet)
                dataSets.add(weightOnBMIDataSet)
                fStatistics_clo_bmi_change.visible()
                fStatistics_clo_line_03.visible()
            }
            false->{
                dataSets.add(weightDataSet)
                fStatistics_clo_bmi_change.gone()
                fStatistics_clo_line_03.gone()
            }
        }
        fStatistics_linechart.xAxis.isGranularityEnabled = true
        fStatistics_linechart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisArray)
        fStatistics_linechart.data = LineData(dataSets)
        //fStatistics_linechart.data = LineData(weightOnBMIDataSet)

    }

    private fun initializeView(){
        Log.d("jayStatistics" , "load statistics")
        val statisticsListener = StatisticsListener(activity!!,setting,statisticsViewModel)
        statisticsListener.initListener()

        setColor()
        fStatistics_swc_mode_bmi.isChecked = setting.isShowbmi
        fStatistics_swc_monthly.isChecked = setting.isMonthly
        fStatistics_linechart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fStatistics_linechart.setMaxVisibleValueCount(20)
        fStatistics_tv_bmi.text = setting.bmi.toString()
        fStatistics_tv_mode_bmi.text = getString(R.string.template_bmi_str,setting.bmi)
        fStatistics_linechart.axisRight.isEnabled = false
        fStatistics_linechart.description.isEnabled = false

        statisticsViewModel.loadStatistics()
    }

    private fun setColor(){
        when(setting.isNightMode){
            true ->{
                fStatistics_clo_option.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fStatistics_tv_option.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fStatistics_swc_mode_bmi.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                fStatistics_swc_mode_bmi.trackDrawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                fStatistics_tv_mode_bmi.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                fStatistics_swc_monthly.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                fStatistics_swc_monthly.trackDrawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                fStatistics_tv_monthly_title.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                fStatistics_tv_bmi_title.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fStatistics_tv_bmi.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                fStatistics_tv_bmi_link.setTextColor(loadColor(activity!!,R.color.blue_A3BCE8))

                fStatistics_iv_bmi_minus.setImageResource(R.drawable.img_rest_minus_nighttime)
                fStatistics_iv_bmi_plus.setImageResource(R.drawable.img_rest_plus_nighttime)

                fStatistics_linechart.xAxis.textColor = loadColor(activity!!,R.color.white_F7FAFD)
                fStatistics_linechart.legend.textColor = loadColor(activity!!,R.color.white_F7FAFD)
                fStatistics_linechart.axisLeft.textColor = loadColor(activity!!,R.color.white_F7FAFD)

                fStatistics_clo_line_01.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
                fStatistics_clo_line_02.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
                fStatistics_clo_line_03.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
            }
            false ->{
                fStatistics_clo_option.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fStatistics_tv_option.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                fStatistics_swc_mode_bmi.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                fStatistics_swc_mode_bmi.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                fStatistics_tv_mode_bmi.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fStatistics_swc_monthly.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                fStatistics_swc_monthly.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                fStatistics_tv_monthly_title.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fStatistics_tv_bmi_title.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fStatistics_tv_bmi.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fStatistics_tv_bmi_link.setTextColor(loadColor(activity!!,R.color.blue_5C83CF_70))

                fStatistics_iv_bmi_minus.setImageResource(R.drawable.img_rest_minus_daytime)
                fStatistics_iv_bmi_plus.setImageResource(R.drawable.img_rest_plus_daytime)

                fStatistics_linechart.xAxis.textColor = loadColor(activity!!,R.color.black_3B4046)
                fStatistics_linechart.rendererLeftYAxis.paintAxisLabels.color = loadColor(activity!!,R.color.black_3B4046)
                fStatistics_linechart.legend.textColor = loadColor(activity!!,R.color.black_3B4046)
                fStatistics_linechart.axisLeft.textColor = loadColor(activity!!,R.color.black_3B4046)

                fStatistics_clo_line_01.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fStatistics_clo_line_02.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fStatistics_clo_line_03.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
            }
        }
    }
}