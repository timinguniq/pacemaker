package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import javax.inject.Inject

class SwapExerciseData
@Inject constructor(private val homeRepository: HomeRepository) : UseCase<HomeData, SwapExerciseData.Params>(){
    override suspend fun run(paramas: Params) = homeRepository.swapExerciseData(paramas.homeData1Id, paramas.homeData2Id)

    data class Params(val homeData1Id: Int, val homeData2Id: Int)
}