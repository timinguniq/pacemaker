package com.devjj.pacemaker.features.pacemaker.home

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.visible
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.features.pacemaker.addition.*
import com.devjj.pacemaker.features.pacemaker.dialog.showDeleteDialog
import kotlinx.android.synthetic.main.activity_pacemaker.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import javax.inject.Inject

class HomeListener(val activity: Activity, val context: Context,
                   val navigator: Navigator, val homeAdapter: HomeAdapter, val homeViewModel: HomeViewModel,
                   val setting: SettingSharedPreferences) {

    fun clickListener(){
        // 플로팅 버튼 클릭 이벤트
        activity.fHome_floating_action_btn.setOnClickListener {
            navigator.showAddition(activity, AdditionView.empty())
        }

        activity.aPacemaker_flo_sort.setOnClickListener {
            setting.isSortMode = !setting.isSortMode
            homeAdapter.notifyDataSetChanged()
        }

        homeAdapter.clickListener = {additionView  ->
            navigator.showAddition(activity, additionView)
        }

        homeAdapter.longClickListener = {homeView ->
            Dlog.d( "longClickListener")
            showDeleteDialog(activity, setting.isNightMode, homeViewModel, homeView)
        }
    }

}