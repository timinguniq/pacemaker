package com.devjj.pacemaker.core.dialog

import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import kotlinx.android.synthetic.main.dialog_profile_input.*
import kotlinx.android.synthetic.main.dialog_profile_input.view.*

fun showProfileDialog(activity: Activity, setting: SettingSharedPreferences, date : String , flag : Int) {
    val builder = AlertDialog.Builder(activity)
    val dialogView = activity.layoutInflater.inflate(R.layout.dialog_profile_input, null)


    dialogView.dProfile_swc_mode_height.isChecked = setting.isUpdateHeight
    dialogView.dProfile_swc_mode_weight.isChecked = setting.isUpdateWeight

    dialogView.dProfile_swc_mode_weight.setOnCheckedChangeListener { buttonView, isChecked ->
        setting.isUpdateWeight = isChecked
    }

    dialogView.dProfile_swc_mode_height.setOnCheckedChangeListener { buttonView, isChecked ->
        setting.isUpdateHeight = isChecked
    }

    if (setting.isNightMode) {
        dialogView.dProfile_clo_main.background =
            ResourcesCompat.getDrawable( activity.resources, R.drawable.img_popup_background_nighttime, null )
        dialogView.dProfile_tv_height.setTextColor(activity.getColor(R.color.grey_bg_basic))
        dialogView.dProfile_ev_height.setTextColor(activity.getColor(R.color.blue_bg_thick))
        dialogView.dProfile_ev_height.setHintTextColor(activity.getColor(R.color.white_txt_light))

        dialogView.dProfile_tv_weight.setTextColor(activity.getColor(R.color.grey_bg_basic))
        dialogView.dProfile_ev_weight.setTextColor(activity.getColor(R.color.blue_bg_thick))
        dialogView.dProfile_ev_weight.setHintTextColor(activity.getColor(R.color.white_txt_light))

        dialogView.dProfile_tv_confirm.setTextColor(activity.getColor(R.color.orange_bg_thick))

    } else {
        dialogView.dProfile_clo_main.background =
            ResourcesCompat.getDrawable( activity.resources, R.drawable.img_popup_background_daytime, null )
        dialogView.dProfile_tv_height.setTextColor(activity.getColor(R.color.grey_bg_thickest))
        dialogView.dProfile_ev_height.setTextColor(activity.getColor(R.color.grey_bg_thickest))
        dialogView.dProfile_ev_height.setHintTextColor(activity.getColor(R.color.grey_bg_lightest))

        dialogView.dProfile_tv_weight.setTextColor(activity.getColor(R.color.grey_bg_thickest))
        dialogView.dProfile_ev_weight.setTextColor(activity.getColor(R.color.grey_bg_thickest))
        dialogView.dProfile_ev_weight.setHintTextColor(activity.getColor(R.color.grey_bg_lightest))

        dialogView.dProfile_tv_confirm.setTextColor(activity.getColor(R.color.blue_bg_thick))

    }



    if(setting.weight < 0 && setting.height < 0 ){
            dialogView.dProfile_clo_option_height.visible()
            dialogView.dProfile_clo_option_weight.visible()
            dialogView.dProfile_tv_confirm.text = activity.getString(R.string.dprofile_tv_first_setting_str)
    }else {
        if(!setting.isUpdateWeight){
            dialogView.dProfile_clo_weight.gone()
        }

        if(!setting.isUpdateHeight){
            dialogView.dProfile_clo_height.gone()
        }
    }





    val dialog = builder.setView(dialogView).show()


    var fSaveHeight: Float
    var fSaveWeight: Float

    dialogView.dProfile_tv_confirm.setOnClickListener {
        val sSaveHeight = dialogView.dProfile_ev_height.text.toString()
        val sSaveWeight = dialogView.dProfile_ev_weight.text.toString()

        // 키와 몸무게 입력 칸을 빈칸으로 넘겼을 시 SharedPreferences에 저장된 값 가져오기!
        fSaveHeight = if (sSaveHeight == String.empty()) setting.height else sSaveHeight.toFloat()
        fSaveWeight = if (sSaveWeight == String.empty()) setting.weight else sSaveWeight.toFloat()
        //


        fSaveHeight = fSaveHeight.round(1)
        fSaveWeight = fSaveWeight.round(1)


        // 소수점 한자리 반올림하는 코드
        //fSaveHeight = (fSaveHeight * 10).roundToInt() / 10f
        //fSaveWeight = (fSaveWeight * 10).roundToInt() / 10f
        //

        setting.height = fSaveHeight
        setting.weight = fSaveWeight

        dialog.dismiss()
    }

    dialog.setOnDismissListener {
        if(setting.height < 0 ) setting.height = 0f
        if(setting.weight < 0 ) setting.weight = 0f
    }
}


