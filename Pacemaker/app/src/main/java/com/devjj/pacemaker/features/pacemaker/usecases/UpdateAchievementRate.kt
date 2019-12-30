package com.devjj.pacemaker.features.pacemaker.usecases

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import javax.inject.Inject

class UpdateAchievementRate
@Inject constructor(private val historyDetailsRepository: HistoryDetailsRepository) :
    UseCase<Int, String>() {
    override suspend fun run(paramas: String): Either<Failure, Int> = historyDetailsRepository.updateAchievementRate(paramas)
}