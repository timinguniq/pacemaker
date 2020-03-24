package com.devjj.pacemaker.features.pacemaker.history

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import com.prolificinteractive.materialcalendarview.CalendarDay
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_history.*
import org.threeten.bp.LocalDate
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        historyViewModel = viewModel(viewModelFactory) {
            observe(histories, ::renderHistoryList)
            //    failure(failure, ::handleFailure)
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
                    var dates = history.date.split("-")
                    Log.d("calendarcheck", "${dates[0]} , ${dates[1]} , ${dates[2]}")
                    fHistory_calendarView.setDateSelected(
                        CalendarDay.from(LocalDate.of(dates[0].toInt(), dates[1].toInt(), dates[2].toInt()))
                        ,true
                    )
                }
        }
    }

    private fun initializeView() {
        fHistory_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistory_recyclerview.adapter = historyAdapter

        historyAdapter.clickListener = { date ->
            navigator.showHistoryDetail(activity!!, date)
        }

/*
        val a = ExerciseHistoryEntity(0, "2019-12-30", 0, "벤치프레스", 5, 10, 2, 30, true, 30)
        val b = ExerciseHistoryEntity(1, "2020-01-01", 1, "데드리프트", 10, 10, 3, 40, false, 40)
        val c = ExerciseHistoryEntity(2, "2020-01-01", 2, "스쿼드", 15, 10, 4, 50, true, 40)
        val d = ExerciseHistoryEntity(3, "2020-01-03", 3, "레그레이즈", 20, 10, 5, 60, false, 50)
        val e = ExerciseHistoryEntity(4, "2020-01-03", 4, "크런치", 25, 10, 6, 70, true, 50)
        val f = ExerciseHistoryEntity(5, "2020-01-03", 3, "이두컬", 30, 10, 7, 30, false, 50)
        val g = ExerciseHistoryEntity(6, "2020-01-05", 2, "아놀드프레스", 35, 10, 8, 40, false, 60)
        val h = ExerciseHistoryEntity(7, "2020-01-05", 1, "숄더프레스", 40, 10, 7, 50, true, 60)
        val i = ExerciseHistoryEntity(8, "2020-01-09", 2, "플랭크", 0, 10, 6, 60, false, 70)
        val j = ExerciseHistoryEntity(9, "2020-01-13", 3, "풀업", 0, 10, 5, 70, true, 80)
        val k = ExerciseHistoryEntity(10, "2020-01-13", 3, "팔굽혀펴기", 0, 10, 4, 20, false, 80)
        val l = ExerciseHistoryEntity(11, "2020-01-15", 4, "벤치프레스", 30, 10, 6, 30, true, 100)


        Flowable.just("abc")
            .subscribeOn(Schedulers.io())
            .subscribe {
                db.ExerciseHistoryDAO().insert(a, b, c, d, e, f, g, h, i, j, k, l)
            }
*/
        historyViewModel.loadHistories()
/*
        for(history in historyViewModel.histories.value!!.toList()){
            Log.d("calendar", "date : ${history.date}")
        }*/


        fHistory_calendarView.setOnDateChangedListener { calendarView, date, selected ->

            Log.d("calendarcheck", "date : ${date.date} , selected : $selected")

            calendarView.setDateSelected(
                CalendarDay.from(LocalDate.of(date.year, date.month, date.day)),
                selected.not()
            )

            when(selected){
                false->{
                    Log.d("calendarcheck", "date : ${date.date.toString()}")
                    navigator.showHistoryDetail(activity!!, date.date.toString())
                }
                true->{}
            }
/*
            Log.d("calendar", "date : ${date.date} , selected : $selected")
            calendarView.setDateSelected(
                CalendarDay.from(LocalDate.of(date.year, date.month, 1)),
                selected
            )
            calendarView.setDateSelected(
                CalendarDay.from(LocalDate.of(date.year, date.month, 11)),
                selected
            )
            calendarView.setDateSelected(
                CalendarDay.from(LocalDate.of(date.year, date.month, 21)),
                selected
            )
            calendarView.setDateSelected(
                CalendarDay.from(
                    LocalDate.of(
                        date.year,
                        date.month + 1,
                        1
                    )
                ), selected
            )
            calendarView.setDateSelected(
                CalendarDay.from(
                    LocalDate.of(
                        date.year,
                        date.month + 1,
                        11
                    )
                ), selected
            )
            calendarView.setDateSelected(
                CalendarDay.from(
                    LocalDate.of(
                        date.year,
                        date.month + 1,
                        21
                    )
                ), selected
            )
            calendarView.setDateSelected(
                CalendarDay.from(
                    LocalDate.of(
                        date.year,
                        date.month + 1,
                        date.date.plusMonths(1).lengthOfMonth()
                    )
                ), selected
            )
*/

            // var dat= Date()
            //val dat = getInstance()
            /*var dat = getInstance()
            dat.set(year, month, date)
            dat[YEAR] = year
            dat[MONTH] = month
            dat[DATE] =date
            calendarView.date = dat.timeInMillis*/

            //   var dat = SimpleDateFormat("yy-MM-dd").format(date.date)
            //   Log.d("calendar","$dat" )
            //Log.d("calendar","${dat[YEAR]}")
            // var newDate = Date(year,month,date+1)
            //calendarView.date = newDate.time
        }
    }

}
