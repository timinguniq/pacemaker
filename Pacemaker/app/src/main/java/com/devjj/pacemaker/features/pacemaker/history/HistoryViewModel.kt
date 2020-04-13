package com.devjj.pacemaker.features.pacemaker.history

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetExerciseHistories
import com.devjj.pacemaker.features.pacemaker.usecases.GetSummary
import javax.inject.Inject

class HistoryViewModel
@Inject constructor(private val getExerciseHistories: GetExerciseHistories,private val getSummary: GetSummary) : BaseViewModel() {
    var histories : MutableLiveData<List<HistoryView>> = MutableLiveData()
    var summary : MutableLiveData<Summary> = MutableLiveData()
    fun loadHistories() = getExerciseHistories(None()) {it.fold(::handleFailure,::handleHistoryList)}
    fun loadSummary() = getSummary(None()){it.fold(::handleFailure,::handleSummary)}

    private fun handleSummary(summary: Summary){
        this.summary.value = summary
    }

    private fun handleHistoryList(histores: List<History>){
        this.histories.value = histores.map { HistoryView(it.id,it.date,it.achievementRate) }
    }
}