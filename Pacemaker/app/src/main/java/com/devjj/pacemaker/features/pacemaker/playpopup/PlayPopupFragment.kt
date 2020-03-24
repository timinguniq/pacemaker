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
    @Inject lateinit var playPopupAdapter: PlayPopupAdapter

    private lateinit var playPopupListener: PlayPopupListener

    private lateinit var playPopupViewModel: PlayPopupViewModel

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

        if(playPopupViewModel.getCurrentSet().value == null) {
            Log.d("test", "currentSet null")
            playPopupViewModel.initCurrentSet()
        }

        playPopupViewModel.getCurrentSet().observe(this, Observer<Int>{ currentSet->
            // update UI
            Log.d("test", "observer currentSet : ${currentSet}")
            //currentSet = currentSet
        })

    }

    // 한번만 소환되는거 같다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "onViewCreated")

    }

    override fun onResume() {
        super.onResume()
        //initializeView()

        isDarkMode = setting.isDarkMode

        // 모드에 따른 셋팅(ex 화이트모드, 다크모드)
        initSettingMode(isDarkMode)

        // playPopupListener 초기화
        playPopupListener = PlayPopupListener(activity!!, this, playPopupViewModel)

        // 클릭 리스너들 모아둔 함수.
        playPopupListener.clickListener()


        // DB에 있는 데이터 로드
        playPopupViewModel.loadPlayPopupList()

        // 테스트 코드
        marginPartImg(25)
        //
    }

    // playPopupFragment 초기화 하는 함수
    private fun initializeView() {
        fPlayPopup_rv.layoutManager = LinearLayoutManager(this.context)
        fPlayPopup_rv.adapter = playPopupAdapter

        // DB에 있는 데이터 로드
        playPopupViewModel.loadPlayPopupList()
        // Timer 연습코드
        var timer: TimerTask = Timer().schedule(100, 1000){}

        // continue 버튼을 눌렀을 떄 이벤트 함수.
        fPlayPopup_btn_continue.setOnClickListener {
            if(mode == STOP_MODE){
                // stop_mode일 떄 이 코드를 수행한다.
                mode = PROGRESS_MODE

                timer = Timer("TimerDown", false).schedule(100, 1000){
                    interval -= 1
                    runBlocking {
                        launch(Dispatchers.Main){
                            //progressTimer()
                        }
                    }
                    if(interval == 0){
                        interval = 0
                        timer.cancel()
                        // playPopupView 데이터를 업데이트 하는 함수.
                        updatePlayPopupView()

                        showSet()
                    }
                }
            }else{
                // progress_mode일 떄 이 코드를 수행한다.
                mode = STOP_MODE

                timer.cancel()

                // playPopupView 데이터를 업데이트 하는 함수.
                updatePlayPopupView()

                showSet()
            }
        }
    }

    // 뷰 모드에 대한 초기 셋팅
    private fun initSettingMode(isDarkMode: Boolean){
        /*
        listResource 목록
        0 -> 상태바 색깔
        1 -> 배경이미지
        2 -> 진행바 진행 안됐을 때 배경 이미지
        3 -> 운동 이름 색깔
        4 -> 무게랑 세트 색깔
        5 -> 저장 버튼 이미지
        6 -> 추가 시간 글자 색깔지
        7 -> 추가 시간 글자 배경 이미지
        8 -> 아래 배경 이미지
        9 -> 아래 다음 배경 이미
        */
        val listResource = mutableListOf<Int>()

        if(!isDarkMode){
            // 화이트모드
            listResource.add(wmStatusBarColor)
            listResource.add(R.drawable.fplaypopup_wm_bg)
            listResource.add(R.drawable.fplaypopup_wm_progress_unselect_bar)
            listResource.add(wmExerciseNameTextColor)
            listResource.add(wmMassSetTextColor)
            listResource.add(R.drawable.faddition_wm_save_img)
            listResource.add(wmUnderNextTextColor)
            listResource.add(R.drawable.fplaypopup_wm_plus_bg)
            listResource.add(R.drawable.fplaypopup_wm_under_bg)
            listResource.add(R.drawable.fplaypopup_wm_under_next_bg)
        }else{
            // 다크모드
            listResource.add(dmStatusBarColor)
            listResource.add(R.drawable.fplaypopup_dm_bg)
            listResource.add(R.drawable.fplaypopup_dm_progress_unselect_bar)
            listResource.add(dmExerciseNameTextColor)
            listResource.add(dmMassSetTextColor)
            listResource.add(R.drawable.faddition_dm_save_img)
            listResource.add(dmUnderNextTextColor)
            listResource.add(R.drawable.fplaypopup_dm_plus_bg)
            listResource.add(R.drawable.fplaypopup_dm_under_bg)
            listResource.add(R.drawable.fplaypopup_dm_under_next_bg)
        }

        activity?.window?.statusBarColor = listResource[0]
        fPlayPopup_clo_main.setBackgroundResource(listResource[1])
        for(view in progressBars){
            view.setBackgroundResource(listResource[2])
        }

        fPlayPopup_tv_name.setTextColor(listResource[3])
        fPlayPopup_tv_m_s.setTextColor(listResource[4])
        fPlayPopup_iv_confirm.setImageResource(listResource[5])
        fPlayPopup_btn_plus.setTextColor(listResource[6])
        fPlayPopup_btn_plus.setBackgroundResource(listResource[7])
        fPlayPopup_vi_under.setBackgroundResource(listResource[8])
        fPlayPopup_flo_next.setBackgroundResource(listResource[9])

        fPlayPopup_clo_next.visibility = View.GONE
    }

/*
    // playPopup 데이터들 갱신하는 함수.
    private fun renderPlayPopupList(playPopupView: List<PlayPopupView>?) {
        // 재생 목록이 없으면 화면 종료
        if(playPopupView!!.isEmpty()){
            Toast.makeText(context, "재생 목록이 없습니다.", Toast.LENGTH_LONG).show()
            activity?.finish()
        }
        //

        currentSet = 1

        playPopupAdapter.collection = playPopupView.orEmpty()

        var achivementCount = 0
        val playPopupDataList = mutableListOf<PlayPopupData>()
        for(popupView in playPopupView.orEmpty()){
            if(popupView.achivement == 0){
                val bAchivement = popupView.achivement != 0
                currentPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.set, popupView.interval, bAchivement)
                showBoardSetting(popupView)
                // 스크롤 자동이동.
                scrollRecyclerview(achivementCount)
                break
            }else{
                achivementCount++
                Log.d("test", "achivementCount : ${achivementCount}")
                Log.d("test", "playPopupView : ${playPopupView?.size}")

                val initPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.set, popupView.interval, false)
                playPopupDataList.add(initPlayPopupData)

                if(achivementCount == playPopupView?.size){
                    // TODO: view 종료 되고 historyDB로 저장되게 해야 될 것 같다.
                    for(a in playPopupDataList){
                        playPopupViewModel.updateExercisePlayPopupData(a)
                    }

                    Toast.makeText(context, "운동이 완료 되었습니다.", Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            }
        }
    }
*/

    // playPopup 데이터들 갱신하는 함수.
    private fun renderPlayPopupList(playPopupView: List<PlayPopupView>?) {
        // 재생 목록이 없으면 화면 종료
        if(playPopupView!!.isEmpty()){
            Toast.makeText(context, "재생 목록이 없습니다.", Toast.LENGTH_LONG).show()
            activity?.finish()
        }
        //
        Log.d("test", "renderPlayPopupList")
        currentSet = 1

        var achivementCount = 0
        val playPopupDataList = mutableListOf<PlayPopupData>()
        for(popupView in playPopupView.orEmpty()){
            if(popupView.achivement == 0){
                val bAchivement = popupView.achivement != 0
                currentPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.set, popupView.interval, bAchivement)
                showInitSetting(popupView)

                break
            }else{
                achivementCount++
                Log.d("test", "achivementCount : ${achivementCount}")
                Log.d("test", "playPopupView : ${playPopupView?.size}")

                val initPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.set, popupView.interval, false)
                playPopupDataList.add(initPlayPopupData)

                if(achivementCount == playPopupView?.size){
                    // TODO: view 종료 되고 historyDB로 저장되게 해야 될 것 같다.
                    for(a in playPopupDataList){
                        playPopupViewModel.updateExercisePlayPopupData(a)
                    }

                    Toast.makeText(context, "운동이 완료 되었습니다.", Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            }
        }
    }

    // 스크롤 자동 이동하는 함수
    private fun scrollRecyclerview(position: Int){
        Log.d("test", "scroll achivementCount position : ${position}")
        //fPlayPopup_rv.layoutManager?.scrollToPosition(position)
        fPlayPopup_rv.layoutManager?.scrollToPosition(0)

        Handler().post {
            when(position){
                0 -> fPlayPopup_rv.layoutManager?.scrollToPosition(position)
                in 1..(playPopupAdapter.itemCount-3) -> fPlayPopup_rv.layoutManager?.scrollToPosition(position + 2)
                in (playPopupAdapter.itemCount-2)..(playPopupAdapter.itemCount) -> fPlayPopup_rv.layoutManager?.scrollToPosition(playPopupAdapter.itemCount - 1)
            }
        }
    }

    // playPopupView 데이터 업데이트하는 함수
    private fun updatePlayPopupView(){
        val playPopupViewList = playPopupViewModel.playPopupList.value

        for(a in playPopupViewList.orEmpty()){
            Log.d("test", "playPopupList a : ${a.set}")
        }
    }

    // 보드에 세트화면 셋팅하는 함수.
    fun showSet(){
        if(maxSet > currentSet){
            // 현재 세트에 1을 더하는 함수.
            currentSet++
            val currentPlayPopupView = PlayPopupView(currentPlayPopupData.id, currentPlayPopupData.part_img, currentPlayPopupData.name,
                currentPlayPopupData.mass, currentPlayPopupData.rep, currentPlayPopupData.set, currentPlayPopupData.interval,
                if(currentPlayPopupData.achivement) 1 else 0)
            showInitSetting(currentPlayPopupView)
        }else{
            // maxSet <= currentSet
            currentPlayPopupData.achivement = true
            playPopupViewModel.updateExercisePlayPopupData(currentPlayPopupData)
            playPopupViewModel.loadPlayPopupList()
        }
    }

    // 초기 운동 화면을 셋팅하는 함수.
    private fun showInitSetting(currentPlayPopupView: PlayPopupView){
        mode = STOP_MODE
        Log.d("test", "showBoardSetting")
        // 최대 셋 설정하는 코드
        var playPopupViewSet = currentPlayPopupView.set
        maxSet = playPopupViewSet
        //

        Log.d("test", "showBoardSetting currentSet $currentSet")
        Log.d("test", "showBoardSetting maxSet $maxSet")
        // progressBar 셋팅하는 코드
        isVisibleProgressBars(playPopupViewSet)
        settingNextProgressBars(currentSet)
        //

        // 근육 부위 화면에 셋팅하는 코드
        var partImgResources = convertPartImgToResource(currentPlayPopupView.part ,isDarkMode)
        fPlayPopup_iv_part_img.setImageResource(partImgResources)
        //

        fPlayPopup_tv_name.text = currentPlayPopupView.name
        var slash = getString(R.string.fPlayPopup_slash)
        var massUnit = getString(R.string.fPlayPopup_mass_unit)
        var setUnit = getString(R.string.fPlayPopup_set_unit)
        var mstxv = currentPlayPopupView.mass.toString() + massUnit + " $slash " +
                currentPlayPopupView.set.toString() + setUnit
        fPlayPopup_tv_m_s.text = mstxv

        interval = currentPlayPopupView.interval
        val timerText = settingFormatForTimer(interval)
        fPlayPopup_tv_rest_time.text = timerText


        /*
        fPlayPopup_tv_timer?.setTextColor(Color.argb(255, 0, 0, 0))

        playPopupViewModel.setCurrentSet(currentSet)
        Log.d("test", "playPopupViewModel showBoardSetting currentSet : ${currentSet}")
        maxSet = currentPlayPopupView.set
        interval = currentPlayPopupView.interval
        val maxTime = settingFormatForTimer(interval)
        fPlayPopup_tv_set?.text = "$currentSet/${maxSet} Set"
        fPlayPopup_tv_timer?.text = maxTime
        */

    }

    //

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
        val resourcesSelect = if(!isDarkMode) R.drawable.fplaypopup_wm_progress_select_bar
                         else R.drawable.fplaypopup_dm_progress_select_bar

        val resourcesUnSelect = if(!isDarkMode) R.drawable.fplaypopup_wm_progress_unselect_bar
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
/*
    // 타이머 바꿔주는 함수.
    private fun progressTimer(){
        fPlayPopup_tv_rest_time?.text = settingFormatForTimer(interval)
    }
*/

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
