package com.devjj.pacemaker.features.pacemaker.addition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.AdditionActivity
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import kotlinx.android.synthetic.main.fragment_addition.*

class AdditionFragment(private val intent: Intent) : BaseFragment() {

    private var mode = 0
    private val ADDITION_MODE = 0
    private val EDITING_MODE = 1

    private lateinit var additionViewModel: AdditionViewModel

    // part_img뷰들을 List로 선언한 변수.
    private lateinit var fAdditionIvPartList: List<ImageView>

    // part_img UnClick되어 있을때 이미지 자원들을 넣어놓은 리스트
    private val fAdditionIvPartUnClickResource = listOf(R.drawable.part_one_unclicked_img,
        R.drawable.part_two_unclicked_img,
        R.drawable.part_three_unclicked_img,
        R.drawable.part_four_unclicked_img,
        R.drawable.part_five_unclicked_img)

    // part_img Click되어 있을때 이미지 자원들을 넣어놓은 리스트
    private val fAdditionIvPartClickResource = listOf(R.drawable.part_one_clicked_img,
        R.drawable.part_two_clicked_img,
        R.drawable.part_three_clicked_img,
        R.drawable.part_four_clicked_img,
        R.drawable.part_five_clicked_img)

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

        // part_img list init
        fAdditionIvPartList = listOf(fAddition_iv_part_one, fAddition_iv_part_two, fAddition_iv_part_three,
                            fAddition_iv_part_four, fAddition_iv_part_five)

        // additionData를 DB에서 찾아오는 함수.
        additionViewModel.loadTheAdditionData(additionView.id)

        // additionView의 name데이터를 이용하여 모드를 설정
        mode = if(additionView.name.isEmpty()) ADDITION_MODE else EDITING_MODE

        // 클릭 리스터들 모아둔 함수.
        clickListener()


    }

    // additionFragment에 클릭 이벤트 리스너들
    private fun clickListener() {
        // 백키를 눌렀을 떄 리스너
        fAddition_iv_back.setOnClickListener {
            activity?.finish()
        }

        // part img 클릭 리스너들
        for(i in fAdditionIvPartList.indices) {
            fAdditionIvPartList[i].setOnClickListener {
                // part_img 초기화하는 함수.
                initPartImg()

                fAdditionIvPartList[i].setImageResource(fAdditionIvPartClickResource[i])
                additionData_part_img = i
            }
        }

        // 저장 이미지를 클릭했을때 리스너
        fAddition_iv_save.setOnClickListener {
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

    // Exercise 데이터들 갱신하는 함수.
    private fun renderExerciseData(additionView: AdditionView?) {
        val tempAdditionView = additionView?:AdditionView.empty()


        // Part Img를 초기화해주는 함수.
        initPartImg()

        // 모드에 따라 추가모드일때는 뷰를 비어있게 나타내고 편집모드일때는 관련 데이터를 표시해준다.
        when (mode) {
            ADDITION_MODE -> {
                fAddition_ev_name.text = String.editText(String.empty())
                fAddition_ev_mass.text = String.editText(String.empty())
                fAddition_ev_rep.text = String.editText(String.empty())
                fAddition_ev_set.text = String.editText(String.empty())
                fAddition_ev_interval.text = String.editText(String.empty())
            }
            EDITING_MODE -> {
                additionData_id = tempAdditionView.id
                additionData_part_img = tempAdditionView.part_img

                fAdditionIvPartList[tempAdditionView.part_img].setImageResource(fAdditionIvPartClickResource[tempAdditionView.part_img])
                fAddition_ev_name.text = String.editText(tempAdditionView.name)
                fAddition_ev_mass.text = String.editText(tempAdditionView.mass.toString())
                fAddition_ev_rep.text = String.editText(tempAdditionView.rep.toString())
                fAddition_ev_set.text = String.editText(tempAdditionView.set.toString())
                fAddition_ev_interval.text = String.editText(tempAdditionView.interval.toString())
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

    // part_img 초기화하는 함수.
    private fun initPartImg(){
        for(i in fAdditionIvPartList.indices){
            fAdditionIvPartList[i].setImageResource(fAdditionIvPartUnClickResource[i])
        }
    }

    // editText에 입력한 데이터들을 넣는 함수.
    private fun inputData(){
        additionData_name = fAddition_ev_name.text.toString()
        if(fAddition_ev_mass.text.toString() != String.empty()){
            additionData_mass = fAddition_ev_mass.text.toString().toInt()
        }
        if(fAddition_ev_rep.text.toString() != String.empty()){
            additionData_rep = fAddition_ev_rep.text.toString().toInt()
        }
        if(fAddition_ev_set.text.toString() != String.empty()){
            additionData_set = fAddition_ev_set.text.toString().toInt()
        }
        if(fAddition_ev_interval.text.toString() != String.empty()){
            additionData_interval = fAddition_ev_interval.text.toString().toInt()
        }
    }
}
