package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import com.devjj.pacemaker.features.pacemaker.history.History
import javax.inject.Inject

class GetExerciseHistories
@Inject constructor(private val historiesRepository: HistoriesRepository) : UseCase<List<History>,None>(){
    override suspend fun run(paramas: None) = historiesRepository.histories()
}