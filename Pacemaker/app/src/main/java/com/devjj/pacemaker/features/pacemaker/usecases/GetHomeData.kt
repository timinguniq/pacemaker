package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import javax.inject.Inject

class GetHomeData
@Inject constructor(private val homeRepository: HomeRepository) : UseCase<List<HomeData>, UseCase.None>(){
    override suspend fun run(paramas: None) = homeRepository.homeData()
}