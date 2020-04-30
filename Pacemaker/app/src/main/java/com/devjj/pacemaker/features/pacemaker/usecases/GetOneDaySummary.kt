package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import com.devjj.pacemaker.features.pacemaker.historydetail.OneDaySummary
import javax.inject.Inject

class GetOneDaySummary
@Inject constructor(private val historyDetailsRepository: HistoryDetailsRepository) : UseCase<OneDaySummary,String>(){
    //override suspend fun run(paramas: String) = historyDetailsRepository.getOneDaySummary(paramas)
    override suspend fun run(paramas: String): Either<Failure, OneDaySummary> {
        TODO("Not yet implemented")
    }
}