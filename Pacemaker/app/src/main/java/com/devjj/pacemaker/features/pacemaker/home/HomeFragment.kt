package com.devjj.pacemaker.features.pacemaker.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.features.pacemaker.dialog.showProfileDialog
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.functional.OnStartDragListener
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateProfile
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnBackPressedListener, OnStartDragListener {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var setting: SettingSharedPreferences
    @Inject lateinit var updateProfile: UpdateProfile
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var homeListener: HomeListener

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var touchHelper: ItemTouchHelper

    override fun layoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        Dlog.d( "onCreate HomeFragment")

        homeViewModel = viewModel(viewModelFactory){
            observe(homeList, ::renderHomeList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onResume() {
        super.onResume()
        initializeHomeAdapter()
        initializeView()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

    // homeFragment 초기화 하는 함수
    private fun initializeView() {
        this.activity!!.aPacemaker_tv_title.text = this.getString(R.string.apacemaker_tv_title_str)
        this.activity!!.aPacemaker_flo_sort.visible()
        if(!setting.isNightMode){
            // 화이트모드
            fHome_floating_action_btn.setImageResource(R.drawable.fhome_img_fabtn_daytime)
            fHome_floating_action_btn.supportBackgroundTintList =
                ContextCompat.getColorStateList(activity!!, R.color.blue_5C83CF)
        }else{
            // 다크모드
            fHome_floating_action_btn.setImageResource(R.drawable.fhome_img_fabtn_nighttime)
            fHome_floating_action_btn.supportBackgroundTintList =
                ContextCompat.getColorStateList(activity!!, R.color.orange_F74938)
        }

        // homeListener 초기화
        homeListener = HomeListener(activity!!, context!!, navigator, homeAdapter, homeViewModel, setting)
        // 클릭 리스너
        homeListener.clickListener()

        if(setting.height < 0 && setting.weight < 0) {
             showProfileDialog(activity!!, setting, String.empty(), GET_HEIGHT_WEIGHT, updateProfile)
        }

        // DB에 있는 데이터 로드
        homeViewModel.loadHomeList()
    }

    // homeAdapter 초기화 하는 함수
    private fun initializeHomeAdapter() {
        homeAdapter = HomeAdapter(this, context!!, setting, homeViewModel)
        val callback: ItemTouchHelper.Callback = HomeItemMoveCallbackListener(homeAdapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(fHome_recyclerview)
        fHome_recyclerview.layoutManager = LinearLayoutManager(context)
        fHome_recyclerview.adapter = homeAdapter
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
            is HomeFailure.ListNotAvailable -> renderFailure(R.string.fhome_tv_list_unavailable_str)
            else -> Dlog.d( "error")
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        fHome_recyclerview.gone()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }

}