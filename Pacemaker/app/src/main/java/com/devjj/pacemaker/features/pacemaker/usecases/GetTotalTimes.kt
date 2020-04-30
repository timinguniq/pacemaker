package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import com.devjj.pacemaker.features.pacemaker.history.TotalTimes
import javax.inject.Inject

class GetTotalTimes
@Inject constructor(private val historiesRepository: HistoriesRepository) : UseCase<TotalTimes, String>(){
    override suspend fun run(paramas: String) = historiesRepository.totalTimes(paramas)
}