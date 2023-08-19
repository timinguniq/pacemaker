package com.devjj.pacemaker.core.extension

import com.devjj.pacemaker.BuildConfig
import java.util.*

const val GET_HEIGHT_ONLY = 1

const val GET_WEIGHT_ONLY = 2

const val GET_HEIGHT_WEIGHT = GET_HEIGHT_ONLY + GET_WEIGHT_ONLY
//2020-05-27 marker_1
/*val GET_HEIGHT_WEIGHT = 0*/

const val EXERCISE_NAME_ENG_HOME_LEN_MAX = 20
const val EXERCISE_NAME_ENG_PLAY_LEN_MAX = 16
const val EXERCISE_NAME_ENG_HISTORY_LEN_MAX = 34
const val EXERCISE_NAME_KOR_HOME_LEN_MAX = 11
const val EXERCISE_NAME_KOR_PLAY_LEN_MAX = 8
const val EXERCISE_NAME_KOR_HISTORY_LEN_MAX = 17

const val EXERCISE_NAME_HOME = 0
const val EXERCISE_NAME_PLAY = 1
const val EXERCISE_NAME_HISTORY = 2

// 현재 화면 분할
var cur_division = 0
// 첫번째 분할
const val DIVISION_1 = 0
// 두번째 분할
const val DIVISION_2 = 1
// 세번째 분할
const val DIVISION_3 = 2


const val APP_VERSION = BuildConfig.VERSION_NAME