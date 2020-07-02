package com.devjj.pacemaker.core.navigation

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
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

    // TODO : 나중에 showpacemaker를 showTutorial로 바꾸고 tutorial 슬라이드하는거 연구해야 될듯.
    fun showMain(context: Context) {
        when (authenticator.userLoggedIn()) {
            // TODO :
            true -> if(setting.height == -1.0f && setting.weight == -1.0f) showTutorial(context)
                    else showPacemaker(context)
            false -> showLogin(context)
        }
    }

    fun showLicense(context: Context) = context.startActivity(OpenSourceActivity.callingIntent(context))

    fun showTutorial(context: Context) = context.startActivity(TutorialActivity.callingIntent(context))

    fun showPacemaker(context: Context) = context.startActivity(PacemakerActivity.callingIntent(context))

    // 옵션화면으로 이동하는 함수
    fun showOption(context: Context) = context.startActivity(OptionActivity.callingIntent(context))

    // 추가화면과 편집화면으로 이동하는 함수.
    fun showAddition(context: Context, additionView: AdditionView) = context.startActivity(AdditionActivity.callingIntent(context, additionView))

    fun showStatistics(context : Context) = context.startActivity(StatisticsActivity.callingIntent(context))

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
                    val homeFragmentName = context.resources.getString(R.string.fhome_tv_fragment_name_str )
                    // 지금 보여지는 화면이 home이 아닐경우 home으로 이동하는 코드
                    if(!fragment.toString().contains(homeFragmentName)){
                        fragmentManager.beginTransaction().replace(R.id.aPacemaker_flo_container, HomeFragment()).commit()
                    }

                    showPlayPopup(context)

                }
                R.id.navigation_history -> {
                    fragmentManager.beginTransaction().replace(R.id.aPacemaker_flo_container, HistoryFragment()).commit()
                }
                else -> Dlog.d( "else")
            }
            Dlog.d( "final")
            it.isChecked
        }
    }

    class Extras(val transitionSharedElement: View)
}