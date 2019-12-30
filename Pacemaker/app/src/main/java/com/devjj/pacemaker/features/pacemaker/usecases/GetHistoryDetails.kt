package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetail
import javax.inject.Inject

class GetHistoryDetails
@Inject constructor(private val historyDetailsRepository: HistoryDetailsRepository) : UseCase<List<HistoryDetail>,String>(){
    override suspend fun run(paramas: String) = historyDetailsRepository.historyDetails(paramas)
}