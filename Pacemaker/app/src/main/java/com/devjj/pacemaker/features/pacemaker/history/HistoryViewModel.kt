package com.devjj.pacemaker.features.pacemaker.history

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.*
import javax.inject.Inject

class HistoryViewModel
@Inject constructor(
    private val getExerciseHistories: GetExerciseHistories,
    private val getSumOfSetsAndMass: GetSumOfSetsAndMass,
    private val getOneMonthSumOfSetsAndMass: GetOneMonthSumOfSetsAndMass,
    private val getTotalTimes : GetTotalTimes
) : BaseViewModel() {
    var histories: MutableLiveData<List<HistoryView>> = MutableLiveData()
    var setsAndMass: MutableLiveData<SumOfSetsAndMass> = MutableLiveData()
    var setsAndMassOneMonth : MutableLiveData<SumOfSetsAndMass> = MutableLiveData()
    var totalTimes : MutableLiveData<TotalTimes> = MutableLiveData()

    fun loadHistories() =
        getExerciseHistories(None()) { it.fold(::handleFailure, ::handleHistoryList) }
    fun loadSumOfSetsAndMass() = getSumOfSetsAndMass(None()) { it.fold(::handleFailure, ::handleSetsAndMass) }
    fun loadOneMonthSumOfSetsAndMass(date :String) = getOneMonthSumOfSetsAndMass(date) {it.fold(::handleFailure , ::handleSetsAndMassOneMonth)}
    fun loadTotalTimes(date: String) = getTotalTimes(date) {it.fold(::handleFailure, ::handleTotalTimes)}

    private fun handleSetsAndMass(sumOfSetsAndMass: SumOfSetsAndMass) {
        this.setsAndMass.value = sumOfSetsAndMass
    }

    private fun handleSetsAndMassOneMonth(sumOfSetsAndMass:SumOfSetsAndMass){
        this.setsAndMassOneMonth.value = sumOfSetsAndMass
    }

    private fun handleTotalTimes(totalTimes: TotalTimes){
        this.totalTimes.value = totalTimes
    }

    private fun handleHistoryList(histories: List<History>) {
        this.histories.value = histories.map { HistoryView(it.id, it.date) }
    }


}