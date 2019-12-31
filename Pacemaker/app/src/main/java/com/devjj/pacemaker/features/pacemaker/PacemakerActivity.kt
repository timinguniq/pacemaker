package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.history.HistoryFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.option.OptionFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_pacemaker.*
import java.util.*
import javax.inject.Inject

class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    override fun fragment() = HomeFragment()

    @Inject lateinit var navigator: Navigator
    // TODO : ExerciseData 임시 추가를 위한 변수 나중에 삭제해야 됨.
    @Inject lateinit var db: ExerciseDatabase

    override var layout = R.layout.activity_pacemaker
    override var fragmentId = R.id.aPacemaker_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()

    }

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        // NavigationBottomView setting
        navigator.transitonNavigationBottomView(aPacemaker_bottom_navigation_view, supportFragmentManager,this)
        // settingImv clickListener
        navigator.showSettingFragment(aPacemaker_iv_setting, aPacemaker_bottom_navigation_view, supportFragmentManager)

        // TODO : 나중에 삭제해야 되는 코드
        aPacemaker_tv_add.setOnClickListener {
            // TODO : 나중에 삭제해야 될 코드
            addExerciseData()
        }

        aPacemaker_tv_delete.setOnClickListener {
            deleteExerciseData()
        }
        // 여기까지

        // 광고 테스트 코드
        val adRequest = AdRequest.Builder().build()
        aPacemaker_adView.loadAd(adRequest)

    }

    // TODO: 임시로 ExerciseData만드는 코드 나중에 삭제해야 됨.
    private fun addExerciseData(){
        val a = ExerciseEntity(0, 0, "벤치프레스", 5, 10, 2, 30, false)
        val b = ExerciseEntity(1, 1, "데드리프트", 10, 10, 3, 40, false)
        val c = ExerciseEntity(2, 2, "스쿼드", 15, 10, 4, 50, false)
        val d = ExerciseEntity(3, 3, "레그레이즈", 20, 10, 5, 60, false)
        val e = ExerciseEntity(4, 4, "크런치(맛있다)", 25,10,  6, 70, false)
        val f = ExerciseEntity(5, 3, "이두컬", 30, 10, 7, 30, false)
        val g = ExerciseEntity(6, 2, "아놀드프레스", 35, 10, 8, 40, false)
        val h = ExerciseEntity(7, 1, "숄더프레스", 40, 10, 7, 50, false)
        val i = ExerciseEntity(8, 2, "플랭크", 0, 10, 6, 60, false)
        val j = ExerciseEntity(9, 3, "풀업", 0, 10, 5, 70, false)
        val k = ExerciseEntity(10, 3, "팔굽혀펴기", 0, 10, 4, 20, false)
        val l = ExerciseEntity(11, 4, "벤치프레스", 30, 10, 6, 30, false)

        Flowable.just("abc")
            .subscribeOn(Schedulers.io())
            .subscribe {
                db.ExerciseDAO().insert(a, b, c, d, e, f, g, h, i, j, k, l)
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
