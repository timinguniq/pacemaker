package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.play.PlayPopupFragment
import javax.inject.Inject

class PlayPopupActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, PlayPopupActivity::class.java)
    }

    override var layout = R.layout.activity_play_popup
    override var fragmentId = R.id.aPlayPopup_flo_container

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun fragment() = PlayPopupFragment()
}
