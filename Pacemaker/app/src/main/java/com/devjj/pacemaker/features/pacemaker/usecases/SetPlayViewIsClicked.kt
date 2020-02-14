package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import javax.inject.Inject

class SetPlayViewIsClicked
@Inject constructor(private val homeRepository: HomeRepository) : UseCase<Unit, SetPlayViewIsClicked.Params>(){
    override suspend fun run(paramas: Params) = homeRepository.setPlayViewClick(paramas.isClicked)

    data class Params(val isClicked: Boolean)
}