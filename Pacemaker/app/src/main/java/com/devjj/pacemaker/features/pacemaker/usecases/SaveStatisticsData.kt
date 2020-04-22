package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupRepository
import javax.inject.Inject

class SaveStatisticsData
@Inject constructor(private val playPopupRepository: PlayPopupRepository) : UseCase<PlayPopupData, SaveStatisticsData.Params>(){
    override suspend fun run(paramas: Params) = playPopupRepository.insertStatisticsData(paramas.todaySetDone, paramas.todaySetGoal)

    data class Params(val todaySetDone: Int, val todaySetGoal: Int)
}