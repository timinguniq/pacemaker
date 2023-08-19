package com.devjj.pacemaker.features.pacemaker.addition

import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.convertPartImgToResource
import com.devjj.pacemaker.core.extension.convertPartImgToStringResource
import com.devjj.pacemaker.core.extension.empty
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.databinding.FragmentAdditionBinding


class AdditionListener(val activity: Activity, val binding: FragmentAdditionBinding, val additionViewModel: AdditionViewModel) {

    private var clickTime: Long = 0
    private var clickTimeSave: Long = 0

    fun clickListener() {
        // 백키를 눌렀을 떄 리스너
        binding.fAdditionIvBack.setOnClickListener {
            activity.finish()
        }

        // 부위에서 왼쪽 화살 이미지 클릭 리스너
        binding.fAdditionFloPartLeftArrow.setOnClickListener {
            rotationPartId(DIRECTION_LEFT)
            val tempPartImg = convertPartImgToResource(additionData_part_img, isNightMode)
            binding.fAdditionIvPartMain.setImageResource(tempPartImg)
        }

        // 부위에서 오른쪽 화살 이미지 클릭 리스너
        binding.fAdditionFloPartRightArrow.setOnClickListener {
            rotationPartId(DIRECTION_RIGHT)
            val tempPartImg = convertPartImgToResource(additionData_part_img, isNightMode)
            binding.fAdditionIvPartMain.setImageResource(tempPartImg)
        }

        // 휴식시간 마이너스 이미지 클릭 리스너
        binding.fAdditionFloIntervalMinus.setOnClickListener {
            // 인터벌 타임 계산하는 함수
            calcIntervalTime(INTERVAL_MINUS)
            // 인터벌 타임 셋팅
            settingIntervalTime(binding.fAdditionTvIntervalTime)
        }

        // 휴식시간 플러스 이미지 클릭 리스너
        binding.fAdditionFloIntervalPlus.setOnClickListener {
            // 인터벌 타임 계산하는 함수
            calcIntervalTime(INTERVAL_PLUS)
            // 인터벌 타임 셋팅
            settingIntervalTime(binding.fAdditionTvIntervalTime)
        }

        // 저장 이미지를 클릭했을때 리스너
        binding.fAdditionFloSave.setOnClickListener {
            inputData(binding)

            when (mode) {
                ADDITION_MODE -> {
                    if (additionData_name != String.empty()) {
                        if(System.currentTimeMillis() - clickTimeSave < 2000){
                            return@setOnClickListener
                        }
                        val additionData =
                            AdditionData(
                                0, additionData_part_img, additionData_name, additionData_mass,
                                additionData_rep, additionData_set, additionData_interval
                            )
                        additionViewModel.saveExerciseData(additionData)
                        activity.finish()

                        clickTimeSave = System.currentTimeMillis()
                    } else {
                        // 운동이름 빈칸으로 했을 때
                        if(System.currentTimeMillis() - clickTime < 30000){
                            // 디폴트 운동 이름 받아오는 함수.
                            val additionData_default_name = convertPartImgToStringResource(additionData_part_img, activity)

                            val additionData =
                                AdditionData(
                                    0, additionData_part_img, additionData_default_name, additionData_mass,
                                    additionData_rep, additionData_set, additionData_interval
                                )
                            additionViewModel.saveExerciseData(additionData)
                            activity.finish()
                        }else{
                            clickTime = System.currentTimeMillis()

                            binding.fAdditionEvName.hint =
                                activity.resources.getString(R.string.faddition_tv_name_hint_str)

                            val exerciseName = activity.getString(R.string.faddition_tv_exercise_name_str)
                            Toast.makeText(activity, exerciseName, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                EDITING_MODE -> {
                    if (additionData_name != String.empty()) {
                        if(System.currentTimeMillis() - clickTimeSave < 2000){
                            return@setOnClickListener
                        }
                        val additionData =
                            AdditionData(
                                additionData_id,
                                additionData_part_img,
                                additionData_name,
                                additionData_mass,
                                additionData_rep,
                                additionData_set,
                                additionData_interval
                            )
                        additionViewModel.updateExerciseData(additionData)
                        activity.finish()

                        clickTimeSave = System.currentTimeMillis()
                    } else {
                        // 운동이름 빈칸으로 했을 때
                        if(System.currentTimeMillis() - clickTime < 30000){
                            // 디폴트 운동 이름 받아오는 함수.
                            val additionData_default_name = convertPartImgToStringResource(additionData_part_img, activity)

                            val additionData =
                                AdditionData(
                                    additionData_id, additionData_part_img, additionData_default_name, additionData_mass,
                                    additionData_rep, additionData_set, additionData_interval
                                )
                            additionViewModel.updateExerciseData(additionData)
                            activity.finish()
                        }else{
                            clickTime = System.currentTimeMillis()

                            binding.fAdditionEvName.hint =
                                activity.resources.getString(R.string.faddition_tv_name_hint_str)

                            val exerciseName = activity.getString(R.string.faddition_tv_exercise_name_str)
                            Toast.makeText(activity, exerciseName, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> {
                    Dlog.d( "fAddition_iv_save.setOnClickListener error")
                }
            }
        }

        // name edit에서 엔터를 누르면 저장으로 포커스를 보내기 위한 코드
        binding.fAdditionEvName.setOnEditorActionListener { _, actionId, _ ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> Dlog.d( "SEARCH")
                else -> {
                    // 엔터 키를 눌렀을 때 들어오는 코드
                    Dlog.d( "else")
                    binding.fAdditionIvSave.isFocusableInTouchMode = true
                    binding.fAdditionIvSave.requestFocus()
                }
            }
            true
        }
    }

    // 넘버 픽커 리스너
    fun numberPickerListener() {
        binding.fAdditionNpMass.setOnValueChangedListener { picker, oldVal, newVal ->
            Dlog.d( "mass oldVal : ${oldVal}, rep newVal : $newVal")
            Dlog.d( "mass picker.displayedValues ${picker.displayedValues[picker.value]}")
            additionData_mass = picker.displayedValues[picker.value].toInt()
        }
        binding.fAdditionNpRep.setOnValueChangedListener { picker, oldVal, newVal ->
            Dlog.d( "rep oldVal : ${oldVal}, rep newVal : $newVal")
            Dlog.d( "rep picker.displayedValues ${picker.displayedValues[picker.value - 1]}")
            additionData_rep = picker.displayedValues[picker.value - 1].toInt()
        }
        binding.fAdditionNpSet.setOnValueChangedListener { picker, oldVal, newVal ->
            Dlog.d( "set oldVal : ${oldVal}, rep newVal : $newVal")
            Dlog.d( "set picker.displayedValues ${picker.displayedValues[picker.value - 1]}")
            additionData_set = picker.displayedValues[picker.value - 1].toInt()
        }
    }
}