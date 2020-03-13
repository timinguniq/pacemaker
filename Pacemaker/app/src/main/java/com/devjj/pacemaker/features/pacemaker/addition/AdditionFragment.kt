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

    private lateinit var additionViewModel: AdditionViewModel

    private lateinit var additionListener: AdditionListener

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

        // additionListener 초기화
        additionListener = AdditionListener(activity!!, additionViewModel)

        // 클릭 리스너들 모아둔 함수.
        additionListener.clickListener()

        // 넘버 픽커 초기화
        initNumberPicker()

        // 넘버 픽커 리스너
        additionListener.numberPickerListener()
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

    // Exercise 데이터들 갱신하는 함수.
    private fun renderExerciseData(additionView: AdditionView?) {
        val tempAdditionView = additionView?:AdditionView.empty()

        // 모드에 따라 추가모드일때는 뷰를 비어있게 나타내고 편집모드일때는 관련 데이터를 표시해준다.
        when (mode) {
            ADDITION_MODE -> {
                fAddition_tv_title.text = resources.getString(R.string.fAddition_title_addition_txv)
                fAddition_ev_name.text = String.editText(String.empty())
            }
            EDITING_MODE -> {
                additionData_id = tempAdditionView.id
                additionData_part_img = tempAdditionView.part_img

                fAddition_tv_title.text = resources.getString(R.string.fAddition_title_edit_txv)
                fAddition_ev_name.text = String.editText(tempAdditionView.name)
                fAddition_np_mass.value = 200 - tempAdditionView.mass
                fAddition_np_rep.value = 21 - tempAdditionView.rep
                fAddition_np_set.value = 21 - tempAdditionView.set
                additionData_interval = tempAdditionView.interval
                settingIntervalTime(fAddition_tv_interval_time)
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
}
