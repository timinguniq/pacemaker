package com.devjj.pacemaker.features.pacemaker.dialog

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupViewModel
import kotlinx.android.synthetic.main.dialog_give_up_exercise.view.*


// 운동 전체 포기 다이얼 로그를 보여주는 함수.
fun showGiveUpAllExerciseDialog(activity: Activity, isDarkMode: Boolean, playPopupViewModel: PlayPopupViewModel, playPopupDataList: List<PlayPopupData>){
    val builder = AlertDialog.Builder(activity)
    val dialogView = activity.layoutInflater.inflate(R.layout.dialog_give_up_exercise, null)

    dialogView.dGiveUp_tv_main.text = activity.getString(R.string.dgiveupall_tv_main_str)
    if(!isDarkMode){
        // 화이트모드
        dialogView.dGiveUp_clo_main.background =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_daytime, null)
        dialogView.dGiveUp_tv_main.setTextColor(loadColor(activity, R.color.grey_444646))
        dialogView.dGiveUp_tv_confirm.setTextColor(loadColor(activity, R.color.blue_5F87D6))
        dialogView.dGiveUp_tv_cancel.setTextColor(loadColor(activity, R.color.blue_5F87D6))
    }else{
        // 다크모드
        dialogView.dGiveUp_clo_main.background =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_nighttime, null)
        dialogView.dGiveUp_tv_main.setTextColor(loadColor(activity, R.color.grey_F9F9F9))
        dialogView.dGiveUp_tv_confirm.setTextColor(loadColor(activity, R.color.orange_F74938))
        dialogView.dGiveUp_tv_cancel.setTextColor(loadColor(activity, R.color.orange_F74938))
    }

    val dialog = builder.setView(dialogView).show()

    dialogView.dGiveUp_tv_confirm.setOnClickListener {
        Log.d("test", "showGiveUpAllExerciseDialog confirm")

        for(playPopupData in playPopupDataList){
            Log.d("test", "playPopupData.achievement ${playPopupData.achievement}")
            Log.d("test", "playPopupData.setDone : ${playPopupData.setDone}")
            if(!playPopupData.achievement) {
                playPopupData.achievement = true
                playPopupData.setDone--
                if (playPopupData.setDone <= 0)
                    playPopupData.setDone = 0
                playPopupViewModel.updateExercisePlayPopupData(playPopupData)
            }
        }

        playPopupViewModel.loadPlayPopupList()
        dialog.dismiss()
    }

    dialogView.dGiveUp_tv_cancel.setOnClickListener {
        Log.d("test", "showGiveUpAllExerciseDialog cancel")
        dialog.dismiss()
    }
}