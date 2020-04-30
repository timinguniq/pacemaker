package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import com.devjj.pacemaker.features.pacemaker.history.Summary
import javax.inject.Inject

class GetOneMonthSummary
@Inject constructor(private val historiesRepository: HistoriesRepository ) : UseCase<Summary,String>() {
    //override suspend fun run(paramas: String) = historiesRepository.summaryOneMonth(paramas)
    override suspend fun run(paramas: String): Either<Failure, Summary> {
        TODO("Not yet implemented")
    }
}