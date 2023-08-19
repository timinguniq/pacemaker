package com.devjj.pacemaker.features.pacemaker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.features.pacemaker.dialog.showProfileDialog
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.functional.OnStartDragListener
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentHomeBinding
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateProfile
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnBackPressedListener, OnStartDragListener {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var setting: SettingSharedPreferences
    @Inject lateinit var updateProfile: UpdateProfile
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var homeListener: HomeListener

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var touchHelper: ItemTouchHelper

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun layoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        Dlog.d( "onCreate HomeFragment")

        homeViewModel = viewModel(viewModelFactory){
            observe(homeList, ::renderHomeList)
            failure(failure, ::handleFailure)
        }
    /*
        // TODO : 테스트 코드..
        Dlog.d("homeFragment interstitialCount : ${setting.interstitialCount}")
        if(setting.interstitialCount >= 2){
            // 테스트 코드
            val manager = ReviewManagerFactory.create(activity!!)
            //val manager = FakeReviewManager(activity!!)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { request1 ->
                if (request1.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = request1.result
                    Dlog.d("InApp reviewInfo : $reviewInfo")
                    Dlog.d("InApp request.isSuccessful")
                    val flow = manager.launchReviewFlow(activity!!, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Dlog.d("InApp addOnCompleteListener ")
                    }
                } else {
                    Dlog.d("InApp error")
                    // There was some problem, continue regardless of the result.
                }
            }
        }
        // 플레이 스토어 올려보고 되나 확인해봐야 될듯.
    */
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initializeHomeAdapter()
        initializeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

    // homeFragment 초기화 하는 함수
    private fun initializeView() {
        (requireActivity() as PacemakerActivity).getBinding().aPacemakerTvTitle.text = getString(R.string.apacemaker_tv_title_str)
        (requireActivity() as PacemakerActivity).getBinding().aPacemakerFloEdit.visible()
        if(!setting.isNightMode){
            // 화이트모드
            binding.fHomeFloatingActionBtn.setImageResource(R.drawable.fhome_img_fabtn_daytime)
            binding.fHomeFloatingActionBtn.supportBackgroundTintList =
                ContextCompat.getColorStateList(activity!!, R.color.blue_5C83CF)

            // divison imageView init
            initDivisionImageViews(setting.isNightMode)
            // divisin imageView select init
            settingDivisionImageViews(cur_division, setting.isNightMode)

        }else{
            // 다크모드
            binding.fHomeFloatingActionBtn.setImageResource(R.drawable.fhome_img_fabtn_nighttime)
            binding.fHomeFloatingActionBtn.supportBackgroundTintList =
                ContextCompat.getColorStateList(activity!!, R.color.orange_F74938)

            // divison imageview init
            initDivisionImageViews(setting.isNightMode)
            // divisin imageView select init
            settingDivisionImageViews(cur_division, setting.isNightMode)
        }

        // homeListener 초기화
        homeListener = HomeListener(activity!!, binding, (requireActivity() as PacemakerActivity).getBinding(), navigator, homeAdapter,
            this, homeViewModel, setting)
        // 클릭 리스너
        homeListener.clickListener()

        if(setting.height < 0 && setting.weight < 0) {
             showProfileDialog(activity!!, setting, String.empty(), GET_HEIGHT_WEIGHT, updateProfile)
        }

        // DB에 있는 데이터 로드
        homeViewModel.loadHomeList()
    }

    // homeAdapter 초기화 하는 함수
    private fun initializeHomeAdapter() {
        homeAdapter = HomeAdapter(this, context!!, setting, homeViewModel)
        val callback: ItemTouchHelper.Callback = HomeItemMoveCallbackListener(homeAdapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.fHomeRecyclerview)
        binding.fHomeRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.fHomeRecyclerview.adapter = homeAdapter
    }

    // 분할 이미지 뷰들 초기 셋팅
    // input이 isDarked로 false이면 화이트 모드, true이면 다크모드
    private fun initDivisionImageViews(isNightMode: Boolean){
        if(!isNightMode){
            // 화이트 모드
            binding.fHomeIvDivision1.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_1_uncliked_daytime, null))
            binding.fHomeIvDivision2.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_2_uncliked_daytime, null))
            binding.fHomeIvDivision3.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_3_uncliked_daytime, null))
        }else{
            // 다크 모드
            binding.fHomeIvDivision1.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_1_uncliked_nighttime, null))
            binding.fHomeIvDivision2.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_2_uncliked_nighttime, null))
            binding.fHomeIvDivision3.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_3_uncliked_nighttime, null))
        }
    }

    // 분할 관련 이미지 뷰들 클릭 이벤트 메소드
    // input은 0, 1, 2 분할 번호
    fun settingDivisionImageViews(number: Int, isNightMode: Boolean){
        initDivisionImageViews(isNightMode)

        when(number){
            0 -> {
                if(!isNightMode){
                    // 화이트 모드
                    binding.fHomeIvDivision1.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_1_cliked_daytime, null))
                }else{
                    // 다크 모드
                    binding.fHomeIvDivision1.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_1_cliked_nighttime, null))
                }
            }
            1 -> {
                if(!isNightMode){
                    // 화이트 모드
                    binding.fHomeIvDivision2.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_2_cliked_daytime, null))
                }else{
                    // 다크 모드
                    binding.fHomeIvDivision2.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_2_cliked_nighttime, null))
                }
            }
            2 -> {
                if(!isNightMode){
                    // 화이트 모드
                    binding.fHomeIvDivision3.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_3_cliked_daytime, null))
                }else{
                    // 다크 모드
                    binding.fHomeIvDivision3.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.fhome_img_division_3_cliked_nighttime, null))
                }
            }
            else -> {
                Dlog.d("settingDivisionImageViews error")
            }
        }
    }

    // edit image save로 바꾸는 함수.
    fun switchEditImage(isChecked: Boolean){
        if(isChecked) (requireActivity() as PacemakerActivity).getBinding().aPacemakerIvEdit.setImageResource(R.drawable.apacemaker_img_edit_save)
        else (requireActivity() as PacemakerActivity).getBinding().aPacemakerIvEdit.setImageResource(R.drawable.apacemaker_img_edit)
    }

    // Home 데이터들 갱신하는 함수.
    private fun renderHomeList(homeView: List<HomeView>?) {
        homeAdapter.collection = homeView.orEmpty()
    }

    // Home 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is HomeFailure.ListNotAvailable -> renderFailure(R.string.fhome_tv_list_unavailable_str)
            else -> Dlog.d( "error")
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        binding.fHomeRecyclerview.gone()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }

}