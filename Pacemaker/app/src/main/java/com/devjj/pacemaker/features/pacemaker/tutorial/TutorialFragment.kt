package com.devjj.pacemaker.features.pacemaker.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentTutorialBinding
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class TutorialFragment : BaseFragment() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var setting: SettingSharedPreferences

    private lateinit var tutorialViewModel: TutorialViewModel

    private lateinit var tutorialListener: TutorialListener

    private var currentTempItem = 0

    private var _binding: FragmentTutorialBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun layoutId() = R.layout.fragment_tutorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        tutorialViewModel = viewModel(viewModelFactory){
            observe(currentItemData, ::renderCurrentItemData)
            failure(failure, ::handleFailure)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // finish btn visible setting
        if(setting.height == -1.0f && setting.weight == -1.0f) setFinishBtnVisible(false)
        else setFinishBtnVisible(true)

        // viewModel로부터 currentItemData 가져오기
        currentTempItem = tutorialViewModel.currentItemData.value?:0
        Dlog.d("currentTempItem : $currentTempItem")
        binding.fTutorialViewpager.currentItem = 0
    }

    override fun onResume() {
        super.onResume()

        // 데이터 초기화
        initTutorialData()

        // viewpager 현재 아이템으로 셋팅하는 코드
        binding.fTutorialViewpager.currentItem = currentTempItem

        // tutorialListener 초기화
        tutorialListener = TutorialListener(activity!!, this, binding, navigator, setting, tutorialViewModel)

        // 클릭 리스너들 모아둔 함수.
        tutorialListener.clickListener()


        // 혹시나 쓸일 있으면 쓸 코드
        binding.fTutorialIndicator.indicatorsToShow = 5

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 데이터 초기화 하는 함수
    private fun initTutorialData(){
        val displayLanguage = Locale.getDefault().displayLanguage

        var tutorials = ArrayList<TutorialView>()
        when(displayLanguage){
            "English" -> {
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide1))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide2))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide3))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide4))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide5))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide6))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide7))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide8))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide9))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide10))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide11))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide12))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide13))
            }
            "한국어" -> {
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide1))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide2))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide3))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide4))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide5))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide6))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide7))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide8))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide9))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide10))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide11))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide12))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_kor_guide13))
            }
            else -> {
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide1))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide2))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide3))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide4))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide5))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide6))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide7))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide8))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide9))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide10))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide11))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide12))
                tutorials.add(TutorialView(0, R.drawable.ftutorial_img_eng_guide13))
            }
        }

        binding.fTutorialViewpager.adapter = TutorialAdapter(tutorials)
    }

    // finish btn visible
    fun setFinishBtnVisible(visible: Boolean){
        if(visible){
            binding.fTutorialIvFinish.visible()
        }else{
            binding.fTutorialIvFinish.invisible()
        }
    }

    // currentItem 데이터들 갱신하는 함수.
    private fun renderCurrentItemData(currentItem: Int?) {
        binding.fTutorialViewpager.currentItem = currentItem?:0
    }

    // currentItem 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is TutorialFailure.ListNotAvailable -> renderFailure(R.string.fhome_tv_list_unavailable_str)
            else -> Dlog.d( "error")
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        //fHome_recyclerview.gone()
        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }
}
