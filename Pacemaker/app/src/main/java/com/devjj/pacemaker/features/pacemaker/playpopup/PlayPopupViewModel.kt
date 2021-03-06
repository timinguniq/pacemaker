package com.devjj.pacemaker.features.pacemaker.playpopup

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.*
import javax.inject.Inject

class PlayPopupViewModel
@Inject constructor(private val getPlayPopupData: GetPlayPopupData,
                    private val updateExercisePlayPopupData: UpdateExercisePlayPopupData,
                    private val saveExerciseHistoryData: SaveExerciseHistoryData,
                    private val deleteExerciseHistoryData: DeleteExerciseHistoryData,
                    private val saveStatisticsData: SaveStatisticsData) : BaseViewModel() {

    var totalTime : MutableLiveData<Int> = MutableLiveData()

    var playPopupList: MutableLiveData<List<PlayPopupView>> = MutableLiveData()

    var existPlayPopupList: MutableLiveData<List<PlayPopupView>> = MutableLiveData()

    var playPopupData: MutableLiveData<PlayPopupView> = MutableLiveData()

    var playPopupStatisticsData: MutableLiveData<PlayPopupView> = MutableLiveData()

    fun loadPlayPopupList() = getPlayPopupData(UseCase.None()) {it.fold(::handleFailure, ::handlePlayPopupData)}

    fun existPlayPopupList() = getPlayPopupData(UseCase.None()) {it.fold(::handleFailure, ::handleExistPlayPopupData)}

    fun updateExercisePlayPopupData(playPopupData: PlayPopupData) =
        updateExercisePlayPopupData(UpdateExercisePlayPopupData.Params(playPopupData))

    // PlayPopupData를 ExerciseEntity로 변환해서 저장하는 함수.
    fun saveExerciseHistoryData(playPopupData: PlayPopupData) {
        saveExerciseHistoryData(SaveExerciseHistoryData.Params(playPopupData)) {
            it.fold(
                ::handleFailure,
                ::handleTheExerciseHistoryData
            )
        }
    }

    fun deleteExerciseHistoryData() =
        deleteExerciseHistoryData(UseCase.None()) {it.fold(::handleFailure,::handleTheExerciseHistoryData)}

    private fun handleExistPlayPopupData(playPopupData: List<PlayPopupData>){
        this.existPlayPopupList.value = playPopupData.map{
            PlayPopupView(it.id, it.part_img, it.name, it.mass, it.rep, it.setGoal, it.setDone, it.interval,
                if(it.achievement) 1 else 0)
        }
    }

    // PlayPopupData를 StatisticsEntity로 변환해서 저장하는 함수.
    fun saveStatisticsData(todaySetDone: Int, todaySetGoal: Int) {
        Dlog.d( "todaySetDone : $todaySetDone, todaySetGoal : $todaySetGoal")

        saveStatisticsData(SaveStatisticsData.Params(todaySetDone, todaySetGoal)) {
            it.fold(
                ::handleFailure,
                ::handleTheStatisticsData
            )
        }
    }

    private fun handlePlayPopupData(playPopupData: List<PlayPopupData>){
        this.playPopupList.value = playPopupData.map{
            PlayPopupView(it.id, it.part_img, it.name, it.mass, it.rep, it.setGoal, it.setDone, it.interval,
                if(it.achievement) 1 else 0)
        }
    }

    private fun handleTheExerciseHistoryData(playPopupData: PlayPopupData?){
        Dlog.d( "handleTheExerciseHistoryData : ${playPopupData?.id}")
        var tempPlayPopupData = playPopupData ?: PlayPopupData.empty()
        this.playPopupData.value = PlayPopupView(tempPlayPopupData.id, tempPlayPopupData.part_img, tempPlayPopupData.name,
            tempPlayPopupData.mass, tempPlayPopupData.rep, tempPlayPopupData.setGoal, tempPlayPopupData.setDone,
            tempPlayPopupData.interval, if(tempPlayPopupData.achievement) 1 else 0)
    }

    private fun handleTheStatisticsData(playPopupData: PlayPopupData?){
        Dlog.d( "handleTheStatisticsData")
        var tempPlayPopupData = playPopupData ?: PlayPopupData.empty()
        this.playPopupStatisticsData.value = PlayPopupView(tempPlayPopupData.id, tempPlayPopupData.part_img, tempPlayPopupData.name,
            tempPlayPopupData.mass, tempPlayPopupData.rep, tempPlayPopupData.setGoal, tempPlayPopupData.setDone,
            tempPlayPopupData.interval, if(tempPlayPopupData.achievement) 1 else 0)
    }

    // TODO: 재영이가 만들었으면 지우기
    // 키, 몸무게 regex
    // 출력이 true이면 통과(정상 범위)
    fun regexHeightAndWeight(height: Float, weight: Float) : Boolean{
        var result = true
        if(height < minHeight || height > maxHeight) result = false
        if(weight < minWeight || weight > maxWeight) result = false
        return result
    }

}
