package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.features.pacemaker.play.PlayPopupFragment
import javax.inject.Inject
import kotlin.math.roundToInt

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
        initLayout()

    }
    private fun initLayout(){

        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width *0.8).roundToInt(), (height *0.5).roundToInt())

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))	// 배경화면 투명하게 하는 코드

    }
    override fun fragment() = PlayPopupFragment()
}
