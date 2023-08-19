package com.devjj.pacemaker.features.pacemaker.addition

import android.graphics.Paint
import android.os.Build
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.databinding.FragmentAdditionBinding

// 부위 로테이션 함수(왼쪽과 오른쪽 화살 이미지 클릭했을 떄 변수가 변하는 함수
// direction 이 0이면 left, 1이면 right
fun rotationPartId(direction: Int){
    if(additionData_part_img < 0 || additionData_part_img > 5){
        Dlog.d( "rotationPartId error")
    }

    when(direction){
        DIRECTION_LEFT -> additionData_part_img--
        DIRECTION_RIGHT -> additionData_part_img++
        else -> Dlog.d( "rotationPartId when error")
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
        INTERVAL_MINUS -> additionData_interval -= REST_TIME_INTERVAL
        INTERVAL_PLUS -> additionData_interval += REST_TIME_INTERVAL
        else -> Dlog.d( "calcIntervalTime error")
    }

    if(additionData_interval <= REST_TIME_MINVALUE){
        additionData_interval = REST_TIME_MINVALUE
    }else if(additionData_interval >= REST_TIME_MAXVALUE){
        additionData_interval = REST_TIME_MAXVALUE
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
        Dlog.d( "convertIntervalTimeToSec error")
    }
    return result
}

// editText에 입력한 데이터들을 넣는 함수.
fun inputData(binding: FragmentAdditionBinding){
    binding.fAdditionEvName.hint = binding.fAdditionEvName.text.toString()
    additionData_name = binding.fAdditionEvName.text.toString()
    additionData_mass = binding.fAdditionNpMass.displayedValues[binding.fAdditionNpMass.value].toInt()
    additionData_rep = binding.fAdditionNpRep.displayedValues[binding.fAdditionNpRep.value-1].toInt()
    additionData_set = binding.fAdditionNpSet.displayedValues[binding.fAdditionNpSet.value-1].toInt()
    additionData_interval = convertIntervalTimeToSec(binding.fAdditionTvIntervalTime.text.toString())
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
                    Dlog.d( "exception $exception")
                }
            }
        }
    } else {
        numberPicker.textColor = color
    }
}