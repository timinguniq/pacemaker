package com.devjj.pacemaker.features.pacemaker.usecases;

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import com.devjj.pacemaker.features.pacemaker.history.Summary
import javax.inject.Inject

class GetSummary
@Inject constructor(private val historiesRepository: HistoriesRepository) :
    UseCase<Summary, None>() {
    //override suspend fun run(paramas: None) = historiesRepository.summary()
    override suspend fun run(paramas: None): Either<Failure, Summary> {
        TODO("Not yet implemented")
    }
}
