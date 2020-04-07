package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupRepository
import javax.inject.Inject

class DeleteExerciseHistoryData
@Inject constructor(private val playPopupRepository: PlayPopupRepository) : UseCase<PlayPopupData, UseCase.None>(){
    override suspend fun run(paramas: None) = playPopupRepository.deleteExerciseHistoryData()

}