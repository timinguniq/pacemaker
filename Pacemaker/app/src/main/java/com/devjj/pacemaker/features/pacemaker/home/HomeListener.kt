package com.devjj.pacemaker.features.pacemaker.home

import android.app.Activity
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.databinding.ActivityPacemakerBinding
import com.devjj.pacemaker.databinding.FragmentHomeBinding
import com.devjj.pacemaker.features.pacemaker.addition.*
import com.devjj.pacemaker.features.pacemaker.dialog.showDeleteDialog

class HomeListener(val activity: Activity,
                   val binding: FragmentHomeBinding,
                   val activityBinding: ActivityPacemakerBinding,
                   val navigator: Navigator, val homeAdapter: HomeAdapter, val homeFragment: HomeFragment,
                   val homeViewModel: HomeViewModel, val setting: SettingSharedPreferences) {

    fun clickListener(){
        // 플로팅 버튼 클릭 이벤트
        binding.fHomeFloatingActionBtn.setOnClickListener {
            navigator.showAddition(activity, AdditionView.empty())
        }

        activityBinding.aPacemakerFloEdit.setOnClickListener {
            setting.isEditMode = !setting.isEditMode
            homeFragment.switchEditImage(setting.isEditMode)
            homeAdapter.notifyDataSetChanged()
        }

        homeAdapter.clickListener = {additionView  ->
            navigator.showAddition(activity, additionView)
        }

        homeAdapter.deleteClickListener = {homeView ->
            Dlog.d( "longClickListener")
            showDeleteDialog(activity, setting.isNightMode, homeViewModel, homeView)
        }

        // 분할 1번 이미지뷰 이벤트 메소드
        binding.fHomeIvDivision1.setOnClickListener {
            // 이미지 뷰 셋팅
            homeFragment.settingDivisionImageViews(DIVISION_1, setting.isNightMode)
            cur_division = DIVISION_1

            // DB에 있는 데이터 로드
            homeViewModel.loadHomeList()
        }

        // 분할 2번 이미지뷰 이벤트 메소드
        binding.fHomeIvDivision2.setOnClickListener {
            // 이미지 뷰 셋팅
            homeFragment.settingDivisionImageViews(DIVISION_2, setting.isNightMode)
            cur_division = DIVISION_2

            // DB에 있는 데이터 로드
            homeViewModel.loadHomeList()
        }

        // 분할 3번 이미지뷰 이벤트 메소드
        binding.fHomeIvDivision3.setOnClickListener {
            // 이미지 뷰 셋팅
            homeFragment.settingDivisionImageViews(DIVISION_3, setting.isNightMode)
            cur_division = DIVISION_3

            // DB에 있는 데이터 로드
            homeViewModel.loadHomeList()
        }

    }

}