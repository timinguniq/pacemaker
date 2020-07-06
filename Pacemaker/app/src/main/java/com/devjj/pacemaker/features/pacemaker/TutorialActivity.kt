package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.playpopup.STOP_MODE
import com.devjj.pacemaker.features.pacemaker.playpopup.mode
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import com.devjj.pacemaker.features.pacemaker.tutorial.TutorialFragment

class TutorialActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, TutorialActivity::class.java)
    }

    override var layout = R.layout.activity_tutorial
    override var fragmentId = R.id.aTutorial_flo_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        window.statusBarColor = loadColor(this,R.color.grey_88898A)
    }

    override fun fragment() = TutorialFragment()


}
