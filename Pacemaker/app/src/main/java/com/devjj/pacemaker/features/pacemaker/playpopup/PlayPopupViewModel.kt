package com.devjj.pacemaker.features.pacemaker.playpopup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.devjj.pacemaker.features.pacemaker.usecases.GetPlayPopupData
import com.devjj.pacemaker.features.pacemaker.usecases.SaveExerciseHistoryData
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateExercisePlayPopupData
import javax.inject.Inject

class PlayPopupViewModel
@Inject constructor(private val getPlayPopupData: GetPlayPopupData,
                    private val updateExercisePlayPopupData: UpdateExercisePlayPopupData,
                    private val saveExerciseHistoryData: SaveExerciseHistoryData) : BaseViewModel() {

    var playPopupList: MutableLiveData<List<PlayPopupView>> = MutableLiveData()

    var playPopupData: MutableLiveData<PlayPopupView> = MutableLiveData()

    var currentSet: MutableLiveData<Int> = MutableLiveData()

    fun getCurrentSet(): LiveData<Int> {
        return currentSet
    }

    fun initCurrentSet(){
        this.currentSet.value = 1
    }

    fun setCurrentSet(currentSet: Int) {
        this.currentSet.value = currentSet
    }

    fun onePlusCurrentSet(){
        this.currentSet.value = currentSet.value?.plus(1)
    }


    fun loadPlayPopupList() = getPlayPopupData(UseCase.None()) {it.fold(::handleFailure, ::handlePlayPopupData)}

    fun updateExercisePlayPopupData(playPopupData: PlayPopupData) =
        updateExercisePlayPopupData(UpdateExercisePlayPopupData.Params(playPopupData))

    // AdditionData를 ExerciseEntity로 변환해서 저장하는 함수.
    fun saveExerciseHistoryData(playPopupData: PlayPopupData, saveDate: String, saveHeight: Int, saveWeight: Int) {
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

    // 현재 세트 로드하는 함수
    private fun loadCurrentSet(){
        // Do an asynchronous operation to fetch users.
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

}
