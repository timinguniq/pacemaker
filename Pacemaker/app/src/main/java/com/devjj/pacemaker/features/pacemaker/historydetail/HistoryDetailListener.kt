package com.devjj.pacemaker.features.pacemaker.historydetail

import android.app.Activity
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.iterator
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.gone
import com.devjj.pacemaker.core.extension.isVisible
import com.devjj.pacemaker.core.extension.visible
import com.devjj.pacemaker.databinding.FragmentHistoryDetailBinding

class HistoryDetailListener (val activity : Activity, val binding: FragmentHistoryDetailBinding,
                             private val historyDetailAdapter: HistoryDetailAdapter){
    fun initListener(){
        binding.fHistoryDetailCloOpenAll.setOnClickListener {
            when (binding.fHistoryDetailIvDrop.tag) {
                R.drawable.fhistorydetail_img_btn_dropdown_daytime -> {
                    binding.fHistoryDetailIvDrop.setImageDrawable(activity.getDrawable(R.drawable.fhistorydetail_img_btn_dropup_daytime))
                    binding.fHistoryDetailIvDrop.tag = R.drawable.fhistorydetail_img_btn_dropup_daytime

                    for (item in binding.fHistoryDetailRecyclerview) {
                        item.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_detail).visible()
                        // 아래 한줄이 원래 코드
                        //item.rvExerciseItem_clo_detail.visible()
                    }
                }
                R.drawable.fhistorydetail_img_btn_dropup_daytime -> {
                    binding.fHistoryDetailIvDrop.setImageDrawable(activity.getDrawable(R.drawable.fhistorydetail_img_btn_dropdown_daytime))
                    binding.fHistoryDetailIvDrop.tag = R.drawable.fhistorydetail_img_btn_dropdown_daytime

                    for (item in binding.fHistoryDetailRecyclerview) {
                        item.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_detail).gone()

                        // 원본 코드
                        //item.rvExerciseItem_clo_detail.gone()
                    }
                }
            }
        }

        historyDetailAdapter.clickListener = { view, id, date ->
            var transition : Transition = Slide(Gravity.BOTTOM)
            transition.duration = 500
            transition.addTarget(
                view.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_detail))
            TransitionManager.beginDelayedTransition(
                view.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_main),transition)

            when(view.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_detail).isVisible()){
                true->view.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_detail).visibility = View.GONE
                false->view.findViewById<ConstraintLayout>(R.id.rvExerciseItem_clo_detail).visibility = View.VISIBLE
            }
        }
    }
}