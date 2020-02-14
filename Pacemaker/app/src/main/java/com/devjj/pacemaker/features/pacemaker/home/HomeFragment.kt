package com.devjj.pacemaker.features.pacemaker.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.PlayViewSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.google.android.gms.dynamic.SupportFragmentWrapper
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject
import javax.inject.Singleton

class HomeFragment : BaseFragment(), OnBackPressedListener{

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var homeAdapter: HomeAdapter
    @Inject lateinit var playViewSharedPreferences: PlayViewSharedPreferences

    lateinit var homeViewModel: HomeViewModel

    override fun layoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        Log.d("test", "onCreate HomeFragment")

        homeViewModel = viewModel(viewModelFactory){
            observe(homeList, ::renderHomeList)
            observe(playViewIsClicked, ::renderPlayView)
            failure(failure, ::handleFailure)
        }
    }

    // 한번만 소환되는거 같다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    // homeFragment 초기화 하는 함수
    private fun initializeView() {
        // 플로팅 버튼 클릭 이벤트
        fHome_floating_action_btn?.setOnClickListener {
            navigator.showAddition(context!!, AdditionView.empty())
        }

        fHome_recyclerview.layoutManager = LinearLayoutManager(this.context)
        fHome_recyclerview.adapter = homeAdapter
        homeAdapter.clickListener = {additionView  ->
            navigator.showAddition(activity!!, additionView)}

        // DB에 있는 데이터 로드
        homeViewModel.loadHomeList()

        // SharedPreferences에 있는 playView 변수 로드
        homeViewModel.getPlayViewIsClicked()
    }

    // Home 데이터들 갱신하는 함수.
    private fun renderHomeList(homeView: List<HomeView>?) {
        homeAdapter.collection = homeView.orEmpty()
    }

    // playViewIsClicked
    private fun renderPlayView(playView: Boolean?){
        Log.d("test","renderPlayView : $playView")
        val isClicked = playView?:false
        if(isClicked){
            fHome_clo_play.visible()
            fHome_floating_action_btn.invisible()
        }else{
            fHome_clo_play.invisible()
            fHome_floating_action_btn.visible()
        }
    }


    // Home 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is HomeFailure.ListNotAvailable -> renderFailure(R.string.fHome_list_unavailable)
            else -> Log.d("homeFragment", "error")
        }
    }

    // 테스트 코드
    override fun onBackPressed() {
        if(fHome_clo_play.isVisible){
            fHome_clo_play.invisible()
            fHome_floating_action_btn.visible()
            homeViewModel.setPlayViewIsClicked(false)
        }else{
            super.onBackPressed()
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        fHome_recyclerview.invisible()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }


}