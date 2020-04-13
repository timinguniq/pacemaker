package com.devjj.pacemaker.features.pacemaker.historydetail

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetHistoryDetails
import com.devjj.pacemaker.features.pacemaker.usecases.GetOneDaySummary
import com.devjj.pacemaker.features.pacemaker.usecases.SwitchHistoryDetailAchievement
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateAchievementRate
import javax.inject.Inject

class HistoryDetailViewModel
@Inject constructor(
    private val getHistoryDetails: GetHistoryDetails,
    private val switchHistoryDetailAchievement: SwitchHistoryDetailAchievement,
    private val updateAchievementRate: UpdateAchievementRate,
    private val getOneDaySummary: GetOneDaySummary
) : BaseViewModel() {

    var historyDetails: MutableLiveData<List<HistoryDetailView>> = MutableLiveData()
    var oneDaySummary : MutableLiveData<OneDaySummary> = MutableLiveData()

    fun switchAchievementById(id: Int) = switchHistoryDetailAchievement(id)
    fun updateAchievementRateByDate(date: String) = updateAchievementRate(date)
    fun loadHistoryDetails(date: String) =
        getHistoryDetails(date) { it.fold(::handleFailure, ::handleHistoryDetailList) }
    fun loadOneDaySummary(date:String) = getOneDaySummary(date){it.fold(::handleFailure, ::handleOneDaySummary)}

    private fun handleOneDaySummary(oneDaySummary: OneDaySummary){
        this.oneDaySummary.value = oneDaySummary
    }


    private fun handleHistoryDetailList(historyDetails: List<HistoryDetail>) {
        this.historyDetails.value = historyDetails.map {
            HistoryDetailView(
                it.id,
                it.date,
                it.part_img,
                it.name,
                it.mass,
                it.rep,
                it.set,
                it.interval,
                it.achievement,
                it.achievementRate
            )
        }
    }
}
