package com.devjj.pacemaker.features.pacemaker.addition

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.StringRes

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_addition.*
import javax.inject.Inject

class AdditionFragment(private val intent: Intent) : BaseFragment() {

    @Inject lateinit var setting: SettingSharedPreferences

    private var mode = 0
    private val ADDITION_MODE = 0
    private val EDITING_MODE = 1

    private var isDarkMode: Boolean = false

    private val DIRECTION_LEFT = 0
    private val DIRECTION_RIGHT = 1

    private val INTERVAL_MINUS = 0
    private val INTERVAL_PLUS = 1

    private lateinit var additionViewModel: AdditionViewModel

    // 저장 버튼을 눌렀을 때 additionData의 데이터 변수들
    private var additionData_id = 0
    private var additionData_part_img = 0
    private var additionData_name = String.empty()
    private var additionData_mass = 0
    private var additionData_rep = 0
    private var additionData_set = 0
    private var additionData_interval = 0
    //

    override fun layoutId() = R.layout.fragment_addition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        additionViewModel = viewModel(viewModelFactory){
            observe(additionData, ::renderExerciseData)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // homeFragment에서 받아온 intent로부터 데이터 가져오는 코드
        val additionView: AdditionView = intent.getParcelableExtra("view")?:AdditionView.empty()

        // additionData를 DB에서 찾아오는 함수.
        additionViewModel.loadTheAdditionData(additionView.id)

        // additionView의 name데이터를 이용하여 모드를 설정
        mode = if(additionView.name.isEmpty()) ADDITION_MODE else EDITING_MODE
        // 다크모드 설정
        isDarkMode = setting.isDarkMode

        // 뷰 초기화
        initializeView()

        // 클릭 리스터들 모아둔 함수.
        clickListener()

        // 넘버 픽커 초기화
        initNumberPicker()

        // 넘버 픽커 리스너
        numberPickerListener()
    }

    // AdditionFragment 초기화 하는 함수
    private fun initializeView() {
        initSettingMode(isDarkMode)

        // 초기화
        additionData_mass = 0
        additionData_rep = 1
        additionData_set = 1
        additionData_interval = 30
    }

    // additionFragment에 클릭 이벤트 리스너들
    private fun clickListener() {
        // 백키를 눌렀을 떄 리스너
        fAddition_iv_back.setOnClickListener {
            activity?.finish()
        }

        // 부위에서 왼쪽 화살 이미지 클릭 리스너
        fAddition_flo_part_left_arrow.setOnClickListener {
            rotationPartId(DIRECTION_LEFT)
            val tempPartImg = convertPartImgToResource(additionData_part_img, isDarkMode)
            fAddition_iv_part_main.setImageResource(tempPartImg)
        }

        // 부위에서 오른쪽 화살 이미지 클릭 리스너
        fAddition_flo_part_right_arrow.setOnClickListener {
            rotationPartId(DIRECTION_RIGHT)
            val tempPartImg = convertPartImgToResource(additionData_part_img, isDarkMode)
            fAddition_iv_part_main.setImageResource(tempPartImg)
        }

        // 휴식시간 마이너스 이미지 클릭 리스너
        fAddition_flo_interval_minus.setOnClickListener {
            // 인터벌 타임 계산하는 함수
            calcIntervalTime(INTERVAL_MINUS)
            // 인터벌 타임 셋팅
            settingIntervalTime()
        }

        // 휴식시간 플러스 이미지 클릭 리스너
        fAddition_flo_interval_plus.setOnClickListener {
            // 인터벌 타임 계산하는 함수
            calcIntervalTime(INTERVAL_PLUS)
            // 인터벌 타임 셋팅
            settingIntervalTime()
        }


        // 저장 이미지를 클릭했을때 리스너
        fAddition_flo_save.setOnClickListener {
            inputData()

            when(mode){
                ADDITION_MODE -> {
                    if(additionData_name != String.empty()) {
                        val additionData =
                            AdditionData(0, additionData_part_img, additionData_name, additionData_mass,
                                additionData_rep, additionData_set, additionData_interval)
                        additionViewModel.saveExerciseData(additionData)
                        activity?.finish()
                    }else{
                        fAddition_ev_name.hint = resources.getString(R.string.fAddition_name_hint_txv)
                        // TODO : 임시코드
                        Toast.makeText(context!!, "운동 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                EDITING_MODE -> {
                    if(additionData_name != String.empty()) {
                        val additionData =
                            AdditionData(additionData_id, additionData_part_img, additionData_name, additionData_mass,
                                additionData_rep, additionData_set, additionData_interval)
                        additionViewModel.updateExerciseData(additionData)
                        activity?.finish()
                    }else{
                        fAddition_ev_name.hint = resources.getString(R.string.fAddition_name_hint_txv)
                        // TODO: 임시코드
                        Toast.makeText(context!!, "운동 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Log.d("addition", "fAddition_iv_save.setOnClickListener error")
                }
            }
        }

    }

    // 넘버 픽커 초기화
    private fun initNumberPicker(){
        val data1: Array<String> = Array(201){
                i ->
                when(i){
                    in 0..9 -> "00$i"
                    in 10..99 -> "0$i"
                    in 100..200 -> i.toString()
                    else -> {
                        Log.d("test", "initNumberPicker error")
                        i.toString()
                    }
                }
        }

        val data2: Array<String> = Array(20) {
                i -> if((i+1)<10) "0" + (i+1).toString() else (i+1).toString()
        }

        // 테스트 코드
        //setNumberPickerTextColor(fAddition_np_mass, Color.argb(255, 0, 0, 0))
        //fAddition_np_mass.setBackgroundColor(Color.argb(255, 241, 241, 241))
        fAddition_np_mass.minValue = 0
        fAddition_np_mass.maxValue = data1.size-1
        fAddition_np_mass.wrapSelectorWheel = false
        data1.reverse()
        fAddition_np_mass.displayedValues = data1
        fAddition_np_mass.value = 200

        //setNumberPickerTextColor(fAddition_np_rep, Color.argb(255, 0, 0, 0))
        //fAddition_np_rep.setBackgroundColor(Color.argb(255, 241, 241, 241))
        fAddition_np_rep.minValue = 1
        fAddition_np_rep.maxValue = data2.size
        fAddition_np_rep.wrapSelectorWheel = false
        data2.reverse()
        fAddition_np_rep.displayedValues = data2
        fAddition_np_rep.value = 20

        //setNumberPickerTextColor(fAddition_np_set, Color.argb(255, 0, 0, 0))
        //fAddition_np_set.setBackgroundColor(Color.argb(255, 241, 241, 241))
        fAddition_np_set.minValue = 1
        fAddition_np_set.maxValue = data2.size
        fAddition_np_set.wrapSelectorWheel = false
        //data2.reverse()
        fAddition_np_set.displayedValues = data2
        fAddition_np_set.value = 20
        //
    }


    // 넘버 픽커 리스너
    private fun numberPickerListener(){
        fAddition_np_mass.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("test", "mass oldVal : ${oldVal}, rep newVal : $newVal")
            Log.d("test", "mass picker.displayedValues ${picker.displayedValues[picker.value]}")
            additionData_mass = picker.displayedValues[picker.value].toInt()
        }

        fAddition_np_rep.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("test", "rep oldVal : ${oldVal}, rep newVal : $newVal")
            Log.d("test", "rep picker.displayedValues ${picker.displayedValues[picker.value-1]}")
            additionData_rep = picker.displayedValues[picker.value-1].toInt()
        }

        fAddition_np_set.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("test", "set oldVal : ${oldVal}, rep newVal : $newVal")
            Log.d("test", "set picker.displayedValues ${picker.displayedValues[picker.value-1]}")
            additionData_set = picker.displayedValues[picker.value-1].toInt()
        }
    }

    // 넘버 픽커 텍스트 색깔 설정하는 함수
    private fun setNumberPickerTextColor(numberPicker: NumberPicker,color: Int){
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

    // 뷰 모드에 대한 셋팅
    private fun initSettingMode(isDarkMode: Boolean){
        val listResource = mutableListOf<Int>()
        if(!isDarkMode){
            // 화이트모드
            listResource.add(wmStatusBarColor)
            listResource.add(R.drawable.apacemaker_wm_title_background)
            listResource.add(wmContainerColor)
            val partMainImage = convertPartImgToResource(additionData_part_img, false)
            listResource.add(partMainImage)
            listResource.add(R.drawable.faddition_wm_name_line_img)
            listResource.add(wmExerciseNameColor)
            listResource.add(wmAdditionMRSITitleColor)
            listResource.add(wmAdditionInContainerColor)
            listResource.add(R.drawable.faddition_wm_rest_minus_img)
            listResource.add(R.drawable.faddition_wm_rest_plus_img)
        }else{
            // 다크모드
            listResource.add(dmStatusBarColor)
            listResource.add(R.drawable.apacemaker_dm_title_background)
            listResource.add(dmContainerColor)
            val partMainImage = convertPartImgToResource(additionData_part_img, true)
            listResource.add(partMainImage)
            listResource.add(R.drawable.faddition_dm_name_line_img)
            listResource.add(dmExerciseNameColor)
            listResource.add(dmAdditionMRSITitleColor)
            listResource.add(dmAdditionInContainerColor)
            listResource.add(R.drawable.faddition_dm_rest_minus_img)
            listResource.add(R.drawable.faddition_dm_rest_plus_img)
        }

        activity?.window?.statusBarColor = listResource[0]
        fAddition_clo_status.setBackgroundResource(listResource[1])
        fAddition_clo_main.setBackgroundColor(listResource[2])
        fAddition_iv_part_main.setImageResource(listResource[3])
        fAddition_iv_name_line.setImageResource(listResource[4])
        fAddition_ev_name.setTextColor(listResource[5])
        fAddition_ev_name.setHintTextColor(listResource[5])

        fAddition_tv_mass.setTextColor(listResource[6])
        fAddition_tv_rep.setTextColor(listResource[6])
        fAddition_tv_set.setTextColor(listResource[6])
        fAddition_tv_interval.setTextColor(listResource[6])

        setNumberPickerTextColor(fAddition_np_mass, listResource[5])
        fAddition_np_mass.setBackgroundColor(listResource[7])
        setNumberPickerTextColor(fAddition_np_rep, listResource[5])
        fAddition_np_rep.setBackgroundColor(listResource[7])
        setNumberPickerTextColor(fAddition_np_set, listResource[5])
        fAddition_np_set.setBackgroundColor(listResource[7])

        fAddition_clo_interval_time.setBackgroundColor(listResource[7])
        fAddition_iv_interval_minus.setImageResource(listResource[8])
        fAddition_iv_interval_plus.setImageResource(listResource[9])
        fAddition_tv_interval_time.setTextColor(listResource[5])
    }

    // 부위 로테이션 함수(왼쪽과 오른쪽 화살 이미지 클릭했을 떄 변수가 변하는 함수
    // direction 이 0이면 left, 1이면 right
    private fun rotationPartId(direction: Int){
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
    private fun calcIntervalTime(increase: Int){
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
    private fun settingIntervalTime(){
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
    private fun convertIntervalTimeToSec(input: String): Int{
        var result: Int = 0
        val arrayString = input.split(":")
        try {
            result = arrayString[0].toInt() * 60 + arrayString[1].toInt()
        }catch (exception: Exception){
            Log.d("test", "convertIntervalTimeToSec error")
        }
        return result
    }

    // Exercise 데이터들 갱신하는 함수.
    private fun renderExerciseData(additionView: AdditionView?) {
        val tempAdditionView = additionView?:AdditionView.empty()

        // 모드에 따라 추가모드일때는 뷰를 비어있게 나타내고 편집모드일때는 관련 데이터를 표시해준다.
        when (mode) {
            ADDITION_MODE -> {
                fAddition_ev_name.text = String.editText(String.empty())
            }
            EDITING_MODE -> {
                additionData_id = tempAdditionView.id
                additionData_part_img = tempAdditionView.part_img

                fAddition_ev_name.text = String.editText(tempAdditionView.name)
                fAddition_np_mass.value = 200 - tempAdditionView.mass
                fAddition_np_rep.value = 21 - tempAdditionView.rep
                fAddition_np_set.value = 21 - tempAdditionView.set
                additionData_interval = tempAdditionView.interval
                settingIntervalTime()
            }
            else -> {
                Log.d("addition", "renderExerciseData error")
            }
        }
    }

    // Exercise 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is AdditionFailure.ListNotAvailable -> renderFailure(R.string.fHome_list_unavailable)
            else -> Log.d("AdditionFragment", "error")
        }
    }


    private fun renderFailure(@StringRes message: Int) {
        //fHome_recyclerview.invisible()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }

    // editText에 입력한 데이터들을 넣는 함수.
    private fun inputData(){
        fAddition_ev_name.hint = fAddition_ev_name.text.toString()
        additionData_name = fAddition_ev_name.text.toString()
        additionData_mass = fAddition_np_mass.displayedValues[fAddition_np_mass.value].toInt()
        additionData_rep = fAddition_np_rep.displayedValues[fAddition_np_rep.value-1].toInt()
        additionData_set = fAddition_np_set.displayedValues[fAddition_np_set.value-1].toInt()
        additionData_interval = convertIntervalTimeToSec(fAddition_tv_interval_time.text.toString())
    }
}
