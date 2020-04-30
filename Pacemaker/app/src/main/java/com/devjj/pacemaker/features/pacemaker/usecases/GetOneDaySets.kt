package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import com.devjj.pacemaker.features.pacemaker.historydetail.OneDaySets
import javax.inject.Inject


class GetOneDaySets
@Inject constructor(private val historyDetailsRepository: HistoryDetailsRepository) : UseCase<OneDaySets,String>(){
    override suspend fun run(paramas: String) = historyDetailsRepository.getOneDaySets(paramas)
}