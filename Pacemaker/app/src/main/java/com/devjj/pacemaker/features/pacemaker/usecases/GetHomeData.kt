package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.interactor.UseCaseDatabase
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import javax.inject.Inject

class GetHomeData
@Inject constructor(private val homeRepository: HomeRepository) : UseCaseDatabase<List<HomeData>, UseCase.None>(){
    override suspend fun run(paramas: UseCase.None) = homeRepository.homeData()
}