package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupRepository
import javax.inject.Inject

class UpdateExercisePlayPopupData
@Inject constructor(private val playPopupRepository: PlayPopupRepository) : UseCase<Unit, UpdateExercisePlayPopupData.Params>(){
    override suspend fun run(paramas: Params) = playPopupRepository.updateExerciseData(paramas.playPopupData)

    data class Params(val playPopupData: PlayPopupData)
}