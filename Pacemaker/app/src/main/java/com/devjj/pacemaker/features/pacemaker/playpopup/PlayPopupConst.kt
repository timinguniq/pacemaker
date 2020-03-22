package com.devjj.pacemaker.features.pacemaker.playpopup


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

var currentPlayPopupData: PlayPopupData = PlayPopupData.empty()