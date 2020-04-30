package com.devjj.pacemaker.features.pacemaker.home

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.devjj.pacemaker.features.pacemaker.dialog.showDeleteDialog
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeListener(val activity: Activity, val context: Context,
                   val navigator: Navigator, val homeAdapter: HomeAdapter, val homeViewModel: HomeViewModel,
                   val isNightMode: Boolean) {

    fun clickListener(){
        // 플로팅 버튼 클릭 이벤트
        activity.fHome_floating_action_btn.setOnClickListener {
            navigator.showAddition(activity, AdditionView.empty())
        }

        activity.fHome_recyclerview.layoutManager = LinearLayoutManager(this.context)
        activity.fHome_recyclerview.adapter = homeAdapter
        homeAdapter.clickListener = {additionView  ->
            navigator.showAddition(activity, additionView)}

        homeAdapter.longClickListener = {homeView ->
            Log.d("test", "longClickListener")
            showDeleteDialog(activity, isNightMode, homeViewModel, homeView)
        }

        // DB에 있는 데이터 로드
        homeViewModel.loadHomeList()
    }


}