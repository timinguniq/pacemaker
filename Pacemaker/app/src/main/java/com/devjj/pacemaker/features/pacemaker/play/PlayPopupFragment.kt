package com.devjj.pacemaker.features.pacemaker.play

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.failure
import com.devjj.pacemaker.core.extension.invisible
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_play_popup.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class PlayPopupFragment : BaseFragment() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var playPopupAdapter: PlayPopupAdapter

    private lateinit var playPopupViewModel: PlayPopupViewModel

    // 현재 셋트 수를 나타내는 변수.
    private var currentSet: Int = 1
    private var maxSet: Int = 0

    // Timer 동작 모드
    private var mode = 0
    private val STOP_MODE = 0
    private val PROGRESS_MODE = 1
    //

    // 운동 간 휴식시간.
    private var interval = 0

    private var currentPlayPopupData: PlayPopupData = PlayPopupData.empty()

    override fun layoutId() = R.layout.fragment_play_popup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        playPopupViewModel = viewModel(viewModelFactory){
            observe(playPopupList, ::renderPlayPopupList)
            failure(failure, ::handleFailure)
        }

    }

    // 한번만 소환되는거 같다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        initializeView()
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
                    progressTimer()
                    if(interval == 0){
                        interval = 0
                        timer.cancel()

                        showSet()
                    }
                }
            }else{
                // progress_mode일 떄 이 코드를 수행한다.
                mode = STOP_MODE

                timer.cancel()

                showSet()
            }
        }
    }

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

    // 보드에 세트화면 셋팅하는 함수.
    private fun showSet(){
        if(maxSet > currentSet){
            currentSet++
            val currentPlayPopupView = PlayPopupView(currentPlayPopupData.id, currentPlayPopupData.part_img, currentPlayPopupData.name,
                currentPlayPopupData.mass, currentPlayPopupData.rep, currentPlayPopupData.set, currentPlayPopupData.interval,
                if(currentPlayPopupData.achivement) 1 else 0)
            showBoardSetting(currentPlayPopupView)
        }else{
            // maxSet <= currentSet
            currentPlayPopupData.achivement = true
            runBlocking {
                async {
                    playPopupViewModel.updateExercisePlayPopupData(currentPlayPopupData)
                }.join()
                async {
                    playPopupViewModel.loadPlayPopupList()
                }.join()
            }



            //playPopupViewModel.updateExercisePlayPopupData(currentPlayPopupData)
            //Thread.sleep(100)
            //playPopupViewModel.loadPlayPopupList()
        }
    }

    // 아래쪽 보드 셋팅하는 함수
    private fun showBoardSetting(currentPlayPopupView: PlayPopupView){
        mode = STOP_MODE
        fPlayPopup_tv_timer.setTextColor(Color.argb(255, 0, 0, 0))

        maxSet = currentPlayPopupView.set
        interval = currentPlayPopupView.interval
        val maxTime = settingFormatForTimer(interval)
        fPlayPopup_tv_set.text = "$currentSet/${maxSet} Set"
        fPlayPopup_tv_timer.text = maxTime

    }

    // 타이머 시간 셋팅하는 함수
    private fun settingFormatForTimer(time: Int): String{
        val min = if(time/60 < 10) "0${time/60}" else (time/60).toString()
        val sec = if(time%60 < 10) "0${time%60}" else (time%60).toString()
        val result: String = "$min:$sec"
        return result
    }

    // 타이머 바꿔주는 함수.
    private fun progressTimer(){
        if(interval <= 15){
            fPlayPopup_tv_timer?.setTextColor(Color.argb(255, 255, 0, 0))
        }else{
            fPlayPopup_tv_timer?.setTextColor(Color.argb(255, 0, 0, 0))
        }
        fPlayPopup_tv_timer?.text = settingFormatForTimer(interval)
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
