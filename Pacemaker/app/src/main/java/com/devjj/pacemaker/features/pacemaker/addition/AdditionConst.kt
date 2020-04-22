package com.devjj.pacemaker.features.pacemaker.addition

import com.devjj.pacemaker.core.extension.empty

var mode = 0
const val ADDITION_MODE = 0
const val EDITING_MODE = 1

var isNightMode: Boolean = false

const val DIRECTION_LEFT = 0
const val DIRECTION_RIGHT = 1

const val INTERVAL_MINUS = 0
const val INTERVAL_PLUS = 1

// 단위 초
const val REST_TIME_INTERVAL = 5
const val REST_TIME_MINVALUE = 0
const val REST_TIME_MAXVALUE = 300
//

const val MASS_MAXVALUE = 200
const val REP_MAXVALUE = 20
const val SET_MAXVALUE = 10


// 저장 버튼을 눌렀을 때 additionData의 데이터 변수들
var additionData_id = 0
var additionData_part_img = 0
var additionData_name = String.empty()
var additionData_mass = 0
var additionData_rep = 0
var additionData_set = 0
var additionData_interval = 0
//