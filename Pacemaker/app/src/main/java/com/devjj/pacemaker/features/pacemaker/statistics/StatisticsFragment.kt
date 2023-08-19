package com.devjj.pacemaker.features.pacemaker.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import javax.inject.Inject


class StatisticsFragment : BaseFragment(){
    override fun layoutId() = R.layout.fragment_statistics

    @Inject lateinit var statisticsDB : StatisticsDatabase
    private lateinit var statisticsViewModel: StatisticsViewModel
    @Inject lateinit var setting: SettingSharedPreferences

    private var _binding: FragmentStatisticsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        statisticsViewModel = viewModel(viewModelFactory){
            observe(statistics,::renderStatistics)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderStatistics(statistics: List<StatisticsView>?){
        val weightEntries: MutableList<Entry> = ArrayList()
        val weightOnBMIEntries : MutableList<Entry> = ArrayList()
        val xAxisArray = ArrayList<String>()
        when(statistics){
            null ->{
                Dlog.d("no statistics")
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
        binding.fStatisticsLinechart.clear()

        when(setting.isShowbmi){
            true-> {
                dataSets.add(weightDataSet)
                dataSets.add(weightOnBMIDataSet)
                binding.fStatisticsCloBmiChange.visible()
                binding.fStatisticsCloLine03.visible()
            }
            false->{
                dataSets.add(weightDataSet)
                binding.fStatisticsCloBmiChange.gone()
                binding.fStatisticsCloLine03.gone()
            }
        }
        binding.fStatisticsLinechart.xAxis.isGranularityEnabled = true
        binding.fStatisticsLinechart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisArray)
        binding.fStatisticsLinechart.data = LineData(dataSets)
        //fStatistics_linechart.data = LineData(weightOnBMIDataSet)

    }

    private fun initializeView(){
        Dlog.d("load statistics")
        val statisticsListener = StatisticsListener(activity!!, binding, setting, statisticsViewModel)
        statisticsListener.initListener()

        setColor()
        binding.fStatisticsSwcModeBmi.isChecked = setting.isShowbmi
        binding.fStatisticsSwcMonthly.isChecked = setting.isMonthly
        binding.fStatisticsLinechart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.fStatisticsLinechart.setMaxVisibleValueCount(20)
        binding.fStatisticsTvBmi.text = setting.bmi.toString()
        binding.fStatisticsTvModeBmi.text = getString(R.string.template_bmi_str,setting.bmi)
        binding.fStatisticsLinechart.axisRight.isEnabled = false
        binding.fStatisticsLinechart.description.isEnabled = false

        statisticsViewModel.loadStatistics()
    }

    private fun setColor(){
        when(setting.isNightMode){
            true ->{
                binding.fStatisticsCloOption.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fStatisticsTvOption.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fStatisticsSwcModeBmi.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                binding.fStatisticsSwcModeBmi.trackDrawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                binding.fStatisticsTvModeBmi.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fStatisticsSwcMonthly.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                binding.fStatisticsSwcMonthly.trackDrawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                binding.fStatisticsTvMonthlyTitle.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fStatisticsTvBmiTitle.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fStatisticsTvBmi.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fStatisticsTvBmiLink.setTextColor(loadColor(activity!!,R.color.blue_A3BCE8))

                binding.fStatisticsIvBmiMinus.setImageResource(R.drawable.img_rest_minus_nighttime)
                binding.fStatisticsIvBmiPlus.setImageResource(R.drawable.img_rest_plus_nighttime)

                binding.fStatisticsLinechart.xAxis.textColor = loadColor(activity!!,R.color.white_F7FAFD)
                binding.fStatisticsLinechart.legend.textColor = loadColor(activity!!,R.color.white_F7FAFD)
                binding.fStatisticsLinechart.axisLeft.textColor = loadColor(activity!!,R.color.white_F7FAFD)

                binding.fStatisticsCloLine01.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
                binding.fStatisticsCloLine02.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
                binding.fStatisticsCloLine03.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
            }
            false ->{
                binding.fStatisticsCloOption.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fStatisticsTvOption.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                binding.fStatisticsSwcModeBmi.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                binding.fStatisticsSwcModeBmi.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                binding.fStatisticsTvModeBmi.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fStatisticsSwcMonthly.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                binding.fStatisticsSwcMonthly.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                binding.fStatisticsTvMonthlyTitle.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fStatisticsTvBmiTitle.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fStatisticsTvBmi.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fStatisticsTvBmiLink.setTextColor(loadColor(activity!!,R.color.blue_5C83CF_70))

                binding.fStatisticsIvBmiMinus.setImageResource(R.drawable.img_rest_minus_daytime)
                binding.fStatisticsIvBmiPlus.setImageResource(R.drawable.img_rest_plus_daytime)

                binding.fStatisticsLinechart.xAxis.textColor = loadColor(activity!!,R.color.black_3B4046)
                binding.fStatisticsLinechart.rendererLeftYAxis.paintAxisLabels.color = loadColor(activity!!,R.color.black_3B4046)
                binding.fStatisticsLinechart.legend.textColor = loadColor(activity!!,R.color.black_3B4046)
                binding.fStatisticsLinechart.axisLeft.textColor = loadColor(activity!!,R.color.black_3B4046)

                binding.fStatisticsCloLine01.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fStatisticsCloLine02.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fStatisticsCloLine03.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
            }
        }
    }
}