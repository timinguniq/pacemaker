package com.devjj.pacemaker.features.pacemaker.addition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
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
        isNightMode = setting.isNightMode

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
        initSettingMode(isNightMode)

        // 초기화
        additionData_part_img = 0
        val partImg = convertPartImgToResource(additionData_part_img, isNightMode)
        fAddition_iv_part_main.setImageResource(partImg)
        additionData_mass = 0
        additionData_rep = 1
        additionData_set = 1
        additionData_interval = 30
        //
    }

    // 넘버 픽커 초기화
    private fun initNumberPicker(){
        val massData: Array<String> = Array(MASS_MAXVALUE+1){
            i ->
            when(i){
                in 0..9 -> "00$i"
                in 10..99 -> "0$i"
                in 100..200 -> i.toString()
                else -> {
                   Dlog.d("initNumberPicker error")
                   i.toString()
                }
            }
        }

        val repData: Array<String> = Array(REP_MAXVALUE) {
            i -> if((i+1)<10) "0" + (i+1).toString() else (i+1).toString()
        }

        val setData: Array<String> = Array(SET_MAXVALUE){
            i -> if((i+1)<10) "0" + (i+1).toString() else (i+1).toString()
        }

        // 테스트 코드
        fAddition_np_mass.minValue = 0
        fAddition_np_mass.maxValue = massData.size-1
        fAddition_np_mass.wrapSelectorWheel = false
        massData.reverse()
        fAddition_np_mass.displayedValues = massData
        fAddition_np_mass.value = MASS_MAXVALUE

        fAddition_np_rep.minValue = 1
        fAddition_np_rep.maxValue = repData.size
        fAddition_np_rep.wrapSelectorWheel = false
        repData.reverse()
        fAddition_np_rep.displayedValues = repData
        fAddition_np_rep.value = REP_MAXVALUE

        fAddition_np_set.minValue = 1
        fAddition_np_set.maxValue = setData.size
        fAddition_np_set.wrapSelectorWheel = false
        setData.reverse()
        fAddition_np_set.displayedValues = setData
        fAddition_np_set.value = SET_MAXVALUE
        //
    }


    // 뷰 모드에 대한 셋팅
    private fun initSettingMode(isNightMode: Boolean){
        val listResource = mutableListOf<Int>()
        if(!isNightMode){
            // 화이트모드
            listResource.add(loadColor(activity!!, R.color.blue_5F87D6))
            listResource.add(R.drawable.img_title_background_daytime)
            listResource.add(loadColor(activity!!, R.color.white_FFFFFF))
            val partMainImage = convertPartImgToResource(additionData_part_img, false)
            listResource.add(partMainImage)
            listResource.add(R.drawable.faddition_img_name_line_daytime)
            listResource.add(loadColor(activity!!, R.color.black_3B4046))
            listResource.add(loadColor(activity!!, R.color.black_3B4046_38))
            listResource.add(loadColor(activity!!, R.color.grey_87888A))
            listResource.add(loadColor(activity!!, R.color.grey_F9F9F9))
            listResource.add(R.drawable.img_rest_minus_daytime)
            listResource.add(R.drawable.img_rest_plus_daytime)
            listResource.add(R.drawable.img_save_daytime)
        }else{
            // 다크모드
            listResource.add(loadColor(activity!!, R.color.grey_444646))
            listResource.add(R.drawable.img_title_background_nighttime)
            listResource.add(loadColor(activity!!, R.color.grey_606060))
            val partMainImage = convertPartImgToResource(additionData_part_img, true)
            listResource.add(partMainImage)
            listResource.add(R.drawable.faddition_img_name_line_nighttime)
            listResource.add(loadColor(activity!!, R.color.white_FFFFFF))
            listResource.add(loadColor(activity!!, R.color.white_FFFFFF_38))
            listResource.add(loadColor(activity!!, R.color.grey_B2B2B2))
            listResource.add(loadColor(activity!!, R.color.grey_444646))
            listResource.add(R.drawable.img_rest_minus_nighttime)
            listResource.add(R.drawable.img_rest_plus_nighttime)
            listResource.add(R.drawable.img_save_nighttime)
        }

        activity?.window?.statusBarColor = listResource[0]
        fAddition_clo_status.setBackgroundResource(listResource[1])
        fAddition_clo_main.setBackgroundColor(listResource[2])
        fAddition_iv_part_main.setImageResource(listResource[3])
        fAddition_iv_name_line.setImageResource(listResource[4])
        fAddition_ev_name.setTextColor(listResource[5])
        fAddition_ev_name.setHintTextColor(listResource[6])

        fAddition_tv_mass.setTextColor(listResource[7])
        fAddition_tv_rep.setTextColor(listResource[7])
        fAddition_tv_set.setTextColor(listResource[7])
        fAddition_tv_interval.setTextColor(listResource[7])

        setNumberPickerTextColor(fAddition_np_mass, listResource[5])
        fAddition_np_mass.setBackgroundColor(listResource[8])
        setNumberPickerTextColor(fAddition_np_rep, listResource[5])
        fAddition_np_rep.setBackgroundColor(listResource[8])
        setNumberPickerTextColor(fAddition_np_set, listResource[5])
        fAddition_np_set.setBackgroundColor(listResource[8])

        fAddition_clo_interval_time.setBackgroundColor(listResource[8])
        fAddition_iv_interval_minus.setImageResource(listResource[9])
        fAddition_iv_interval_plus.setImageResource(listResource[10])
        fAddition_tv_interval_time.setTextColor(listResource[5])

        fAddition_iv_save.setImageResource(listResource[11])
    }

    // Exercise 데이터들 갱신하는 함수.
    private fun renderExerciseData(additionView: AdditionView?) {
        val tempAdditionView = additionView?:AdditionView.empty()

        // 모드에 따라 추가모드일때는 뷰를 비어있게 나타내고 편집모드일때는 관련 데이터를 표시해준다.
        when (mode) {
            ADDITION_MODE -> {
                fAddition_tv_title.text = resources.getString(R.string.faddition_tv_title_addition_str)
                fAddition_ev_name.text = String.editText(String.empty())
            }
            EDITING_MODE -> {
                additionData_id = tempAdditionView.id
                additionData_part_img = tempAdditionView.part_img

                fAddition_tv_title.text = resources.getString(R.string.faddition_tv_title_edit_str)
                fAddition_ev_name.text = String.editText(tempAdditionView.name)
                fAddition_np_mass.value = MASS_MAXVALUE - tempAdditionView.mass
                fAddition_np_rep.value = (REP_MAXVALUE + 1) - tempAdditionView.rep
                fAddition_np_set.value = (SET_MAXVALUE + 1) - tempAdditionView.set
                additionData_interval = tempAdditionView.interval
                settingIntervalTime(fAddition_tv_interval_time)
            }
            else -> {
                Dlog.d( "renderExerciseData error")
            }
        }
    }

    // Exercise 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is AdditionFailure.ListNotAvailable -> renderFailure(R.string.fhome_tv_list_unavailable_str)
            else -> Dlog.d( "error")
        }
    }


    private fun renderFailure(@StringRes message: Int) {
        //fHome_recyclerview.gone()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }
}
