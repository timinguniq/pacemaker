package com.devjj.pacemaker.features.pacemaker.playpopup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.devjj.pacemaker.features.pacemaker.usecases.DeleteExerciseHistoryData
import com.devjj.pacemaker.features.pacemaker.usecases.GetPlayPopupData
import com.devjj.pacemaker.features.pacemaker.usecases.SaveExerciseHistoryData
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateExercisePlayPopupData
import javax.inject.Inject

class PlayPopupViewModel
@Inject constructor(private val getPlayPopupData: GetPlayPopupData,
                    private val updateExercisePlayPopupData: UpdateExercisePlayPopupData,
                    private val saveExerciseHistoryData: SaveExerciseHistoryData,
                    private val deleteExerciseHistoryData: DeleteExerciseHistoryData) : BaseViewModel() {

    var totalTime : MutableLiveData<Int> = MutableLiveData()

    var playPopupList: MutableLiveData<List<PlayPopupView>> = MutableLiveData()

    var existPlayPopupList: MutableLiveData<List<PlayPopupView>> = MutableLiveData()

    var playPopupData: MutableLiveData<PlayPopupView> = MutableLiveData()

    fun loadPlayPopupList() = getPlayPopupData(UseCase.None()) {it.fold(::handleFailure, ::handlePlayPopupData)}

    fun existPlayPopupList() = getPlayPopupData(UseCase.None()) {it.fold(::handleFailure, ::handleExistPlayPopupData)}

    fun updateExercisePlayPopupData(playPopupData: PlayPopupData) =
        updateExercisePlayPopupData(UpdateExercisePlayPopupData.Params(playPopupData))

    // AdditionData를 ExerciseEntity로 변환해서 저장하는 함수.
    fun saveExerciseHistoryData(playPopupData: PlayPopupData, saveDate: String, saveHeight: Float, saveWeight: Float) {
        date = saveDate
        height = saveHeight
        weight = saveWeight
        Log.d("test", "date : ${date}, height : ${height}, weight : ${weight}")

        Log.d("test", "achievement rate : ${100 * playPopupData.setDone / playPopupData.setGoal}")
        Log.d("savingtest","date : ${date}, name : ${playPopupData.name}, height : ${height}, weight : ${weight}")
        saveExerciseHistoryData(SaveExerciseHistoryData.Params(playPopupData)) {
            it.fold(
                ::handleFailure,
                ::handleTheExerciseHistoryData
            )
        }
    }

    fun deleteExerciseHistoryData(saveDate: String){
        date = saveDate
        deleteExerciseHistoryData(UseCase.None()) {it.fold(::handleFailure,::handleTheExerciseHistoryData)}
    }

    private fun handleExistPlayPopupData(playPopupData: List<PlayPopupData>){
        this.existPlayPopupList.value = playPopupData.map{
            PlayPopupView(it.id, it.part_img, it.name, it.mass, it.rep, it.setGoal, it.setDone, it.interval,
                if(it.achievement) 1 else 0)
        }
    }

    private fun handlePlayPopupData(playPopupData: List<PlayPopupData>){
        this.playPopupList.value = playPopupData.map{
            PlayPopupView(it.id, it.part_img, it.name, it.mass, it.rep, it.setGoal, it.setDone, it.interval,
                if(it.achievement) 1 else 0)
        }
    }

    private fun handleTheExerciseHistoryData(playPopupData: PlayPopupData?){
        val tempPlayPopupData = playPopupData?:PlayPopupData.empty()
        this.playPopupData.value = PlayPopupView(tempPlayPopupData.id, tempPlayPopupData.part_img, tempPlayPopupData.name,
            tempPlayPopupData.mass, tempPlayPopupData.rep, tempPlayPopupData.setGoal, tempPlayPopupData.setDone,
            tempPlayPopupData.interval, if(tempPlayPopupData.achievement) 1 else 0)
    }

    // 키, 몸무게 regex
    // 출력이 true이면 통과(정상 범위)
    fun regexHeightAndWeight(height: Float, weight: Float) : Boolean{
        var result = true
        if(height < minHeight || height > maxHeight) result = false
        if(weight < minWeight || weight > maxWeight) result = false
        return result
    }

}
