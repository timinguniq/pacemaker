package com.devjj.pacemaker.features.pacemaker.history

import android.app.Activity
import android.util.Log
import com.devjj.pacemaker.core.navigation.Navigator
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.fragment_history.*
import org.threeten.bp.LocalDate

class HistoryListener(val activity : Activity, val navigator: Navigator){

    fun initListener(){
        activity.fHistory_calendarView.setOnDateChangedListener { calendarView, date, selected ->
            Log.d("calendarCheck", "date : ${date.date} , selected : $selected")

            calendarView.setDateSelected(
                CalendarDay.from(LocalDate.of(date.year, date.month, date.day)),
                selected.not()
            )
            when(selected){
                false->{
                    Log.d("calendarCheck", "date : ${date.date.toString()}")
                    navigator.showHistoryDetail(activity, date.date.toString())
                }
                true->{}
            }
        }
    }
}