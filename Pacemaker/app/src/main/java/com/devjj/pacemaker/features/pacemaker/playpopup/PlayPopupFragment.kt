package com.devjj.pacemaker.features.pacemaker.playpopup

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.addition.AdditionListener
import kotlinx.android.synthetic.main.dialog_profile_input.view.*
import kotlinx.android.synthetic.main.fragment_play_popup.*
import kotlinx.android.synthetic.main.fragment_temp_play_popup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class PlayPopupFragment : BaseFragment() {
    @Inject lateinit var setting: SettingSharedPreferences
    @Inject lateinit var navigator: Navigator

    private lateinit var playPopupListener: PlayPopupListener

    private lateinit var playPopupViewModel: PlayPopupViewModel

    private var totalTimer: TimerTask = Timer().schedule(100, 1000){}

    // 진행바들 변수 리스트
    private val progressBars: List<View> by lazy{
        listOf(
            fPlayPopup_vi_progress_10, fPlayPopup_vi_progress_9, fPlayPopup_vi_progress_8, fPlayPopup_vi_progress_7,
            fPlayPopup_vi_progress_6, fPlayPopup_vi_progress_5, fPlayPopup_vi_progress_4, fPlayPopup_vi_progress_3,
            fPlayPopup_vi_progress_2, fPlayPopup_vi_progress_1)
    }

    override fun layoutId() = R.layout.fragment_temp_play_popup

    //override fun layoutId() = R.layout.fragment_temp_play_popup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        Log.d("test", "onCreate PlayPopupFragment")
        playPopupViewModel = viewModel(viewModelFactory){
            observe(playPopupList, ::renderPlayPopupList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        //initializeView()

        isNightMode = setting.isNightMode

        // 모드에 따른 셋팅(ex 화이트모드, 다크모드)
        initSettingMode(isNightMode)

        // playPopupListener 초기화
        playPopupListener = PlayPopupListener(activity!!, this, playPopupViewModel, navigator)

        // 클릭 리스너들 모아둔 함수.
        playPopupListener.clickListener()

        // DB에 있는 데이터 로드
        playPopupViewModel.loadPlayPopupList()

        // 초기 마진 셋팅
        marginPartImg(25)

        totalTime = 0

        // totalTimer 시작시키는 함수.
        totalTimer()

        // 테스트 코드
        fPlayPopup_cv_rate.setValue(0f)
        //

    }

    override fun onStop() {
        super.onStop()
        totalTimer.cancel()
    }

    // 뷰 모드에 대한 초기 셋팅
    private fun initSettingMode(isNightMode: Boolean){
        /*
        listResource 목록
        0 -> 상태바 색깔
        1 -> 배경이미지
        2 -> 진행바 진행 안됐을 때 배경 이미지
        3 -> 부위 나오는 바깥원 rim 색깔
        4 -> 부위 나오는 바깥원 bar 색깔
        5 -> 운동 이름 색깔
        6 -> 무게랑 세트 색깔
        7 -> 저장 버튼 이미지
        8 -> 추가 시간 글자 색깔지
        9 -> 추가 시간 글자 배경 이미지
        10 -> 아래 배경 이미지
        11 -> 아래 다음 배경 이미지
        */
        val listResource = mutableListOf<Int>()

        if(!isNightMode){
            // 화이트모드
            listResource.add(loadColor(activity!!, R.color.blue_bg_thick))
            listResource.add(R.drawable.fplaypopup_wm_bg)
            listResource.add(R.drawable.fplaypopup_wm_progress_unselect_bar)
            listResource.add(loadColor(activity!!, R.color.blue_bg_transparent))
            listResource.add(loadColor(activity!!, R.color.blue_bg_basic))
            listResource.add(loadColor(activity!!, R.color.black_txt_thick))
            listResource.add(loadColor(activity!!, R.color.grey_txt_light))
            listResource.add(R.drawable.faddition_wm_save_img)
            listResource.add(loadColor(activity!!, R.color.blue_bg_thick))
            listResource.add(R.drawable.fplaypopup_wm_plus_bg)
            listResource.add(R.drawable.fplaypopup_wm_under_bg)
            listResource.add(R.drawable.fplaypopup_wm_under_next_bg)
        }else{
            // 다크모드
            listResource.add(loadColor(activity!!, R.color.grey_bg_thickest))
            listResource.add(R.drawable.fplaypopup_dm_bg)
            listResource.add(R.drawable.fplaypopup_dm_progress_unselect_bar)
            listResource.add(loadColor(activity!!, R.color.orange_bg_light))
            listResource.add(loadColor(activity!!, R.color.orange_bg_thick))
            listResource.add(loadColor(activity!!, R.color.white))
            listResource.add(loadColor(activity!!, R.color.grey_txt_thick))
            listResource.add(R.drawable.faddition_dm_save_img)
            listResource.add(loadColor(activity!!, R.color.orange_bg_thick))
            listResource.add(R.drawable.fplaypopup_dm_plus_bg)
            listResource.add(R.drawable.fplaypopup_dm_under_bg)
            listResource.add(R.drawable.fplaypopup_dm_under_next_bg)
        }

        activity?.window?.statusBarColor = listResource[0]
        fPlayPopup_clo_main.setBackgroundResource(listResource[1])
        for(view in progressBars){
            view.setBackgroundResource(listResource[2])
        }
        fPlayPopup_cv_rate.rimColor = listResource[3]
        fPlayPopup_cv_rate.setBarColor(listResource[4])
        fPlayPopup_tv_name.setTextColor(listResource[5])
        fPlayPopup_tv_m_s.setTextColor(listResource[6])
        fPlayPopup_iv_confirm.setImageResource(listResource[7])
        fPlayPopup_tv_plus.setTextColor(listResource[8])
        fPlayPopup_tv_plus.setBackgroundResource(listResource[9])
        fPlayPopup_vi_under.setBackgroundResource(listResource[10])
        fPlayPopup_flo_next.setBackgroundResource(listResource[11])

        fPlayPopup_clo_next.visibility = View.GONE
    }

    // playPopup 데이터들 갱신하는 함수.
    private fun renderPlayPopupList(playPopupView: List<PlayPopupView>?) {
        // 재생 목록이 없으면 화면 종료
        if(playPopupView!!.isEmpty()){
            Toast.makeText(context, R.string.fPlayPopup_ts_playlist_not_exist, Toast.LENGTH_LONG).show()
            activity?.finish()
        }
        //
        Log.d("test", "renderPlayPopupList")

        // 전체 포기를 위한 초기화 데이터 리스트에 추가
        for(popupView in playPopupView.orEmpty()){
            var playPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                popupView.rep, popupView.setGoal, popupView.setDone, popupView.interval, popupView.achievement == 1)
            allPlayPopupDataList.add(playPopupData)
        }
        //

        currentSet = 1

        var achivementCount = 0
        var playPopupDataList = mutableListOf<PlayPopupData>()
        for(popupView in playPopupView.orEmpty()){
            val achivement = popupView.achievement == 1
            if(!achivement){
                currentPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.setGoal, popupView.setDone, popupView.interval, popupView.achievement == 1)
                showInitSetting(popupView)

                break
            }else{
                achivementCount++
                Log.d("test", "achivementCount : ${achivementCount}")
                Log.d("test", "playPopupView : ${playPopupView.size}")

                val initPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.setGoal, popupView.setDone, popupView.interval,
                    popupView.achievement == 1
                )
                playPopupDataList.add(initPlayPopupData)

                if(achivementCount == playPopupView?.size){
                    // 토탈 시간 측정하는 타이머 중지
                    totalTimer.cancel()

                    // 몸무게랑 키 입력하는 다이얼 로그 띄우는 함수.
                    navigator.showProfileDialog(activity!!, isNightMode, playPopupViewModel, playPopupDataList)
                    //showDialogProfile()

                    Log.d("test", "totalTimer : $totalTime")

                    Toast.makeText(context, R.string.fPlayPopup_ts_exercise_finish, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // 보드에 세트화면 셋팅하는 함수.
    fun showSet(){
        if(maxSet > currentSet){
            // 현재 세트에 1을 더하는 함수.
            currentSet++
            val currentPlayPopupView = PlayPopupView(currentPlayPopupData.id, currentPlayPopupData.part_img, currentPlayPopupData.name,
                currentPlayPopupData.mass, currentPlayPopupData.rep, currentPlayPopupData.setGoal,
                currentPlayPopupData.setDone, currentPlayPopupData.interval,
                if(currentPlayPopupData.achievement) 1 else 0)
            showInitSetting(currentPlayPopupView)

            // setDone 업데이트 해주는 코드
            currentPlayPopupData.setDone = currentSet
            playPopupViewModel.updateExercisePlayPopupData(currentPlayPopupData)
        }else{
            // maxSet <= currentSet
            currentPlayPopupData.setDone = currentSet
            currentPlayPopupData.achievement = true
            playPopupViewModel.updateExercisePlayPopupData(currentPlayPopupData)
            playPopupViewModel.loadPlayPopupList()
        }
    }

    // 초기 운동 화면을 셋팅하는 함수.
    private fun showInitSetting(currentPlayPopupView: PlayPopupView){
        mode = STOP_MODE
        Log.d("test", "showBoardSetting")
        // 최대 셋 설정하는 코드
        var playPopupViewSet = currentPlayPopupView.setGoal
        maxSet = playPopupViewSet
        //

        Log.d("test", "showBoardSetting currentSet $currentSet")
        Log.d("test", "showBoardSetting maxSet $maxSet")
        // progressBar 셋팅하는 코드
        isVisibleProgressBars(playPopupViewSet)
        settingNextProgressBars(currentSet)
        //

        // Circle 화면에 표시하는 코드
        val circleProgress = (100 * currentSet / maxSet).toFloat()
        fPlayPopup_cv_rate.setValue(circleProgress)
        //

        // 근육 부위 화면에 셋팅하는 코드
        var partImgResources = convertPartImgToResource(currentPlayPopupView.part ,isNightMode)
        fPlayPopup_iv_part_img.setImageResource(partImgResources)
        //

        fPlayPopup_tv_name.text = currentPlayPopupView.name
        var slash = getString(R.string.fPlayPopup_slash)
        var massUnit = getString(R.string.fPlayPopup_mass_unit)
        var setUnit = getString(R.string.fPlayPopup_set_unit)
        var mstxv = currentPlayPopupView.mass.toString() + massUnit + " $slash " +
                currentPlayPopupView.setGoal.toString() + setUnit
        fPlayPopup_tv_m_s.text = mstxv

        interval = currentPlayPopupView.interval
        val timerText = settingFormatForTimer(interval)
        fPlayPopup_tv_rest_time.text = timerText

        // 세트가 마지막 세트로 왔을 때 휴식 시간을 운동간 휴식시간으로 셋팅하기 위한 코드
        if(currentSet == maxSet){
            interval = setting.restTime
            val restTimeText = settingFormatForTimer(interval)
            fPlayPopup_tv_rest_time.text = restTimeText
        }
        //

    }

    // totalTime 시간 늘리는 Timer
    private fun totalTimer(){
        totalTimer = Timer("TotalTimer", false).schedule(60000, 60000){
            runBlocking {
                launch(Dispatchers.Main){
                    totalTime++
                }
            }
        }
    }

    // 부위 이미지 마진주는 함수
    fun marginPartImg(margin: Int){
        val params = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // dp값 구하는 함수.
        val topMarginInt = getPixelValue(context!!, margin)
        params.setMargins(topMarginInt, topMarginInt, topMarginInt, topMarginInt)
        fPlayPopup_iv_part_img.layoutParams = params
    }

    // progressbar setting(초기 progressBar setting)
    // input값은 총 갯수
    private fun isVisibleProgressBars(count: Int){
        val inVisibleCount = 10 - count

        val handler = Handler(Looper.getMainLooper())

        for(index in 0..9){
            handler.post {
                progressBars[index].visible()
            }
        }

        for(index in 0 until inVisibleCount){
            handler.post{
                progressBars[index].invisible()
            }
        }
    }

    // progressbar 진행 셋팅하는 함수
    // input은 현재 set
    private fun settingNextProgressBars(currentCount: Int){
        val range = 9 downTo (10-currentCount)
        val resourcesSelect = if(!isNightMode) R.drawable.fplaypopup_wm_progress_select_bar
                         else R.drawable.fplaypopup_dm_progress_select_bar

        val resourcesUnSelect = if(!isNightMode) R.drawable.fplaypopup_wm_progress_unselect_bar
                        else R.drawable.fplaypopup_dm_progress_unselect_bar

       val handler = Handler(Looper.getMainLooper())

        for(index in 0..9){
            handler.post{
                progressBars[index].setBackgroundResource(resourcesUnSelect)
            }
        }

        for(index in range){
            handler.post{
                progressBars[index].setBackgroundResource(resourcesSelect)
            }
        }
    }

    // playPopup 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is PlayPopupFailure.ListNotAvailable -> renderFailure(R.string.fPlayPopup_list_unavailable)
            else -> Log.d("PlayPopupFragment", "error")
        }
    }


    private fun renderFailure(@StringRes message: Int) {
        fPlayPopup_rv.invisible()

        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }
}
