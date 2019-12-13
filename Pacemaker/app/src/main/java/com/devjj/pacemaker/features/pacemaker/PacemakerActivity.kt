package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment

import kotlinx.android.synthetic.main.activity_pacemaker.*

class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    override fun fragment() = HomeFragment()

    // TODO : 임시 변수 나중에 지워라
    private var isCheckedSettingImv: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeView()
    }

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        ap_bottom_navigation_view.setOnNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.navigation_home ->
                    supportFragmentManager.beginTransaction().replace(R.id.ap_container_frame, HomeFragment()).commit() == 0
                R.id.navigation_play ->
                    Log.d("test", "play checked") == 0
                R.id.navigation_history ->
                    supportFragmentManager.beginTransaction().replace(R.id.ap_container_frame, HistoryFragment()).commit() == 0
                else -> Log.d("test", "else") == 0
            }
        }

        ap_setting_imv.setOnClickListener {
            if(!isCheckedSettingImv) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.ap_container_frame, OptionFragment()).commit()
                ap_bottom_navigation_view.visibility = View.GONE
                isCheckedSettingImv = true
            }else{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.ap_container_frame, HomeFragment()).commit()
                ap_bottom_navigation_view.visibility = View.VISIBLE
                isCheckedSettingImv = false
            }
        }

    }

}
