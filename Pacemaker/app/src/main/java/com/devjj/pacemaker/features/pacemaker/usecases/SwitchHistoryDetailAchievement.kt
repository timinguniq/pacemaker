package com.devjj.pacemaker.features.pacemaker.usecases


import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import javax.inject.Inject

class SwitchHistoryDetailAchievement
@Inject constructor(private val historyDetailsRepository: HistoryDetailsRepository) : UseCase<Int,Int>(){
    override suspend fun run(paramas: Int): Either<Failure, Int> = historyDetailsRepository.switchAchievement(paramas)
}