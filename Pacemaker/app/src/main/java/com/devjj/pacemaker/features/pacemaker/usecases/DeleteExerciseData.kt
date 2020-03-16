package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import javax.inject.Inject

class DeleteExerciseData
@Inject constructor(private val homeRepository: HomeRepository) : UseCase<HomeData, DeleteExerciseData.Params>(){
    override suspend fun run(paramas: Params) = homeRepository.deleteExerciseData(paramas.homeData)

    data class Params(val homeData: HomeData)
}