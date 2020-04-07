package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.addition.isDarkMode
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.google.android.gms.ads.AdRequest
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pacemaker.*
import javax.inject.Inject


class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    @Inject lateinit var navigator: Navigator
    // TODO : ExerciseData 임시 추가를 위한 변수 나중에 삭제해야 됨.
    @Inject lateinit var db: ExerciseDatabase
    @Inject lateinit var setting: SettingSharedPreferences

    override var layout = R.layout.activity_pacemaker
    override var fragmentId = R.id.aPacemaker_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pacemaker)
        appComponent.inject(this)
        Log.d("test", "onCreate PacemakerActivity")
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    override fun fragment() = HomeFragment()

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {

        // NavigationBottomView setting
        navigator.transitonNavigationBottomView(aPacemaker_bottom_navigation_view, supportFragmentManager,this)

        // settingImv clickListener
        aPacemaker_iv_setting.setOnClickListener {
            navigator.showOption(this)
        }

        // TODO : 여기서부터 그거 함수로 extension에 빼면 됨.
        if(!setting.isDarkMode){
            // TODO : 여기로 들어오면 다크모드가 아니다.
            window.statusBarColor = getColor(R.color.blue_bg_thick)
            aPacemaker_clo_title.setBackgroundResource(R.drawable.apacemaker_wm_title_background)
            aPacemaker_flo_container.setBackgroundColor(getColor(R.color.white))
            aPacemaker_bottom_navigation_view.setBackgroundColor(getColor(R.color.grey_bg_basic))
            aPacemaker_bottom_navigation_view.itemBackgroundResource = R.drawable.apacemaker_wm_bottom_icon_bg_color
            aPacemaker_bottom_navigation_view.itemIconTintList = resources.getColorStateList(R.color.apacemaker_wm_bottom_icon_color,null)
        }else{
            // TODO : 여기로 들어오면 다크모드
            window.statusBarColor = getColor(R.color.grey_bg_thickest)
            aPacemaker_clo_title.setBackgroundResource(R.drawable.apacemaker_dm_title_background)
            aPacemaker_flo_container.setBackgroundColor(getColor(R.color.grey_bg_thick))
            aPacemaker_bottom_navigation_view.setBackgroundColor(getColor(R.color.grey_bg_thickest))
            aPacemaker_bottom_navigation_view.itemBackgroundResource = R.drawable.apacemaker_dm_bottom_icon_bg_color
            aPacemaker_bottom_navigation_view.itemIconTintList = resources.getColorStateList(R.color.apacemaker_dm_bottom_icon_color,null)
        }

        // 광고 테스트 코드
        val adRequest = AdRequest.Builder().build()
        aPacemaker_adView.loadAd(adRequest)
        // TODO : 여기까지 인데. 광고 때문에 view로 매개변수 받아야 될 것 같음.
    }
}