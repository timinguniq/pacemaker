package com.devjj.pacemaker.features.pacemaker.addition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentAdditionBinding
import javax.inject.Inject

class AdditionFragment(private val intent: Intent) : BaseFragment() {

    @Inject lateinit var setting: SettingSharedPreferences

    private lateinit var additionViewModel: AdditionViewModel
    private lateinit var additionListener: AdditionListener

    private var _binding: FragmentAdditionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun layoutId() = R.layout.fragment_addition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        additionViewModel = viewModel(viewModelFactory){
            observe(additionData, ::renderExerciseData)
            failure(failure, ::handleFailure)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdditionBinding.inflate(inflater, container, false)
        return binding.root
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
        additionListener = AdditionListener(requireActivity(), binding, additionViewModel)

        // 클릭 리스너들 모아둔 함수.
        additionListener.clickListener()

        // 넘버 픽커 초기화
        initNumberPicker()

        // 넘버 픽커 리스너
        additionListener.numberPickerListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // AdditionFragment 초기화 하는 함수
    private fun initializeView() {
        initSettingMode(isNightMode)

        // 초기화
        additionData_part_img = 0
        val partImg = convertPartImgToResource(additionData_part_img, isNightMode)
        binding.fAdditionIvPartMain.setImageResource(partImg)
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
        binding.fAdditionNpMass.minValue = 0
        binding.fAdditionNpMass.maxValue = massData.size-1
        binding.fAdditionNpMass.wrapSelectorWheel = false
        massData.reverse()
        binding.fAdditionNpMass.displayedValues = massData
        binding.fAdditionNpMass.value = MASS_MAXVALUE

        binding.fAdditionNpRep.minValue = 1
        binding.fAdditionNpRep.maxValue = repData.size
        binding.fAdditionNpRep.wrapSelectorWheel = false
        repData.reverse()
        binding.fAdditionNpRep.displayedValues = repData
        binding.fAdditionNpRep.value = REP_MAXVALUE

        binding.fAdditionNpSet.minValue = 1
        binding.fAdditionNpSet.maxValue = setData.size
        binding.fAdditionNpSet.wrapSelectorWheel = false
        setData.reverse()
        binding.fAdditionNpSet.displayedValues = setData
        binding.fAdditionNpSet.value = SET_MAXVALUE
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
        binding.fAdditionCloStatus.setBackgroundResource(listResource[1])
        binding.fAdditionCloMain.setBackgroundColor(listResource[2])
        binding.fAdditionIvPartMain.setImageResource(listResource[3])
        binding.fAdditionIvNameLine.setImageResource(listResource[4])
        binding.fAdditionEvName.setTextColor(listResource[5])
        binding.fAdditionEvName.setHintTextColor(listResource[6])

        binding.fAdditionTvMass.setTextColor(listResource[7])
        binding.fAdditionTvRep.setTextColor(listResource[7])
        binding.fAdditionTvSet.setTextColor(listResource[7])
        binding.fAdditionTvInterval.setTextColor(listResource[7])

        setNumberPickerTextColor(binding.fAdditionNpMass, listResource[5])
        binding.fAdditionNpMass.setBackgroundColor(listResource[8])
        setNumberPickerTextColor(binding.fAdditionNpRep, listResource[5])
        binding.fAdditionNpRep.setBackgroundColor(listResource[8])
        setNumberPickerTextColor(binding.fAdditionNpSet, listResource[5])
        binding.fAdditionNpSet.setBackgroundColor(listResource[8])

        binding.fAdditionCloIntervalTime.setBackgroundColor(listResource[8])
        binding.fAdditionIvIntervalMinus.setImageResource(listResource[9])
        binding.fAdditionIvIntervalPlus.setImageResource(listResource[10])
        binding.fAdditionTvIntervalTime.setTextColor(listResource[5])

        binding.fAdditionIvSave.setImageResource(listResource[11])
    }

    // Exercise 데이터들 갱신하는 함수.
    private fun renderExerciseData(additionView: AdditionView?) {
        val tempAdditionView = additionView?:AdditionView.empty()

        // 모드에 따라 추가모드일때는 뷰를 비어있게 나타내고 편집모드일때는 관련 데이터를 표시해준다.
        when (mode) {
            ADDITION_MODE -> {
                binding.fAdditionTvTitle.text = resources.getString(R.string.faddition_tv_title_addition_str)
                binding.fAdditionEvName.text = String.editText(String.empty())
            }
            EDITING_MODE -> {
                additionData_id = tempAdditionView.id
                additionData_part_img = tempAdditionView.part_img
                val partImg = convertPartImgToResource(additionData_part_img, isNightMode)
                binding.fAdditionIvPartMain.setImageResource(partImg)

                binding.fAdditionTvTitle.text = resources.getString(R.string.faddition_tv_title_edit_str)
                binding.fAdditionEvName.text = String.editText(tempAdditionView.name)
                binding.fAdditionNpMass.value = MASS_MAXVALUE - tempAdditionView.mass
                binding.fAdditionNpRep.value = (REP_MAXVALUE + 1) - tempAdditionView.rep
                binding.fAdditionNpSet.value = (SET_MAXVALUE + 1) - tempAdditionView.set
                additionData_interval = tempAdditionView.interval
                settingIntervalTime(binding.fAdditionTvIntervalTime)
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
