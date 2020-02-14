package com.devjj.pacemaker.features.pacemaker.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetHomeData
import com.devjj.pacemaker.features.pacemaker.usecases.GetPlayViewIsClicked
import com.devjj.pacemaker.features.pacemaker.usecases.SaveExerciseData
import com.devjj.pacemaker.features.pacemaker.usecases.SetPlayViewIsClicked
import javax.inject.Inject

class HomeViewModel
@Inject constructor(private val getHomeData: GetHomeData,
                    private val getPlayView: GetPlayViewIsClicked,
                    private val setPlayView: SetPlayViewIsClicked) : BaseViewModel() {

    var homeList: MutableLiveData<List<HomeView>> = MutableLiveData()

    var playViewIsClicked: MutableLiveData<Boolean> = MutableLiveData()

    fun loadHomeList() = getHomeData(UseCase.None()) {it.fold(::handleFailure, ::handleHomeData)}

    fun getPlayViewIsClicked() = getPlayView(UseCase.None()) {it.fold(::handleFailure, ::handlePlayView)}

    fun setPlayViewIsClicked(isClicked: Boolean) = setPlayView(SetPlayViewIsClicked.Params(isClicked))

    private fun handleHomeData(homeData: List<HomeData>){
        this.homeList.value = homeData.map{ HomeView(it.id, it.part_img, it.name, it.mass, it.rep, it.set, it.interval) }
    }

    private fun handlePlayView(playView: Boolean){
        this.playViewIsClicked.value = playView
    }
}
