package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.statistics.Statistics
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsRepository
import javax.inject.Inject

class GetStatistics
@Inject constructor(private val statisticsRepository: StatisticsRepository) : UseCase<List<Statistics>, None>(){
    override suspend fun run(paramas: None) = statisticsRepository.statistics()
}