package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.databinding.ActivityTutorialBinding
import com.devjj.pacemaker.features.pacemaker.tutorial.TutorialFragment

class TutorialActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, TutorialActivity::class.java)
    }

    private lateinit var binding: ActivityTutorialBinding

    override var layout = R.layout.activity_tutorial
    override var fragmentId = R.id.aTutorial_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        window.statusBarColor = loadColor(this,R.color.grey_88898A)
    }

    override fun fragment() = TutorialFragment()


}
