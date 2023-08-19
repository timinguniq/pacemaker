package com.devjj.pacemaker.features.pacemaker.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentHistoryBinding
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import javax.inject.Inject

class HistoryFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_history

    private lateinit var historyViewModel: HistoryViewModel
    @Inject
    lateinit var db: ExerciseHistoryDatabase

    @Inject
    lateinit var statisticsDB : StatisticsDatabase

    //@Inject lateinit var historyAdapter: HistoryAdapter
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var setting: SettingSharedPreferences

    private var _binding: FragmentHistoryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    private lateinit var historyListener: HistoryListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        historyViewModel = viewModel(viewModelFactory) {
            observe(histories, ::renderHistoryList)
            //    failure(failure, ::handleFailure)
            observe(setsAndMass, ::renderSetsAndMass)
            observe(setsAndMassOneMonth, ::renderSetsAndMassOneMonthOneMonth)
            observe(totalTimes, ::renderTotalTimes)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderHistoryList(histories: List<HistoryView>?) {
        //historyAdapter.collection = histories.orEmpty()
        when (histories) {
            null -> {
                Dlog.d( "empty")
            }
            else ->
                for (history in histories) {
                    Dlog.d( "datess ${history.date}")
                    var dates = history.date.split("-")
                    Dlog.d( "${dates[0]} , ${dates[1]} , ${dates[2]}")
                    binding.fHistoryCalendarView.setDateSelected(
                        CalendarDay.from(
                            LocalDate.of(
                                dates[0].toInt(),
                                dates[1].toInt(),
                                dates[2].toInt()
                            )
                        )
                        , true
                    )
                }
        }
    }

    private fun renderSetsAndMass(sumOfSetsAndMass: SumOfSetsAndMass?) {
        binding.fHistoryTvTotalSets.text = getString(R.string.unit_sets, sumOfSetsAndMass!!.sets)
      //  fHistory_tv_total_times.text = getString(R.string.unit_time_hour_min, sumOfSetsAndMass.times / 60, sumOfSetsAndMass.times % 60)
        binding.fHistoryTvTotalKgs.text = getString(R.string.unit_mass, sumOfSetsAndMass.mass)
    }

    private fun renderSetsAndMassOneMonthOneMonth(sumOfSetsAndMass : SumOfSetsAndMass?){
        binding.fHistoryTvMonthSets.text = getString(R.string.unit_sets, sumOfSetsAndMass!!.sets)
      //  fHistory_tv_month_times.text = getString(R.string.unit_time_hour_min, sumOfSetsAndMass.times / 60, sumOfSetsAndMass.times % 60)
        binding.fHistoryTvMonthKgs.text = getString(R.string.unit_mass, sumOfSetsAndMass.mass)
       // Dlog.d( sumOfSetsAndMass.times.toString())
    }

    private fun renderTotalTimes(totalTimes: TotalTimes?){
        binding.fHistoryTvTotalTimes.text = getString(R.string.unit_time_hour_min, totalTimes!!.totalTime / 60, totalTimes.totalTime % 60)
        binding.fHistoryTvMonthTimes.text = getString(R.string.unit_time_hour_min, totalTimes.totalTimeOneMonth / 60, totalTimes.totalTimeOneMonth % 60)
    }

    private fun initializeView() {
        setColors()
        val pacemakerActivity = requireActivity() as PacemakerActivity
        pacemakerActivity.getBinding().aPacemakerTvTitle.text = this.getString(R.string.fhistory_tv_title_str)
        pacemakerActivity.getBinding().aPacemakerFloEdit.gone()
        //this.activity!!.aPacemaker_tv_title.text = this.getString(R.string.fhistory_tv_title_str)
        //this.activity!!.aPacemaker_flo_edit.gone()
        //fHistory_recyclerview.layoutManager = LinearLayoutManager(activity)
        //fHistory_recyclerview.adapter = historyAdapter
        binding.fHistoryTvHeight.text = getString(R.string.unit_height,setting.height)
        binding.fHistoryTvWeight.text = getString(R.string.unit_weight,setting.weight)
        historyListener = HistoryListener(activity!!, binding, navigator, historyViewModel)
        historyListener.initListener()

        runBlocking {
            launch(Dispatchers.IO) {
                statisticsDB.StatisticsDAO().getAll()
            }
        }

        @NonNull
        val today = CalendarDay.today().date.toString()

        historyViewModel.loadHistories()
        historyViewModel.loadSumOfSetsAndMass()
        historyViewModel.loadOneMonthSumOfSetsAndMass(today)
        historyViewModel.loadTotalTimes(today)
        Dlog.d( today)
    }

    private fun setColors() {
        Dlog.d( "history fragment")

        binding.fHistoryCalendarView.tileWidth = convertDpToPx(context!!,52f).toInt()
        binding.fHistoryCalendarView.tileHeight = convertDpToPx(context!!,40f).toInt()

        Dlog.d( "tile density : ${context!!.resources.displayMetrics.density}")
        Dlog.d( "tileWidth : ${convertDpToPx(context!!, 360f)}")
        Dlog.d( "tileHeight : ${convertDpToPx(context!!, 240f)}")

        when (setting.isNightMode) {
            true -> {
                Dlog.d( "Night time mode")
                // loadColor(activity!!,R.color.orange_F74938)
                binding.fHistoryCalendarView.selectionColor = loadColor(activity!!,R.color.orange_F74938)
                binding.fHistoryCalendarView.setDateTextAppearance(R.style.CalendarDateTextAppearanceNightTime)
                binding.fHistoryCalendarView.setHeaderTextAppearance(R.style.CalendarHeaderTextAppearanceNightTime)
                binding.fHistoryCalendarView.setWeekDayTextAppearance(R.style.CalendarHeaderTextAppearanceNightTime)
                binding.fHistoryCalendarView.leftArrow.setTint(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryCalendarView.rightArrow.setTint(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fHistoryCloReport.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
                binding.fHistoryTvReport.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fHistoryTvProfile.setTextColor(loadColor(activity!!,R.color.grey_AEB3B3))
                binding.fHistoryTvWeight.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryTvHeight.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fHistoryTvTotal.setTextColor(loadColor(activity!!,R.color.grey_AEB3B3))
                binding.fHistoryTvTotalSets.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryTvTotalTimes.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryTvTotalKgs.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fHistoryTvMonth.setTextColor(loadColor(activity!!,R.color.grey_AEB3B3))
                binding.fHistoryTvMonthSets.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryTvMonthTimes.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryTvMonthKgs.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fHistoryIvChart.setImageDrawable(activity!!.getDrawable(R.drawable.fhistory_img_btn_chart_nighttime))
                binding.fHistoryIvCommentMonth.setImageDrawable(activity!!.getDrawable(R.drawable.fhistory_img_btn_comment_nighttime))
                binding.fHistoryIvCommentTotal.setImageDrawable(activity!!.getDrawable(R.drawable.fhistory_img_btn_comment_nighttime))
                binding.fHistoryIvChart.setBackgroundResource(R.drawable.fhistory_button_bg_color_nighttime)
                binding.fHistoryIvCommentMonth.setBackgroundResource(R.drawable.fhistory_button_bg_color_nighttime)
                binding.fHistoryIvCommentTotal.setBackgroundResource(R.drawable.fhistory_button_bg_color_nighttime)

                binding.fHistoryCloLine01.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
                binding.fHistoryCloLine02.setBackgroundColor(loadColor(activity!!,R.color.grey_88898A))
            }
            false -> {
                Dlog.d( "Day time mode")
                binding.fHistoryCalendarView.selectionColor = loadColor(activity!!,R.color.orange_F74938)

                binding.fHistoryCalendarView.setDateTextAppearance(R.style.CalendarDateTextAppearanceDayTime)
                binding.fHistoryCalendarView.setHeaderTextAppearance(R.style.CalendarHeaderTextAppearanceDayTime)
                binding.fHistoryCalendarView.setWeekDayTextAppearance(R.style.CalendarHeaderTextAppearanceDayTime)
                binding.fHistoryCalendarView.leftArrow.setTint(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryCalendarView.rightArrow.setTint(loadColor(activity!!,R.color.black_3B4046))

                binding.fHistoryCloReport.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9_70))
                binding.fHistoryTvReport.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                binding.fHistoryTvProfile.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                binding.fHistoryTvWeight.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryTvHeight.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fHistoryTvTotal.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                binding.fHistoryTvTotalSets.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryTvTotalTimes.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryTvTotalKgs.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fHistoryTvMonth.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                binding.fHistoryTvMonthSets.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryTvMonthTimes.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryTvMonthKgs.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fHistoryIvChart.setImageDrawable(activity!!.getDrawable(R.drawable.fhistory_img_btn_chart_daytime))
                binding.fHistoryIvCommentMonth.setImageDrawable(activity!!.getDrawable(R.drawable.fhistory_img_btn_comment_daytime))
                binding.fHistoryIvCommentTotal.setImageDrawable(activity!!.getDrawable(R.drawable.fhistory_img_btn_comment_daytime))
                binding.fHistoryIvCommentMonth.drawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                binding.fHistoryIvCommentTotal.drawable.setTint(loadColor(activity!!,R.color.orange_FF765B))

                binding.fHistoryIvChart.setBackgroundResource(R.drawable.fhistory_button_bg_color_daytime)
                binding.fHistoryIvCommentMonth.setBackgroundResource(R.drawable.fhistory_button_bg_color_daytime)
                binding.fHistoryIvCommentTotal.setBackgroundResource(R.drawable.fhistory_button_bg_color_daytime)

                binding.fHistoryCloLine01.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fHistoryCloLine02.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
            }
        }
    }
}
