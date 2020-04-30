package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import com.devjj.pacemaker.features.pacemaker.historydetail.StatisticsOneDay
import javax.inject.Inject

class GetStatisticsOneDay
@Inject constructor(private val historyDetailsRepository: HistoryDetailsRepository) : UseCase<StatisticsOneDay, String>(){
    override suspend fun run(paramas: String) = historyDetailsRepository.getStatisticsOneDay(paramas)
}