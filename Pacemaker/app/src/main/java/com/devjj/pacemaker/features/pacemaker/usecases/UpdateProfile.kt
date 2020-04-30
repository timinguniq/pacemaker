package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsRepository
import javax.inject.Inject

class UpdateProfile
@Inject constructor(private val statisticsRepository: StatisticsRepository) : UseCase<Unit,UpdateProfile.Params>(){
    override suspend fun run(paramas: Params) = statisticsRepository.updateProfile(paramas.date, paramas.height,paramas.weight)

    data class Params(val date : String, val height : Float,val weight : Float)
}