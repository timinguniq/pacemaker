package com.devjj.pacemaker.features.pacemaker.history

import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.convertPxToDp
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import com.devjj.pacemaker.features.pacemaker.entities.StatisticsEntity
import com.prolificinteractive.materialcalendarview.CalendarDay
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
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



    override fun onResume() {
        super.onResume()
        initializeView()
    }

    private fun renderHistoryList(histories: List<HistoryView>?) {
        //historyAdapter.collection = histories.orEmpty()
        when (histories) {
            null -> {
                Log.d("calendar", "empty")
            }
            else ->
                for (history in histories) {
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

    private fun renderSetsAndMass(sumOfSetsAndMass: SumOfSetsAndMass?) {

        fHistory_tv_total_sets.text = getString(R.string.unit_sets, sumOfSetsAndMass!!.sets)
      //  fHistory_tv_total_times.text = getString(R.string.unit_time_hour_min, sumOfSetsAndMass.times / 60, sumOfSetsAndMass.times % 60)
        fHistory_tv_total_kgs.text = getString(R.string.unit_mass, sumOfSetsAndMass.mass)
    }

    private fun renderSetsAndMassOneMonthOneMonth(sumOfSetsAndMass : SumOfSetsAndMass?){

        fHistory_tv_month_sets.text = getString(R.string.unit_sets, sumOfSetsAndMass!!.sets)
      //  fHistory_tv_month_times.text = getString(R.string.unit_time_hour_min, sumOfSetsAndMass.times / 60, sumOfSetsAndMass.times % 60)
        fHistory_tv_month_kgs.text = getString(R.string.unit_mass, sumOfSetsAndMass.mass)
       // Log.d("jayTotaltime", sumOfSetsAndMass.times.toString())
    }

    private fun renderTotalTimes(totalTimes: TotalTimes?){
        fHistory_tv_total_times.text = getString(R.string.unit_time_hour_min, totalTimes!!.totalTime / 60, totalTimes.totalTime % 60)
        fHistory_tv_month_times.text = getString(R.string.unit_time_hour_min, totalTimes.totalTimeOneMonth / 60, totalTimes.totalTimeOneMonth % 60)
    }

    private fun initializeView() {
        setColors()
        this.activity!!.aPacemaker_tv_title.text = this.getString(R.string.fhistory_tv_title_str)
        //fHistory_recyclerview.layoutManager = LinearLayoutManager(activity)
        //fHistory_recyclerview.adapter = historyAdapter
        fHistory_tv_height.text= getString(R.string.unit_height,setting.height)
        fHistory_tv_weight.text= getString(R.string.unit_weight,setting.weight)
        historyListener = HistoryListener(activity!!, navigator,historyViewModel)
        historyListener.initListener()

        runBlocking {
            launch(Dispatchers.IO) {
                statisticsDB.StatisticsDAO().getAll()
            }

        }

        /*
        historyAdapter.clickListener = { date ->
            navigator.showHistoryDetail(activity!!, date)
        }
*/


/*
        if (false) {
            val a = ExerciseHistoryEntity(0,"2020-03-30",0,"벤치프레스",5,10,2,2,30)

            val b = ExerciseHistoryEntity(0,"2020-04-01",1,"데드리프트",10,10,3,1,40)
            val c = ExerciseHistoryEntity(0,"2020-04-01",2,"스쿼드",15,10,4,4,50)

            val d = ExerciseHistoryEntity(0,"2020-04-03",3,"레그레이즈",20,10,5,0,60)
            val e = ExerciseHistoryEntity(0,"2020-04-03",4,"크런치",25,10,6,6,70)
            val f = ExerciseHistoryEntity(0,"2020-04-03",3,"이두컬",30,10,7,3,30)

            val g = ExerciseHistoryEntity(0,"2020-04-05",2,"아놀드프레스",35,10,8,3,40)
            val h = ExerciseHistoryEntity(0,"2020-04-05",1,"숄더프레스",40,10,7,7,50)

            val i = ExerciseHistoryEntity(0,"2020-04-09",2,"플랭크",0,10,6,1,60)

            val j = ExerciseHistoryEntity(0,"2020-04-13",3,"풀업",0,10,5,5,70)
            val k = ExerciseHistoryEntity(0,"2020-04-13",3,"팔굽혀펴기",0,10,4,0,20)

            val l = ExerciseHistoryEntity(0,"2020-04-15",4,"벤치프레스",30,10,6,6,30)

            val A = StatisticsEntity(0,"2020-03-30",110f, 60f,10,100)
            val B = StatisticsEntity(0,"2020-04-01",120f, 70f,20,71)
            val C = StatisticsEntity(0,"2020-04-03",130f, 80f,30,50)
            val D = StatisticsEntity(0,"2020-04-05",120f, 90f,40,66)
            val E = StatisticsEntity(0,"2020-04-09",130f, 100f,50,16)
            val F = StatisticsEntity(0,"2020-04-13",140f, 110f,60,55)
            val G = StatisticsEntity(0,"2020-04-15",150f, 120f,70,100)

            Flowable.just("abc")
                .subscribeOn(Schedulers.io())
                .subscribe {
                    db.ExerciseHistoryDAO().insert(a, b, c, d, e, f, g, h, i, j, k, l)
                    statisticsDB.StatisticsDAO().insert(A,B,C,D,E,F,G)
                }
        }
        */

        @NonNull
        val today = CalendarDay.today().date.toString()

        historyViewModel.loadHistories()
        historyViewModel.loadSumOfSetsAndMass()
        historyViewModel.loadOneMonthSumOfSetsAndMass(today)
        historyViewModel.loadTotalTimes(today)
        Log.d("jayDateCheck", today)
    }

    private fun setColors() {
        Log.d("jayColor", "history fragment")

        fHistory_calendarView.tileWidth = convertPxToDp(context!!,400f).toInt()
        fHistory_calendarView.tileHeight = convertPxToDp(context!!,250f).toInt()

/*
        fHistory_txv_sets.text = getString(R.string.unit_sets, 10)
        fHistory_txv_times.text = getString(R.string.unit_time_hour_min, 10, 10)
*/
        when (setting.isNightMode) {
            true -> {
                Log.d("jayColor", "Night time mode")
                // activity!!.getColor(R.color.orange_F74938)
                fHistory_calendarView.selectionColor = activity!!.getColor(R.color.orange_F74938)
                fHistory_calendarView.setDateTextAppearance(R.style.CalendarDateTextAppearanceNightTime)
                fHistory_calendarView.setHeaderTextAppearance(R.style.CalendarHeaderTextAppearanceNightTime)
                fHistory_calendarView.setWeekDayTextAppearance(R.style.CalendarHeaderTextAppearanceNightTime)

                fHistory_clo_report.setBackgroundColor(activity!!.getColor(R.color.grey_88898A))
                fHistory_tv_report.setTextColor(activity!!.getColor(R.color.black_3B4046))

                fHistory_tv_profile.setTextColor(activity!!.getColor(R.color.grey_AEB3B3))
                fHistory_tv_weight.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
                fHistory_tv_height.setTextColor(activity!!.getColor(R.color.white_F7FAFD))

                fHistory_tv_total.setTextColor(activity!!.getColor(R.color.grey_AEB3B3))
                fHistory_tv_total_sets.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
                fHistory_tv_total_times.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
                fHistory_tv_total_kgs.setTextColor(activity!!.getColor(R.color.white_F7FAFD))

                fHistory_tv_month.setTextColor(activity!!.getColor(R.color.grey_AEB3B3))
                fHistory_tv_month_sets.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
                fHistory_tv_month_times.setTextColor(activity!!.getColor(R.color.white_F7FAFD))
                fHistory_tv_month_kgs.setTextColor(activity!!.getColor(R.color.white_F7FAFD))

                fHistory_iv_chart.setBackgroundResource(R.drawable.fhistory_button_bg_color_nighttime)
            }
            false -> {
                Log.d("jayColor", "Day time mode")
                fHistory_calendarView.selectionColor = activity!!.getColor(R.color.orange_F74938)

                fHistory_calendarView.setDateTextAppearance(R.style.CalendarDateTextAppearanceDayTime)
                fHistory_calendarView.setHeaderTextAppearance(R.style.CalendarHeaderTextAppearanceDayTime)
                fHistory_calendarView.setWeekDayTextAppearance(R.style.CalendarHeaderTextAppearanceDayTime)

                fHistory_clo_report.setBackgroundColor(activity!!.getColor(R.color.grey_F9F9F9_70))
                fHistory_tv_report.setTextColor(activity!!.getColor(R.color.grey_87888A))

                fHistory_tv_profile.setTextColor(activity!!.getColor(R.color.grey_87888A))
                fHistory_tv_weight.setTextColor(activity!!.getColor(R.color.black_3B4046))
                fHistory_tv_height.setTextColor(activity!!.getColor(R.color.black_3B4046))

                fHistory_tv_total.setTextColor(activity!!.getColor(R.color.grey_87888A))
                fHistory_tv_total_sets.setTextColor(activity!!.getColor(R.color.black_3B4046))
                fHistory_tv_total_times.setTextColor(activity!!.getColor(R.color.black_3B4046))
                fHistory_tv_total_kgs.setTextColor(activity!!.getColor(R.color.black_3B4046))

                fHistory_tv_month.setTextColor(activity!!.getColor(R.color.grey_87888A))
                fHistory_tv_month_sets.setTextColor(activity!!.getColor(R.color.black_3B4046))
                fHistory_tv_month_times.setTextColor(activity!!.getColor(R.color.black_3B4046))
                fHistory_tv_month_kgs.setTextColor(activity!!.getColor(R.color.black_3B4046))

                fHistory_iv_chart.setBackgroundResource(R.drawable.fhistory_button_bg_color_daytime)
            }
        }
    }
}
