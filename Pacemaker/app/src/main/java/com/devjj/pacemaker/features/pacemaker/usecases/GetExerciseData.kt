package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCaseDatabase
import com.devjj.pacemaker.features.pacemaker.home.ExerciseData
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import javax.inject.Inject

class GetExerciseData
@Inject constructor(private val homeRepository: HomeRepository) : UseCaseDatabase<List<ExerciseData>, UseCase.None>(){
    override suspend fun run(paramas: UseCase.None) = homeRepository.exerciseData()
}