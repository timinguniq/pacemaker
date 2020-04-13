package com.devjj.pacemaker.features.pacemaker.playpopup

import android.app.Activity
import android.util.Log
import com.devjj.pacemaker.core.extension.invisible
import com.devjj.pacemaker.core.extension.visible
import kotlinx.android.synthetic.main.fragment_play_popup.*

// 타이머 시간 셋팅하는 함수
fun settingFormatForTimer(time: Int): String{
    val min = if(time/60 < 10) "0${time/60}" else (time/60).toString()
    val sec = if(time%60 < 10) "0${time%60}" else (time%60).toString()
    val result: String = "$min:$sec"
    return result
}

