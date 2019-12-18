package com.devjj.pacemaker.core.navigation

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.features.login.Authenticator
import com.devjj.pacemaker.features.login.LoginActivity
import com.devjj.pacemaker.features.pacemaker.AdditionActivity
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_pacemaker.*
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

    fun showAddition(context: Context) = context.startActivity(AdditionActivity.callingIntent(context))

    // NavigationBottomView 화면 전환하는 함수.
    fun transitonNavigationBottomView(bottomView: BottomNavigationView, fragmentManger: FragmentManager){
        bottomView.setOnNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.navigation_home ->
                    fragmentManger.beginTransaction().replace(R.id.aPacemaker_flo_container, HomeFragment()).commit() == 0
                R.id.navigation_play ->
                    Log.d("test", "play checked") == 0
                R.id.navigation_history ->
                    fragmentManger.beginTransaction().replace(R.id.aPacemaker_flo_container, HistoryFragment()).commit() == 0
                else -> Log.d("test", "else") == 0
            }
        }
    }

    // settingFragment 띄어주는 함수.
    fun showSettingFragment(settingImv: ImageView, bottomView: BottomNavigationView, fragmentManger: FragmentManager){
        var isChecked: Boolean = false
        settingImv.setOnClickListener {
            if(!isChecked) {
                fragmentManger.beginTransaction()
                    .replace(R.id.aPacemaker_flo_container, OptionFragment()).commit()
                bottomView.visibility = View.GONE
                isChecked = true
            }else{
                fragmentManger.beginTransaction()
                    .replace(R.id.aPacemaker_flo_container, HomeFragment()).commit()
                bottomView.visibility = View.VISIBLE
                isChecked = false
            }
        }
    }


    class Extras(val transitionSharedElement: View)
}