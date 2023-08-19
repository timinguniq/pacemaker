package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityPacemakerBinding
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.google.android.gms.ads.AdRequest
import javax.inject.Inject

class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }


    @Inject lateinit var navigator: Navigator
    // TODO : ExerciseData 임시 추가를 위한 변수 나중에 삭제해야 됨.
    @Inject lateinit var db: ExerciseDatabase
    @Inject lateinit var setting: SettingSharedPreferences

    private lateinit var binding: ActivityPacemakerBinding

    override var layout = R.layout.activity_pacemaker
    override var fragmentId = R.id.aPacemaker_flo_container

    private var backKeyTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPacemakerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        appComponent.inject(this)
        Dlog.d( "onCreate PacemakerActivity")
    }

    override fun onResume() {
        super.onResume()
        initializeView()
    }

    override fun fragment() = HomeFragment()

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        Dlog.d("initializeView")
        // NavigationBottomView setting

        navigator.transitonNavigationBottomView(binding.aPacemakerBottomNavigationView, supportFragmentManager,this)

        // settingImv clickListener
        binding.aPacemakerIvSetting.setOnClickListener {
            Dlog.d("aPacemakerIvSetting")
            navigator.showOption(this)
        }

        if(!setting.isNightMode){
            // 화이트 모드
            window.statusBarColor = loadColor(this,R.color.blue_5F87D6)
            binding.aPacemakerCloTitle.setBackgroundResource(R.drawable.img_title_background_daytime)
            binding.aPacemakerFloContainer.setBackgroundColor(loadColor(this,R.color.white_FFFFFF))
            binding.aPacemakerBottomNavigationView.setBackgroundColor(loadColor(this,R.color.grey_F9F9F9))
            binding.aPacemakerBottomNavigationView.itemBackgroundResource = R.drawable.apacemaker_bottom_icon_bg_color_daytime
            binding.aPacemakerBottomNavigationView.itemIconTintList = resources.getColorStateList(R.color.apacemaker_wm_bottom_icon_color,null)
        }else{
            // 다크 모드
            window.statusBarColor = loadColor(this,R.color.grey_444646)
            binding.aPacemakerCloTitle.setBackgroundResource(R.drawable.img_title_background_nighttime)
            binding.aPacemakerFloContainer.setBackgroundColor(loadColor(this,R.color.grey_606060))
            binding.aPacemakerBottomNavigationView.setBackgroundColor(loadColor(this,R.color.grey_444646))
            binding.aPacemakerBottomNavigationView.itemBackgroundResource = R.drawable.apacemaker_bottom_icon_bg_color_nighttime
            binding.aPacemakerBottomNavigationView.itemIconTintList = resources.getColorStateList(R.color.apacemaker_dm_bottom_icon_color,null)
        }

        // 광고 테스트 코드
        val adRequest = AdRequest.Builder().build()
        binding.aPacemakerAdView?.loadAd(adRequest)
        // TODO : 여기까지 인데. 광고 때문에 view로 매개변수 받아야 될 것 같음.
    }


    override fun onBackPressed() {
        //super.onBackPressed()
        Dlog.d( "PacemakerActivity onBackPressed()")

        if(System.currentTimeMillis() - backKeyTime < 2000){
            Dlog.d( "PacemakerActivity if")
            finishAffinity()

        }else{
            val toastStr = resources.getString(R.string.apacemaker_tv_terminate_str)
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show()
        }
        backKeyTime = System.currentTimeMillis()
    }

    fun getBinding() = binding

}