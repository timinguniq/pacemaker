package com.devjj.pacemaker.features.pacemaker.dialog

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeView
import com.devjj.pacemaker.features.pacemaker.home.HomeViewModel
import kotlinx.android.synthetic.main.dialog_remove.view.*

// 삭제 다이얼 로그를 보여주는 함수.
fun showDeleteDialog(activity: Activity, isNightMode: Boolean, homeViewModel: HomeViewModel, homeView: HomeView){
    val builder = AlertDialog.Builder(activity)
    val dialogView = activity.layoutInflater.inflate(R.layout.dialog_remove, null)

    if(!isNightMode){
        // 화이트모드
        dialogView.dRemove_clo_main.background =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_daytime, null)
        dialogView.dRemove_tv_main.setTextColor(loadColor(activity, R.color.grey_444646))
        dialogView.dRemove_tv_main.setTextColor(loadColor(activity, R.color.grey_444646))
        dialogView.dRemove_tv_confirm.setTextColor(loadColor(activity, R.color.blue_5F87D6))
        dialogView.dRemove_tv_cancel.setTextColor(loadColor(activity, R.color.blue_5F87D6))
    }else{
        // 다크모드
        dialogView.dRemove_clo_main.background =
            ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_nighttime, null)
        dialogView.dRemove_tv_main.setTextColor(loadColor(activity, R.color.grey_F9F9F9))
        dialogView.dRemove_tv_confirm.setTextColor(loadColor(activity, R.color.orange_F74938))
        dialogView.dRemove_tv_cancel.setTextColor(loadColor(activity, R.color.orange_F74938))
    }

    val dialog = builder.setView(dialogView).show()

    dialogView.dRemove_tv_confirm.setOnClickListener {
        Log.d("test", "showDeleteDialog confirm")
        val homeData =
            HomeData(
                homeView.id, homeView.part_img, homeView.name, homeView.mass,
                homeView.rep, homeView.set, homeView.interval
            )
        // ExerciseData 삭제하는 코드
        homeViewModel.deleteExerciseData(homeData)
        // homeData 갱신하는 코드
        homeViewModel.loadHomeList()
        // dialog 없애는 코드
        dialog.dismiss()
    }

    dialogView.dRemove_tv_cancel.setOnClickListener {
        Log.d("test", "showDeletedialog cancel")
        dialog.dismiss()
    }
}