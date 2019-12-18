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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

import kotlinx.android.synthetic.main.activity_pacemaker.*
import java.util.*
import javax.inject.Inject

class PacemakerActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    override fun fragment() = HomeFragment()

    @Inject lateinit var navigator: Navigator

    override var layout = R.layout.activity_pacemaker
    override var fragmentId = R.id.aPacemaker_container_flo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()
    }

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        // NavigationBottomView setting
        navigator.transitonNavigationBottomView(aPacemaker_bottom_navigation_view, supportFragmentManager)
        // settingImv clickListener
        navigator.showSettingFragment(aPacemaker_setting_iv, aPacemaker_bottom_navigation_view, supportFragmentManager)
/*
        val testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        */
        val adRequest = AdRequest.Builder().build()
        aPacemaker_adView.loadAd(adRequest)

    }

}
