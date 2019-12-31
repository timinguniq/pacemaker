package com.devjj.pacemaker.features.pacemaker.play

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetPlayPopupData
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateExercisePlayPopupData
import javax.inject.Inject

class PlayPopupViewModel
@Inject constructor(private val getPlayPopupData: GetPlayPopupData,
                    private val updateExercisePlayPopupData: UpdateExercisePlayPopupData) : BaseViewModel() {

    var playPopupList: MutableLiveData<List<PlayPopupView>> = MutableLiveData()

    fun loadPlayPopupList() = getPlayPopupData(UseCase.None()) {it.fold(::handleFailure, ::handlePlayPopupData)}

    fun updateExercisePlayPopupData(playPopupData: PlayPopupData) =
        updateExercisePlayPopupData(UpdateExercisePlayPopupData.Params(playPopupData))

    private fun handlePlayPopupData(playPopupData: List<PlayPopupData>){
        this.playPopupList.value = playPopupData.map{
            val iAchivement = if(it.achivement) 1 else 0
            PlayPopupView(it.id, it.part_img, it.name, it.mass, it.rep, it.set, it.interval, iAchivement)
        }
    }

}
