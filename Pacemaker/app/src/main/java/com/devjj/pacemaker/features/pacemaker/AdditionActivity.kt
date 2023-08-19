package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityAdditionBinding
import com.devjj.pacemaker.features.pacemaker.addition.AdditionFragment
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import com.google.android.gms.ads.AdRequest
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

    private lateinit var binding: ActivityAdditionBinding

    override var layout = R.layout.activity_addition
    override var fragmentId = R.id.aAddition_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionBinding.inflate(layoutInflater)
        appComponent.inject(this)
        initializeView()
        Dlog.d( "onCreate addition")
    }

    // AdditionActivity 초기화 하는 함수
    private fun initializeView() {
        val adRequest = AdRequest.Builder().build()
        binding.aAdditionAdView.loadAd(adRequest)
    }


}