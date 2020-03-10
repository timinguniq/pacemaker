package com.devjj.pacemaker.core.navigation

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.features.login.Authenticator
import com.devjj.pacemaker.features.login.LoginActivity
import com.devjj.pacemaker.features.pacemaker.*
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator){
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
                    val homeFragmentName = context.resources.getString(R.string.fHome_fragment_name)
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