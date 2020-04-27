package com.devjj.pacemaker.features.pacemaker.statistics

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetStatistics
import javax.inject.Inject

class StatisticsViewModel
@Inject constructor(
    private val getStatistics: GetStatistics
): BaseViewModel() {
    var statistics : MutableLiveData<List<StatisticsView>> = MutableLiveData()

    fun loadStatistics() = getStatistics(None()){ it.fold(::handleFailure , ::handleStatistics) }


    private fun handleStatistics(statistics: List<Statistics>){
        this.statistics.value = statistics.map {
            StatisticsView(it.id, it.date, it.height, it.weight)
        }
    }
}
