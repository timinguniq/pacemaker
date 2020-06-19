package com.devjj.pacemaker.features.pacemaker.tutorial

import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.features.pacemaker.addition.*
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupFragment
import com.github.islamkhsh.doOnPageSelected
import kotlinx.android.synthetic.main.fragment_addition.*
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialListener(val activity: Activity, val tutorialFragment: TutorialFragment,
                       val navigator: Navigator, val tutorialViewModel: TutorialViewModel) {
    fun clickListener() {
        // viewpager page listener
        activity.fTutorial_viewpager.doOnPageSelected {

            tutorialViewModel.currentItemData.value = activity.fTutorial_viewpager.currentItem
            Dlog.d("currentItem doOnPageSelected : ${activity.fTutorial_viewpager.currentItem}")
            if(activity.fTutorial_viewpager.currentItem == 12){
                Dlog.d("currentItem 12")
                tutorialFragment.setFinishBtnVisible(true)
            }else{
                tutorialFragment.setFinishBtnVisible(false)
            }
        }

        // finish btn을 클릭했을 때 이벤트 함수
        activity.fTutorial_iv_finish.setOnClickListener {
            navigator.showPacemaker(activity)
        }

    }
}