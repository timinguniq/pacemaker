package com.devjj.pacemaker.features.pacemaker.usecases;

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import com.devjj.pacemaker.features.pacemaker.history.SumOfSetsAndMass
import javax.inject.Inject

class GetSumOfSetsAndMass
@Inject constructor(private val historiesRepository: HistoriesRepository) :
    UseCase<SumOfSetsAndMass, None>() {
    override suspend fun run(paramas: None) = historiesRepository.sumOfSetsAndMass()
}
