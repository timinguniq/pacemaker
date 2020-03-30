package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.addition.AdditionRepository
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupRepository
import javax.inject.Inject

class SaveExerciseHistoryData
@Inject constructor(private val playPopupRepository: PlayPopupRepository) : UseCase<PlayPopupData, SaveExerciseHistoryData.Params>(){
    override suspend fun run(paramas: Params) = playPopupRepository.insertExerciseHistoryData(paramas.playPopupData)

    data class Params(val playPopupData: PlayPopupData)
}