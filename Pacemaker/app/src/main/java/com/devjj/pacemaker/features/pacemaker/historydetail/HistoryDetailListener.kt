package com.devjj.pacemaker.features.pacemaker.historydetail

import android.app.Activity
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.view.get
import androidx.core.view.iterator
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.gone
import com.devjj.pacemaker.core.extension.isVisible
import com.devjj.pacemaker.core.extension.visible
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.recyclerview_exercise_detail_item.*
import kotlinx.android.synthetic.main.recyclerview_exercise_detail_item.view.*

class HistoryDetailListener (val activity : Activity,private val historyDetailAdapter: HistoryDetailAdapter){
    fun initListener(){
        activity.fHistoryDetail_iv_drop.setOnClickListener {
            when (activity.fHistoryDetail_iv_drop.tag) {
                R.drawable.fhistorydetail_img_btn_dropdown_daytime -> {
                    activity.fHistoryDetail_iv_drop.setImageDrawable(activity.getDrawable(R.drawable.fhistorydetail_img_btn_dropup_daytime))
                    activity.fHistoryDetail_iv_drop.tag = R.drawable.fhistorydetail_img_btn_dropup_daytime

                    for (item in activity.fHistoryDetail_recyclerview) {
                        item.rvExerciseItem_clo_detail.visible()
                    }
                }
                R.drawable.fhistorydetail_img_btn_dropup_daytime -> {
                    activity.fHistoryDetail_iv_drop.setImageDrawable(activity.getDrawable(R.drawable.fhistorydetail_img_btn_dropdown_daytime))
                    activity.fHistoryDetail_iv_drop.tag = R.drawable.fhistorydetail_img_btn_dropdown_daytime

                    for (item in activity.fHistoryDetail_recyclerview) {
                        item.rvExerciseItem_clo_detail.gone()
                    }
                }
            }
        }

        historyDetailAdapter.clickListener = { view, id, date ->

            var transition : Transition = Slide(Gravity.BOTTOM)
            transition.duration = 500
            transition.addTarget(view.rvExerciseItem_clo_detail)
            TransitionManager.beginDelayedTransition(view.rvExerciseItem_clo_main,transition)

            when(view.rvExerciseItem_clo_detail.isVisible()){
                true->view.rvExerciseItem_clo_detail.visibility = View.GONE
                false->view.rvExerciseItem_clo_detail.visibility = View.VISIBLE
            }
        }
    }
}