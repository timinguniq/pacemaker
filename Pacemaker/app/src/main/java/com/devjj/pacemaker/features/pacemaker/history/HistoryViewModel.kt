package com.devjj.pacemaker.features.pacemaker.history

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetExerciseHistories
import javax.inject.Inject

class HistoryViewModel
@Inject constructor(private val getExerciseHistories: GetExerciseHistories) : BaseViewModel() {
    var histories : MutableLiveData<List<HistoryView>> = MutableLiveData()

    fun loadHistories() = getExerciseHistories(None()) {it.fold(::handleFailure,::handleHistoryList)}

    private fun handleHistoryList(histores: List<History>){
        this.histories.value = histores.map { HistoryView(it.id,it.date,it.achievementRate) }
    }
}