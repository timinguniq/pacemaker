package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCaseDatabase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.addition.AdditionRepository
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import javax.inject.Inject

class UpdateExerciseData
@Inject constructor(private val additionRepository: AdditionRepository) : UseCaseDatabase<AdditionData, UpdateExerciseData.Params>(){
    override suspend fun run(params: Params) = additionRepository.updateExerciseData(params.additionData)

    data class Params(val additionData: AdditionData)
}