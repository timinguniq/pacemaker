package com.devjj.pacemaker.features.pacemaker.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnBackPressedListener{

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var homeAdapter: HomeAdapter
    @Inject lateinit var setting: SettingSharedPreferences

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var homeListener: HomeListener

    override fun layoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        Log.d("test", "onCreate HomeFragment")

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
        this.activity!!.aPacemaker_tv_title.text="내 트레이닝"
        if(!setting.isDarkMode){
            // 화이트모드
            fHome_floating_action_btn.setImageResource(R.drawable.fhome_wm_fabtn_img)
            fHome_floating_action_btn.supportBackgroundTintList =
                ContextCompat.getColorStateList(activity!!, R.color.fhome_wm_fabtn_color)
        }else{
            // 다크모드
            fHome_floating_action_btn.setImageResource(R.drawable.fhome_dm_fabtn_img)
            fHome_floating_action_btn.supportBackgroundTintList =
                ContextCompat.getColorStateList(activity!!, R.color.fhome_dm_fabtn_color)
        }

        // homeListener 초기화
        homeListener = HomeListener(activity!!, context!!, navigator, homeAdapter, homeViewModel, setting.isDarkMode)
        // 클릭 리스너
        homeListener.clickListener()
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