package com.devjj.pacemaker.features.pacemaker.addition

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetTheAdditionData
import com.devjj.pacemaker.features.pacemaker.usecases.SaveExerciseData
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateExerciseData
import javax.inject.Inject

class AdditionViewModel
@Inject constructor(private val getTheAdditionData: GetTheAdditionData,
                    private val saveExerciseData: SaveExerciseData,
                    private val updateExerciseData: UpdateExerciseData) : BaseViewModel() {

    // AdditionFragment에서 표현할 View의 데이터 변수
    var additionData: MutableLiveData<AdditionView> = MutableLiveData()

    // AdditionData를 불러오는 함수.
    fun loadTheAdditionData(id: Int) =
        getTheAdditionData(GetTheAdditionData.Params(id)) {it.fold(::handleFailure, ::handleTheExerciseData)}

    // AdditionData를 ExerciseEntity로 변환해서 저장하는 함수.
    fun saveExerciseData(additionData: AdditionData) =
        saveExerciseData(SaveExerciseData.Params(additionData)) {it.fold(::handleFailure, ::handleTheExerciseData)}

    // AdditionData를 이용하여 ExerciseEntity를 수정하는 함수
    fun updateExerciseData(additionData: AdditionData) =
        updateExerciseData(UpdateExerciseData.Params(additionData)) {it.fold(::handleFailure, ::handleTheExerciseData)}

    private fun handleTheExerciseData(additionData: AdditionData?){
        val tempAdditionData = additionData?:AdditionData.empty()
        this.additionData.value = AdditionView(tempAdditionData.id, tempAdditionData.part_img, tempAdditionData.name,
                tempAdditionData.mass, tempAdditionData.rep, tempAdditionData.setGoal, tempAdditionData.interval)
    }
}
