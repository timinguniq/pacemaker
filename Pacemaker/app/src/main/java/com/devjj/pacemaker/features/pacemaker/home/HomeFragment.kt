package com.devjj.pacemaker.features.pacemaker.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.failure
import com.devjj.pacemaker.core.extension.invisible
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var homeAdapter: HomeAdapter

    private lateinit var homeViewModel: HomeViewModel

    override fun layoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        homeViewModel = viewModel(viewModelFactory){
            observe(homeList, ::renderHomeList)
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
        homeAdapter.clickListener = { additionView ->
            navigator.showAddition(activity!!, additionView)}

        // DB에 있는 데이터 로드
        homeViewModel.loadHomeList()
    }

    // Home 데이터들 갱신하는 함수.
    private fun renderHomeList(homeView: List<HomeView>?) {
        homeAdapter.collection = homeView.orEmpty()
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


    private fun renderFailure(@StringRes message: Int) {
        fHome_recyclerview.invisible()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }


}