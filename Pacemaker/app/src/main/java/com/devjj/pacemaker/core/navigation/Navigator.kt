package com.devjj.pacemaker.core.navigation

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.features.login.Authenticator
import com.devjj.pacemaker.features.login.LoginActivity
import com.devjj.pacemaker.features.pacemaker.*
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeView
import com.devjj.pacemaker.features.pacemaker.home.HomeViewModel
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupData
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupViewModel
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.dialog_give_up_exercise.view.*
import kotlinx.android.synthetic.main.dialog_profile_input.view.*
import kotlinx.android.synthetic.main.dialog_remove.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator, private val setting: SettingSharedPreferences){

    fun showLogin(context: Context) = context.startActivity(LoginActivity.callingIntent(context))

    fun showMain(context: Context) {
        when (authenticator.userLoggedIn()) {
            true -> showPacemaker(context)
            false -> showLogin(context)
        }
    }

    fun showPacemaker(context: Context) = context.startActivity(PacemakerActivity.callingIntent(context))

    // 옵션화면으로 이동하는 함수
    fun showOption(context: Context) = context.startActivity(OptionActivity.callingIntent(context))

    // 추가화면과 편집화면으로 이동하는 함수.
    fun showAddition(context: Context, additionView: AdditionView) = context.startActivity(AdditionActivity.callingIntent(context, additionView))

    // 삭제 다이얼 로그를 보여주는 함수.
    fun showDeleteDialog(activity: Activity, isNightMode: Boolean, homeViewModel: HomeViewModel, homeView: HomeView){
        val builder = AlertDialog.Builder(activity)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_remove, null)

        if(!isNightMode){
            // 화이트모드
            dialogView.dRemove_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_daytime, null)
            dialogView.dRemove_tv_main.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dRemove_tv_confirm.setTextColor(loadColor(activity, R.color.blue_bg_thick))
            dialogView.dRemove_tv_cancel.setTextColor(loadColor(activity, R.color.blue_bg_thick))
        }else{
            // 다크모드
            dialogView.dRemove_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_nighttime, null)
            dialogView.dRemove_tv_main.setTextColor(loadColor(activity, R.color.grey_bg_basic))
            dialogView.dRemove_tv_confirm.setTextColor(loadColor(activity, R.color.orange_bg_thick))
            dialogView.dRemove_tv_cancel.setTextColor(loadColor(activity, R.color.orange_bg_thick))
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

    // 삭제 다이얼 로그를 보여주는 함수.
    fun showProfileDialog(activity: Activity, isNightMode: Boolean, playPopupViewModel: PlayPopupViewModel, playPopupDataList: List<PlayPopupData>){
        val builder = AlertDialog.Builder(activity)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_profile_input, null)

        if(!isNightMode){
            // 화이트모드
            dialogView.dProfile_clo_main.background =
                    ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_daytime, null)
            dialogView.dProfile_tv_height.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dProfile_ev_height.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dProfile_ev_height.setHintTextColor(loadColor(activity, R.color.grey_bg_lightest))

            dialogView.dProfile_tv_weight.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dProfile_ev_weight.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dProfile_ev_weight.setHintTextColor(loadColor(activity, R.color.grey_bg_lightest))

            dialogView.dProfile_tv_confirm.setTextColor(loadColor(activity, R.color.blue_bg_thick))

        }else{
            // 다크모드
            dialogView.dProfile_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_nighttime, null)
            dialogView.dProfile_tv_height.setTextColor(loadColor(activity, R.color.grey_bg_basic))
            dialogView.dProfile_ev_height.setTextColor(loadColor(activity, R.color.blue_bg_thick))
            dialogView.dProfile_ev_height.setHintTextColor(loadColor(activity, R.color.white_txt_light))

            dialogView.dProfile_tv_weight.setTextColor(loadColor(activity, R.color.grey_bg_basic))
            dialogView.dProfile_ev_weight.setTextColor(loadColor(activity, R.color.blue_bg_thick))
            dialogView.dProfile_ev_weight.setHintTextColor(loadColor(activity, R.color.white_txt_light))

            dialogView.dProfile_tv_confirm.setTextColor(loadColor(activity, R.color.orange_bg_thick))
        }

        val dialog = builder.setView(dialogView).show()

        var fSaveHeight = 0f
        var fSaveWeight = 0f

        dialogView.dProfile_tv_confirm.setOnClickListener {
            Log.d("test", "showDeleteDialog confirm")
            val sSaveHeight = dialogView.dProfile_ev_height.text.toString()
            val sSaveWeight = dialogView.dProfile_ev_weight.text.toString()

            // 키와 몸무게 입력 칸을 빈칸으로 넘겼을 시 SharedPreferences에 저장된 값 가져오기!
            fSaveHeight = if(sSaveHeight == String.empty()) setting.height else sSaveHeight.toFloat()
            fSaveWeight = if(sSaveWeight == String.empty()) setting.weight else sSaveWeight.toFloat()
            //

            // 소수점 한자리 반올림하는 코드
            fSaveHeight = (fSaveHeight * 10).roundToInt() / 10f
            fSaveWeight = (fSaveWeight * 10).roundToInt() / 10f
            //

            setting.height = fSaveHeight
            setting.weight = fSaveWeight

            if(!playPopupViewModel.regexHeightAndWeight(fSaveHeight, fSaveWeight)){
                // 정상범위가 아니면 여기로 들어온다.
                val regexText = activity.getString(R.string.dprofile_tv_regex_str)
                Toast.makeText(activity, regexText, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            Log.d("test", "dialog onDismiss")

            val sSaveDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

            // 날짜가 같은 DB에 데이터 다 지우기
            playPopupViewModel.deleteExerciseHistoryData(sSaveDate)

            for(playPopupData in playPopupDataList) {
                Log.d("test", "id : ${playPopupData.id}, part_img : ${playPopupData.part_img}, name : ${playPopupData.name},\n"
                                  + "mass : ${playPopupData.mass}, rep : ${playPopupData.rep}, setGoal : ${playPopupData.setGoal},\n"
                                  + "setDone : ${playPopupData.setDone}, interval : ${playPopupData.interval}, achievement : ${playPopupData.achievement}")

                val insertPlayPopupData =
                    PlayPopupData(
                        0, playPopupData.part_img, playPopupData.name,
                        playPopupData.mass, playPopupData.rep, playPopupData.setGoal,
                        playPopupData.setDone, playPopupData.interval, playPopupData.achievement
                    )

                Log.d("test", "fSaveHeight : $fSaveHeight, fSaveWeight : $fSaveWeight")
                playPopupViewModel.saveExerciseHistoryData(insertPlayPopupData, sSaveDate, fSaveHeight, fSaveWeight)
            }

            // 데이터 초기화
            for(playPopupData in playPopupDataList){
                playPopupData.achievement = false
                playPopupData.setDone = 1
                playPopupViewModel.updateExercisePlayPopupData(playPopupData)
            }
            //

            // TimerService 종료
            TimerService.stopService(activity)
            //

            activity.finish()
        }
    }

    // 운동 포기 다이얼 로그를 보여주는 함수.
    fun showGiveUpExerciseDialog(activity: Activity, isNightMode: Boolean, playPopupViewModel: PlayPopupViewModel, playPopupData: PlayPopupData){
        val builder = AlertDialog.Builder(activity)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_give_up_exercise, null)

        dialogView.dGiveUp_tv_main.text = activity.getString(R.string.dgiveup_tv_main_str)
        if(!isNightMode){
            // 화이트모드
            dialogView.dGiveUp_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_daytime, null)
            dialogView.dGiveUp_tv_main.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dGiveUp_tv_confirm.setTextColor(loadColor(activity, R.color.blue_bg_thick))
            dialogView.dGiveUp_tv_cancel.setTextColor(loadColor(activity, R.color.blue_bg_thick))
        }else{
            // 다크모드
            dialogView.dGiveUp_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_nighttime, null)
            dialogView.dGiveUp_tv_main.setTextColor(loadColor(activity, R.color.grey_bg_basic))
            dialogView.dGiveUp_tv_confirm.setTextColor(loadColor(activity, R.color.orange_bg_thick))
            dialogView.dGiveUp_tv_cancel.setTextColor(loadColor(activity, R.color.orange_bg_thick))
        }

        val dialog = builder.setView(dialogView).show()

        dialogView.dGiveUp_tv_confirm.setOnClickListener {
            Log.d("test", "showGiveUpExerciseDialog confirm")

            playPopupData.achievement = true
            playPopupViewModel.updateExercisePlayPopupData(playPopupData)
            playPopupViewModel.loadPlayPopupList()
            dialog.dismiss()

        }

        dialogView.dGiveUp_tv_cancel.setOnClickListener {
            Log.d("test", "showGiveUpExerciseDialog cancel")
            dialog.dismiss()
        }
    }

    // 운동 전체 포기 다이얼 로그를 보여주는 함수.
    fun showGiveUpAllExerciseDialog(activity: Activity, isDarkMode: Boolean, playPopupViewModel: PlayPopupViewModel, playPopupDataList: List<PlayPopupData>){
        val builder = AlertDialog.Builder(activity)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_give_up_exercise, null)

        dialogView.dGiveUp_tv_main.text = activity.getString(R.string.dgiveupall_tv_main_str)
        if(!isDarkMode){
            // 화이트모드
            dialogView.dGiveUp_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_daytime, null)
            dialogView.dGiveUp_tv_main.setTextColor(loadColor(activity, R.color.grey_bg_thickest))
            dialogView.dGiveUp_tv_confirm.setTextColor(loadColor(activity, R.color.blue_bg_thick))
            dialogView.dGiveUp_tv_cancel.setTextColor(loadColor(activity, R.color.blue_bg_thick))
        }else{
            // 다크모드
            dialogView.dGiveUp_clo_main.background =
                ResourcesCompat.getDrawable(activity.resources, R.drawable.img_popup_background_nighttime, null)
            dialogView.dGiveUp_tv_main.setTextColor(loadColor(activity, R.color.grey_bg_basic))
            dialogView.dGiveUp_tv_confirm.setTextColor(loadColor(activity, R.color.orange_bg_thick))
            dialogView.dGiveUp_tv_cancel.setTextColor(loadColor(activity, R.color.orange_bg_thick))
        }

        val dialog = builder.setView(dialogView).show()

        dialogView.dGiveUp_tv_confirm.setOnClickListener {
            Log.d("test", "showGiveUpAllExerciseDialog confirm")

            for(playPopupData in playPopupDataList){
                playPopupData.achievement = true
                playPopupViewModel.updateExercisePlayPopupData(playPopupData)
            }

            playPopupViewModel.loadPlayPopupList()
            dialog.dismiss()
        }

        dialogView.dGiveUp_tv_cancel.setOnClickListener {
            Log.d("test", "showGiveUpAllExerciseDialog cancel")
            dialog.dismiss()
        }
    }

    fun showHistoryDetail(context : Context, date : String) = context.startActivity(HistoryDetailActivity.callingIntent(context,date))

    fun showPlayPopup(context: Context) : Boolean{
        context.startActivity(PlayPopupActivity.callingIntent(context))
        return true
    }

    // NavigationBottomView 화면 전환하는 함수.
    fun transitonNavigationBottomView(bottomView: BottomNavigationView, fragmentManager: FragmentManager,context: Context){
        val homeFragment = HomeFragment()
        bottomView.setOnNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.navigation_home -> {
                    fragmentManager.beginTransaction().replace(R.id.aPacemaker_flo_container, homeFragment).commit()
                }
                R.id.navigation_play -> {
                    // 현재 컨테이너에 있는 fragment 구하는 코드.
                    val fragment = fragmentManager.findFragmentById(R.id.aPacemaker_flo_container)
                    // homeFragment 이름 가져오는 함수.
                    val homeFragmentName = context.resources.getString(R.string.fhome_tv_fragment_name_str)
                    // 지금 보여지는 화면이 home이 아닐경우 home으로 이동하는 코드
                    if(!fragment.toString().contains(homeFragmentName)){
                        fragmentManager.beginTransaction().replace(R.id.aPacemaker_flo_container, HomeFragment()).commit()
                    }

                    showPlayPopup(context)

                }
                R.id.navigation_history -> {
                    fragmentManager.beginTransaction().replace(R.id.aPacemaker_flo_container, HistoryFragment()).commit()
                }
                else -> Log.d("test", "else") == 0
            }
            Log.d("test", "final") == 0
        }
    }

    class Extras(val transitionSharedElement: View)
}