package com.devjj.pacemaker.features.pacemaker.history

import android.app.Activity
import android.widget.Toast
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.databinding.FragmentHistoryBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate

class HistoryListener(
    val activity: Activity,
    val binding: FragmentHistoryBinding,
    val navigator: Navigator,
    private val historyViewModel: HistoryViewModel
) {

    fun initListener() {
        binding.fHistoryCalendarView.setOnDateChangedListener { calendarView, date, selected ->
            Dlog.d("date : ${date.date} , selected : $selected")

            calendarView.setDateSelected(
                CalendarDay.from(LocalDate.of(date.year, date.month, date.day)),
                selected.not()
            )
            when (selected) {
                false -> {
                    Dlog.d("date : ${date.date}")
                    navigator.showHistoryDetail(activity, date.date.toString())
                }
                true -> {
                }
            }
        }

        binding.fHistoryCalendarView.setOnMonthChangedListener { widget, date ->
            historyViewModel.loadOneMonthSumOfSetsAndMass(date.date.toString())
            historyViewModel.loadTotalTimes(date.date.toString())
            Dlog.d(date.date.toString())
        }

        binding.fHistoryIvChart.setOnClickListener {
            navigator.showStatistics(activity)
        }

        binding.fHistoryIvCommentMonth.setOnClickListener {

            if (historyViewModel.setsAndMassOneMonth.value != null) {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.fhistory_comment_prefix_month)
                            + getComment(
                        activity,
                        historyViewModel.setsAndMassOneMonth.value!!.mass
                    )
                            + activity.getString(R.string.fhistory_comment_suffix_month),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.fhistory_no_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.fHistoryIvCommentTotal.setOnClickListener {

            if (historyViewModel.setsAndMass.value != null) {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.fhistory_comment_prefix_total)
                            + getComment(activity, historyViewModel.setsAndMass.value!!.mass)
                            + activity.getString(R.string.fhistory_comment_suffix_total),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.fhistory_no_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}