package com.devjj.pacemaker.features.pacemaker.playpopup

// 타이머 시간 셋팅하는 함수
fun settingFormatForTimer(time: Int): String{
    val min = if(time/60 < 10) "0${time/60}" else (time/60).toString()
    val sec = if(time%60 < 10) "0${time%60}" else (time%60).toString()
    val result: String = "$min:$sec"
    return result
}