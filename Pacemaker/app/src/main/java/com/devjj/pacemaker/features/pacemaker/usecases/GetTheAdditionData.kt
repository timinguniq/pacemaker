package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCaseDatabase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.addition.AdditionRepository
import com.devjj.pacemaker.features.pacemaker.usecases.GetTheAdditionData.Params
import javax.inject.Inject

class GetTheAdditionData
@Inject constructor(private val additionRepository: AdditionRepository) : UseCaseDatabase<AdditionData, Params>(){
    override suspend fun run(params: Params) = additionRepository.theAdditionData(params.id)

    data class Params(val id: Int)
}