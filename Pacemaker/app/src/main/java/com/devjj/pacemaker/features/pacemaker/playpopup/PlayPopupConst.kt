package com.devjj.pacemaker.features.pacemaker.playpopup

import android.graphics.Color


// 현재 셋트 수를 나타내는 변수.
var currentSet: Int = 1
var maxSet: Int = 0

var isDarkMode: Boolean = false

// Timer 동작 모드
var mode = 0
val STOP_MODE = 0
val PROGRESS_MODE = 1
//

// 운동 간 휴식시간.
var interval = 0
// plus click number
var plusClickNumber = 0
// plus interval(지금은 10초)
var plusInterval = 11
// max plus click number
val maxPlusClickNumber = 3
// 날짜
var date: String = ""
// 키
var height = 0
// 몸무게
var weight = 0
// 운동 총 시간(휴식시간 다 포함)
var totalTime = 0

var currentPlayPopupData: PlayPopupData = PlayPopupData.empty()

val wmCircleRimColor = Color.argb(255, 156, 179, 225)
val wmCircleBarColor = Color.argb(255, 92, 132, 208)

val dmCircleRimColor = Color.argb(255, 176, 70, 60)
val dmCircleBarColor = Color.argb(255, 247, 71, 35)
