package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.inTransaction
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.addition.AdditionFragment
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeView
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_addition.*
import kotlinx.android.synthetic.main.activity_pacemaker.*
import javax.inject.Inject

class AdditionActivity : BaseActivity() {
    companion object {
        fun callingIntent(context: Context, additionView: AdditionView) : Intent{
            val intent = Intent(context, AdditionActivity::class.java)
            intent.putExtra("view", additionView)
            return intent
        }
    }

    override fun fragment() = AdditionFragment(intent)

    @Inject lateinit var navigator: Navigator

    override var layout = R.layout.activity_addition
    override var fragmentId = R.id.aAddition_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        initializeView()
    }

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        val adRequest = AdRequest.Builder().build()
        aAddition_adView.loadAd(adRequest)
    }


}