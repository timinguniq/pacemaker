package com.devjj.pacemaker.features.pacemaker.addition

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_addition.*
import javax.inject.Inject

// 부위 로테이션 함수(왼쪽과 오른쪽 화살 이미지 클릭했을 떄 변수가 변하는 함수
// direction 이 0이면 left, 1이면 right
fun rotationPartId(direction: Int){
    if(additionData_part_img < 0 || additionData_part_img > 5){
        Log.d("test", "rotationPartId error")
    }

    when(direction){
        DIRECTION_LEFT -> additionData_part_img--
        DIRECTION_RIGHT -> additionData_part_img++
        else -> Log.d("test", "rotationPartId when error")
    }

    if(additionData_part_img < 0){
        additionData_part_img = 5
    }else if(additionData_part_img > 5){
        additionData_part_img = 0
    }
}

// 휴식 시간 관련 계산 함수
fun calcIntervalTime(increase: Int){
    when(increase) {
        INTERVAL_MINUS -> additionData_interval -= 5
        INTERVAL_PLUS -> additionData_interval += 5
        else -> Log.d("test", "calcIntervalTime error")
    }

    if(additionData_interval < 0){
        additionData_interval = 0
    }else if(additionData_interval > 300){
        additionData_interval = 300
    }
}

// 셋팅 인터벌 타임
fun settingIntervalTime(fAddition_tv_interval_time: TextView){
    var result: StringBuilder = StringBuilder()
    var min = additionData_interval / 60
    var sec = additionData_interval % 60
    result.append("0")
    result.append(min.toString())
    result.append(":")
    if(sec < 10)
        result.append("0")
    result.append(sec.toString())

    fAddition_tv_interval_time.text = result.toString()
}

// 인터벌 타임 -> 초로 변환
fun convertIntervalTimeToSec(input: String): Int{
    var result: Int = 0
    val arrayString = input.split(":")
    try {
        result = arrayString[0].toInt() * 60 + arrayString[1].toInt()
    }catch (exception: Exception){
        Log.d("test", "convertIntervalTimeToSec error")
    }
    return result
}

// editText에 입력한 데이터들을 넣는 함수.
fun inputData(activity: Activity){
    activity.fAddition_ev_name.hint = activity.fAddition_ev_name.text.toString()
    additionData_name = activity.fAddition_ev_name.text.toString()
    additionData_mass = activity.fAddition_np_mass.displayedValues[activity.fAddition_np_mass.value].toInt()
    additionData_rep = activity.fAddition_np_rep.displayedValues[activity.fAddition_np_rep.value-1].toInt()
    additionData_set = activity.fAddition_np_set.displayedValues[activity.fAddition_np_set.value-1].toInt()
    additionData_interval = convertIntervalTimeToSec(activity.fAddition_tv_interval_time.text.toString())
}

// 넘버 픽커 텍스트 색깔 설정하는 함수
fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int){
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

        val count = numberPicker.childCount
        for (i in 0..count) {
            val child = numberPicker.getChildAt(i)
            if (child is EditText) {
                try {
                    child.setTextColor(color)
                    numberPicker.invalidate()

                    var selectorWheelPaintField = numberPicker.javaClass.getDeclaredField("mSelectorWheelPaint")
                    var accessible = selectorWheelPaintField.isAccessible
                    selectorWheelPaintField.isAccessible = true
                    (selectorWheelPaintField.get(numberPicker) as Paint).color = color
                    selectorWheelPaintField.isAccessible = accessible
                    numberPicker.invalidate()

                    var selectionDividerField = numberPicker.javaClass.getDeclaredField("mSelectionDivider")
                    accessible = selectionDividerField.isAccessible
                    selectionDividerField.isAccessible = true
                    selectionDividerField.set(numberPicker, null)
                    selectionDividerField.isAccessible = accessible
                    numberPicker.invalidate()
                } catch (exception: Exception) {
                    Log.d("test", "exception $exception")
                }
            }
        }
    } else {
        numberPicker.textColor = color
    }
}