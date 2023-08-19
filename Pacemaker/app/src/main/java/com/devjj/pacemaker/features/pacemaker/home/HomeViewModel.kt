package com.devjj.pacemaker.features.pacemaker.home

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.DeleteExerciseData
import com.devjj.pacemaker.features.pacemaker.usecases.GetHomeData
import com.devjj.pacemaker.features.pacemaker.usecases.SwapExerciseData
import javax.inject.Inject

class HomeViewModel
@Inject constructor(private val getHomeData: GetHomeData,
                    private val deleteExerciseData: DeleteExerciseData,
                    private val swapExerciseData: SwapExerciseData) : BaseViewModel() {
    var homeList: MutableLiveData<List<HomeView>> = MutableLiveData()

    // deleteExerciseData를 위한 임시 변수
    var homeData: MutableLiveData<HomeView> = MutableLiveData()

    fun loadHomeList() = getHomeData(UseCase.None()) {it.fold(::handleFailure, ::handleHomeData)}

    // HomeData를 ExerciseEntity로 변환해서 삭제하는 함수.
    fun deleteExerciseData(homeData: HomeData) =
        deleteExerciseData(DeleteExerciseData.Params(homeData)) {it.fold(::handleFailure, ::handleTheExerciseData)}

    // HomeData1, HomeData2를 교환하는 함수.
    fun swapExerciseData(homeData1Id: Int, homeData2Id: Int) =
        swapExerciseData(SwapExerciseData.Params(homeData1Id, homeData2Id)) {it.fold(::handleFailure, ::handleTheExerciseData)}

    private fun handleHomeData(homeData: List<HomeData>){
        this.homeList.value = homeData.map{ HomeView(it.id, it.part_img, it.name, it.mass, it.rep, it.setGoal, it.interval) }
    }

    private fun handleTheExerciseData(homeData: HomeData?){
        val tempHomeData = homeData?:HomeData.empty()
        this.homeData.value = HomeView(tempHomeData.id, tempHomeData.part_img, tempHomeData.name,
            tempHomeData.mass, tempHomeData.rep, tempHomeData.setGoal, tempHomeData.interval)
    }

}
