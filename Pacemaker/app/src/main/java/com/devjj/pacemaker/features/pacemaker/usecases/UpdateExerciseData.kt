package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.addition.AdditionRepository
import javax.inject.Inject

class UpdateExerciseData
@Inject constructor(private val additionRepository: AdditionRepository) : UseCase<AdditionData, UpdateExerciseData.Params>(){
    override suspend fun run(paramas: Params) = additionRepository.updateExerciseData(paramas.additionData)

    data class Params(val additionData: AdditionData)
}