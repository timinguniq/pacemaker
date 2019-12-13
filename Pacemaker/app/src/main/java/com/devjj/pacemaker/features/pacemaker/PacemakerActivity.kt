package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment

import kotlinx.android.synthetic.main.activity_pacemaker.*
import javax.inject.Inject

class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    override fun fragment() = HomeFragment()

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()
    }

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        // NavigationBottomView setting
        navigator.transitonNavigationBottomView(ap_bottom_navigation_view, supportFragmentManager)
        // settingImv clickListener
        navigator.showSettingFragment(ap_setting_imv, ap_bottom_navigation_view, supportFragmentManager)
    }

}
