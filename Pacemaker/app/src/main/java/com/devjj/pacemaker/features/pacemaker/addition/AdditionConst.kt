package com.devjj.pacemaker.features.pacemaker.addition

import com.devjj.pacemaker.core.extension.empty

var mode = 0
val ADDITION_MODE = 0
val EDITING_MODE = 1

var isDarkMode: Boolean = false

val DIRECTION_LEFT = 0
val DIRECTION_RIGHT = 1

val INTERVAL_MINUS = 0
val INTERVAL_PLUS = 1

// 저장 버튼을 눌렀을 때 additionData의 데이터 변수들
var additionData_id = 0
var additionData_part_img = 0
var additionData_name = String.empty()
var additionData_mass = 0
var additionData_rep = 0
var additionData_set = 0
var additionData_interval = 0
//