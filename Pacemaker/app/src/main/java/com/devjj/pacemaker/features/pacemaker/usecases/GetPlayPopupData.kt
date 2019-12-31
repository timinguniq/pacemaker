package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import com.devjj.pacemaker.features.pacemaker.play.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.play.PlayPopupRepository
import javax.inject.Inject

class GetPlayPopupData
@Inject constructor(private val playPopupRepository: PlayPopupRepository) : UseCase<List<PlayPopupData>, UseCase.None>(){
    override suspend fun run(paramas: None) = playPopupRepository.playPopupData()
}