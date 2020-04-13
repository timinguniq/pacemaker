package com.devjj.pacemaker.features.pacemaker.history

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.LinearLayoutManager

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.android.synthetic.main.fragment_history.*
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HistoryFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_history

    private lateinit var historyViewModel: HistoryViewModel
    @Inject
    lateinit var db: ExerciseHistoryDatabase
    @Inject
    lateinit var historyAdapter: HistoryAdapter
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var setting: SettingSharedPreferences

    private lateinit var historyListener: HistoryListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        historyViewModel = viewModel(viewModelFactory) {
            observe(histories, ::renderHistoryList)
            //    failure(failure, ::handleFailure)
            observe(summary, ::renderSummary)
        }

    }



    override fun onResume() {
        super.onResume()
        initializeView()
    }

    private fun renderHistoryList(histories: List<HistoryView>?) {
        historyAdapter.collection = histories.orEmpty()

        when (histories) {
            null -> {
                Log.d("calendar", "empty")
            }
            else ->
                for (history in histories!!) {
                    Log.d("test", "datess ${history.date}")
                    var dates = history.date.split("-")
                    Log.d("calendarcheck", "${dates[0]} , ${dates[1]} , ${dates[2]}")
                    fHistory_calendarView.setDateSelected(
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

    private fun renderSummary(summary: Summary?) {
        fHistory_txv_sets.text = getString(R.string.unit_sets, summary!!.sets)
        fHistory_txv_times.text =
            getString(R.string.unit_time_hour_min, summary!!.times / 60, summary!!.times % 60)
    }


    private fun initializeView() {
        setColors()
        this.activity!!.aPacemaker_tv_title.text = this.getString(R.string.fhistory_tv_title_str)
        fHistory_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistory_recyclerview.adapter = historyAdapter

        historyListener = HistoryListener(activity!!, navigator)
        historyListener.initListener()

        historyAdapter.clickListener = { date ->
            navigator.showHistoryDetail(activity!!, date)
        }


        if (false) {
            val a = ExerciseHistoryEntity(0,"2020-3-30",0,"벤치프레스",5,10,2,2,30,true,30,60,170,10)
            val b = ExerciseHistoryEntity(0,"2020-04-01",1,"데드리프트",10,10,3,1,40,false,40,60,170,20)
            val c = ExerciseHistoryEntity(0,"2020-04-01",2,"스쿼드",15,10,4,4,50,true,40,60,170,20)
            val d = ExerciseHistoryEntity(0,"2020-04-03",3,"레그레이즈",20,10,5,0,60,false,50,60,170,30)
            val e = ExerciseHistoryEntity(0,"2020-04-03",4,"크런치",25,10,6,6,70,true,50,60,170,30)
            val f = ExerciseHistoryEntity(0,"2020-04-03",3,"이두컬",30,10,7,3,30,false,50,60,170,30)
            val g = ExerciseHistoryEntity(0,"2020-04-05",2,"아놀드프레스",35,10,8,3,40,false,60,60,170,40)
            val h = ExerciseHistoryEntity(0,"2020-04-05",1,"숄더프레스",40,10,7,7,50,true,60,60,170,40)
            val i = ExerciseHistoryEntity(0,"2020-04-09",2,"플랭크",0,10,6,1,60,false,70,60,170,50)
            val j = ExerciseHistoryEntity(0,"2020-04-13",3,"풀업",0,10,5,5,70,true,80,60,170,60)
            val k = ExerciseHistoryEntity(0,"2020-04-13",3,"팔굽혀펴기",0,10,4,0,20,false,80,60,170,60)
            val l = ExerciseHistoryEntity(0,"2020-04-15",4,"벤치프레스",30,10,6,6,30,true,100,60,170,70)


            Flowable.just("abc")
                .subscribeOn(Schedulers.io())
                .subscribe {
                    db.ExerciseHistoryDAO().insert(a, b, c, d, e, f, g, h, i, j, k, l)
                }
        }

        historyViewModel.loadHistories()
        historyViewModel.loadSummary()
    }

    private fun setColors() {
        Log.d("jayColor", "history fragment")

        fHistory_calendarView.tileWidth = 150
        fHistory_calendarView.tileHeight = 110

        fHistory_txv_weight.text = getString(R.string.unit_weight, 150)
        fHistory_txv_height.text = getString(R.string.unit_height, 15.1)
/*
        fHistory_txv_sets.text = getString(R.string.unit_sets, 10)
        fHistory_txv_times.text = getString(R.string.unit_time_hour_min, 10, 10)
*/
        when (setting.isNightMode) {
            true -> {
                Log.d("jayColor", "Night time mode")
                // activity!!.getColor(R.color.orange_bg_thick)
                fHistory_calendarView.selectionColor = activity!!.getColor(R.color.orange_bg_thick)
                fHistory_calendarView.setDateTextAppearance(R.style.CalendarDateTextAppearanceNightTime)
                fHistory_calendarView.setHeaderTextAppearance(R.style.CalendarHeaderTextAppearanceNightTime)
                fHistory_calendarView.setWeekDayTextAppearance(R.style.CalendarHeaderTextAppearanceNightTime)

                fHistory_clo_report.setBackgroundColor(activity!!.getColor(R.color.grey_bg_thicker))
                fHistory_txv_report.setTextColor(activity!!.getColor(R.color.black_txt_thick))

                fHistory_txv_profile.setTextColor(activity!!.getColor(R.color.grey_txt_basic))
                fHistory_txv_weight.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fHistory_txv_height.setTextColor(activity!!.getColor(R.color.white_txt_thick))

                fHistory_txv_total.setTextColor(activity!!.getColor(R.color.grey_txt_basic))
                fHistory_txv_sets.setTextColor(activity!!.getColor(R.color.white_txt_thick))
                fHistory_txv_times.setTextColor(activity!!.getColor(R.color.white_txt_thick))
            }
            false -> {
                Log.d("jayColor", "Day time mode")
                fHistory_calendarView.selectionColor = activity!!.getColor(R.color.orange_bg_thick)

                fHistory_calendarView.setDateTextAppearance(R.style.CalendarDateTextAppearanceDayTime)
                fHistory_calendarView.setHeaderTextAppearance(R.style.CalendarHeaderTextAppearanceDayTime)
                fHistory_calendarView.setWeekDayTextAppearance(R.style.CalendarHeaderTextAppearanceDayTime)

                fHistory_clo_report.setBackgroundColor(activity!!.getColor(R.color.grey_bg_light))
                fHistory_txv_report.setTextColor(activity!!.getColor(R.color.grey_txt_thick))

                fHistory_txv_profile.setTextColor(activity!!.getColor(R.color.grey_txt_thick))
                fHistory_txv_weight.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fHistory_txv_height.setTextColor(activity!!.getColor(R.color.black_txt_thick))

                fHistory_txv_total.setTextColor(activity!!.getColor(R.color.grey_txt_thick))
                fHistory_txv_sets.setTextColor(activity!!.getColor(R.color.black_txt_thick))
                fHistory_txv_times.setTextColor(activity!!.getColor(R.color.black_txt_thick))
            }
        }
    }
}
