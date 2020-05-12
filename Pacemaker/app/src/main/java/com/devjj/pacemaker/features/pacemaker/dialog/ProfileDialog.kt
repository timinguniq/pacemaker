
package com.devjj.pacemaker.features.pacemaker.dialog
import android.app.Activity
import android.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateProfile
import kotlinx.android.synthetic.main.dialog_profile_input.view.*
import javax.inject.Inject

// date 형식은 'yyyy-MM-dd'
fun showProfileDialog(activity: Activity, setting: SettingSharedPreferences, date : String , flag : Int , updateProfile: UpdateProfile) {

    val builder = AlertDialog.Builder(activity)
    val dialogView = activity.layoutInflater.inflate(R.layout.dialog_profile_input, null)

    dialogView.dProfile_swc_mode_height.isChecked = setting.isUpdateHeight
    dialogView.dProfile_swc_mode_weight.isChecked = setting.isUpdateWeight

    dialogView.dProfile_swc_mode_weight.setOnCheckedChangeListener { _, isChecked ->
        setting.isUpdateWeight = isChecked
    }

    dialogView.dProfile_swc_mode_height.setOnCheckedChangeListener { _, isChecked ->
        setting.isUpdateHeight = isChecked
    }

    if (setting.isNightMode) {
        dialogView.dProfile_clo_main.background =
            ResourcesCompat.getDrawable( activity.resources, R.drawable.img_popup_background_nighttime, null )
        dialogView.dProfile_tv_height.setTextColor(loadColor(activity,R.color.grey_F9F9F9))
        dialogView.dProfile_ev_height.setTextColor(loadColor(activity,R.color.blue_5F87D6))
        dialogView.dProfile_ev_height.setHintTextColor(loadColor(activity,R.color.white_F7FAFD_47))

        dialogView.dProfile_tv_weight.setTextColor(loadColor(activity,R.color.grey_F9F9F9))
        dialogView.dProfile_ev_weight.setTextColor(loadColor(activity,R.color.blue_5F87D6))
        dialogView.dProfile_ev_weight.setHintTextColor(loadColor(activity,R.color.white_F7FAFD_47))

        dialogView.dProfile_tv_confirm.setTextColor(loadColor(activity,R.color.orange_F74938))

    } else {
        dialogView.dProfile_clo_main.background =
            ResourcesCompat.getDrawable( activity.resources, R.drawable.img_popup_background_daytime, null )
        dialogView.dProfile_tv_height.setTextColor(loadColor(activity,R.color.grey_444646))
        dialogView.dProfile_ev_height.setTextColor(loadColor(activity,R.color.grey_444646))
        dialogView.dProfile_ev_height.setHintTextColor(loadColor(activity,R.color.grey_444646_47))

        dialogView.dProfile_tv_weight.setTextColor(loadColor(activity,R.color.grey_444646))
        dialogView.dProfile_ev_weight.setTextColor(loadColor(activity,R.color.grey_444646))
        dialogView.dProfile_ev_weight.setHintTextColor(loadColor(activity,R.color.grey_444646_47))

        dialogView.dProfile_tv_confirm.setTextColor(loadColor(activity,R.color.blue_5F87D6))
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

    when(flag){
        GET_HEIGHT_WEIGHT->{
            dialogView.dProfile_clo_weight.visible()
            dialogView.dProfile_clo_height.visible()
        }
        GET_HEIGHT_ONLY->{
            dialogView.dProfile_clo_weight.gone()
        }
        GET_WEIGHT_ONLY->{
            dialogView.dProfile_clo_height.gone()
        }
    }
    val dialog = builder.setView(dialogView).show()

    // dialog 외부(밖에) 클릭시 꺼지는거 막는 코드
    dialog.setCancelable(false)

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

        setting.height = fSaveHeight
        setting.weight = fSaveWeight

        dialog.dismiss()
    }

    dialog.setOnDismissListener {
        if(setting.height < 0 ) setting.height = 0f
        if(setting.weight < 0 ) setting.weight = 0f

        updateProfile(UpdateProfile.Params(date,setting.height,setting.weight))
        Dlog.d("${setting.height},  ${setting.weight}")
        Dlog.d("activity.localClassName : ${activity.localClassName}")
        val pacemakerActivityName = activity.getString(R.string.title_activity_pacemaker)
        if(!activity.localClassName.contains(pacemakerActivityName))
            activity.finish()
    }

}


