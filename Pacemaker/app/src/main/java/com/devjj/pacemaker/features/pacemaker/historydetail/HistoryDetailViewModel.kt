package com.devjj.pacemaker.features.pacemaker.historydetail

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.*
import javax.inject.Inject

class HistoryDetailViewModel
@Inject constructor(
    private val getHistoryDetails: GetHistoryDetails,
    private val getStatisticsOneDay: GetStatisticsOneDay,
    private val getOneDaySets: GetOneDaySets
) : BaseViewModel() {

    var historyDetails: MutableLiveData<List<HistoryDetailView>> = MutableLiveData()
    var statisticsOneDay : MutableLiveData<StatisticsOneDay> = MutableLiveData()
    var oneDaySets : MutableLiveData<OneDaySets> = MutableLiveData()

    fun loadHistoryDetails(date: String) =
        getHistoryDetails(date) { it.fold(::handleFailure, ::handleHistoryDetailList) }
    fun loadStatisticsOneDay(date:String) = getStatisticsOneDay(date){it.fold(::handleFailure, ::handleStatisticsOneDay)}
    fun loadOneDaySets(date:String) = getOneDaySets(date){it.fold(::handleFailure, ::handleOneDaySets)}

    private fun handleStatisticsOneDay(statisticsOneDay: StatisticsOneDay){
        this.statisticsOneDay.value = statisticsOneDay
    }

    private fun handleOneDaySets(oneDaySets: OneDaySets){
        this.oneDaySets.value = oneDaySets
    }


    private fun handleHistoryDetailList(historyDetails: List<HistoryDetail>) {
        this.historyDetails.value = historyDetails.map {
            HistoryDetailView(
                it.id,
                it.date,
                it.part_img,
                it.name,
                it.mass,
                it.rep,
                it.setGoal,
                it.setDone,
                it.interval
            )
        }
    }
}
