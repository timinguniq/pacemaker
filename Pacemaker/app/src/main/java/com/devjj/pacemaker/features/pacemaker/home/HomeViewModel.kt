package com.devjj.pacemaker.features.pacemaker.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetExerciseData
import javax.inject.Inject

class HomeViewModel
@Inject constructor(private val getExerciseData: GetExerciseData) : BaseViewModel() {

    var exerciseList: MutableLiveData<List<HomeView>> = MutableLiveData()

    fun loadExerciseList() = getExerciseData(UseCase.None(), ::handleExerciseData)

    private fun handleExerciseData(exerciseData: List<ExerciseData>){
        this.exerciseList.value = exerciseData.map{ HomeView(it.id, it.part_img, it.name, it.mass, it.set, it.interval) }
    }

}
