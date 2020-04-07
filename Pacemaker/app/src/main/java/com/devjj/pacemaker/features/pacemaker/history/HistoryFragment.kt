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
import kotlinx.android.synthetic.main.activity_pacemaker.*
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
                    Log.d("test", "datess ${history.date}")
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
        this.activity!!.aPacemaker_tv_title.text = this.getString(R.string.fHistory_title_txv)
        fHistory_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistory_recyclerview.adapter = historyAdapter

        historyAdapter.clickListener = { date ->
            navigator.showHistoryDetail(activity!!, date)
        }

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
