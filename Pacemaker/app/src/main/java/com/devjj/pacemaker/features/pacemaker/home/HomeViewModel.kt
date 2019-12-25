package com.devjj.pacemaker.features.pacemaker.home

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.interactor.UseCase
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetHomeData
import javax.inject.Inject

class HomeViewModel
@Inject constructor(private val gethomeData: GetHomeData) : BaseViewModel() {

    var homeList: MutableLiveData<List<HomeView>> = MutableLiveData()

    fun loadHomeList() = gethomeData(UseCase.None()) {it.fold(::handleFailure, ::handleHomeData)}

    private fun handleHomeData(homeData: List<HomeData>){
        this.homeList.value = homeData.map{ HomeView(it.id, it.part_img, it.name, it.mass, it.rep, it.set, it.interval) }
    }

}
