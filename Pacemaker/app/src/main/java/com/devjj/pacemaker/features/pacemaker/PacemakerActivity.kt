package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
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


        if(!setting.isDarkMode){
            // TODO : 여기로 들어오면 다크모드가 아니다.
            window.statusBarColor = wmStatusBarColor
            aPacemaker_clo_title.setBackgroundResource(R.drawable.apacemaker_wm_title_background)
            aPacemaker_flo_container.setBackgroundColor(wmContainerColor)
            aPacemaker_bottom_navigation_view.setBackgroundColor(wmBottomBgColor)
            aPacemaker_bottom_navigation_view.itemBackgroundResource = R.drawable.apacemaker_wm_bottom_icon_bg_color
            aPacemaker_bottom_navigation_view.itemIconTintList = resources.getColorStateList(R.color.apacemaker_wm_bottom_icon_color,null)
        }else{
            // TODO : 여기로 들어오면 다크모드
            window.statusBarColor = dmStatusBarColor
            aPacemaker_clo_title.setBackgroundResource(R.drawable.apacemaker_dm_title_background)
            aPacemaker_flo_container.setBackgroundColor(dmContainerColor)
            aPacemaker_bottom_navigation_view.setBackgroundColor(dmBottomBgColor)
            aPacemaker_bottom_navigation_view.itemBackgroundResource = R.drawable.apacemaker_dm_bottom_icon_bg_color
            aPacemaker_bottom_navigation_view.itemIconTintList = resources.getColorStateList(R.color.apacemaker_dm_bottom_icon_color,null)

        }

        // 광고 테스트 코드
        val adRequest = AdRequest.Builder().build()
        aPacemaker_adView.loadAd(adRequest)

    }

    // TODO: 임시로 ExerciseData만드는 코드 나중에 삭제해야 됨.
    private fun addExerciseData(){
        val a = ExerciseEntity(0, 0, "벤치프레스", 5, 10, 2, 30, false)
        val b = ExerciseEntity(1, 1, "데드리프트", 10, 10, 3, 40, false)
        val c = ExerciseEntity(2, 2, "스쿼드", 15, 10, 4, 50, false)
        val d = ExerciseEntity(3, 3, "레그레이즈", 20, 10, 3, 60, false)
        val e = ExerciseEntity(4, 4, "크런치(맛있다)", 25,10,  2, 70, false)
        val f = ExerciseEntity(5, 3, "이두컬", 30, 10, 3, 30, false)
        val g = ExerciseEntity(6, 2, "아놀드프레스", 35, 10, 3, 40, false)

        Flowable.just("abc")
            .subscribeOn(Schedulers.io())
            .subscribe {
                db.ExerciseDAO().insert(a, b, c, d, e, f, g)
            }.isDisposed
    }

    private fun deleteExerciseData(){
        Flowable.just("abc")
            .subscribeOn(Schedulers.io())
            .subscribe {
                db.ExerciseDAO().deleteAll()
            }.isDisposed
    }

    // 여기까지

}
