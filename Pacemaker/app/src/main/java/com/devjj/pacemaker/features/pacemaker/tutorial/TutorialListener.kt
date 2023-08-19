package com.devjj.pacemaker.features.pacemaker.tutorial

import android.app.Activity
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.databinding.FragmentTutorialBinding
import com.github.islamkhsh.doOnPageSelected

class TutorialListener(val activity: Activity, val tutorialFragment: TutorialFragment,
                       val binding: FragmentTutorialBinding, val navigator: Navigator, val setting: SettingSharedPreferences, val tutorialViewModel: TutorialViewModel) {
    fun clickListener() {
        // viewpager page listener
        binding.fTutorialViewpager.doOnPageSelected {
            tutorialViewModel.currentItemData.value = binding.fTutorialViewpager.currentItem
            Dlog.d("currentItem doOnPageSelected : ${binding.fTutorialViewpager.currentItem}")
            if(binding.fTutorialViewpager.currentItem == 12){
                Dlog.d("currentItem 12")
                tutorialFragment.setFinishBtnVisible(true)
            }else{
                if(setting.height == -1.0f && setting.weight == -1.0f)
                    tutorialFragment.setFinishBtnVisible(false)
            }
        }

        // finish btn을 클릭했을 때 이벤트 함수
        binding.fTutorialIvFinish.setOnClickListener {
            if(setting.height == -1.0f && setting.weight == -1.0f) navigator.showPacemaker(activity)
            else activity.onBackPressed()
        }

    }
}