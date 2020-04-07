package com.devjj.pacemaker.features.pacemaker.playpopup

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.devjj.pacemaker.core.extension.invisible
import com.devjj.pacemaker.core.extension.visible
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.features.pacemaker.addition.AdditionViewModel
import kotlinx.android.synthetic.main.fragment_temp_play_popup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.schedule

class PlayPopupListener(val activity: Activity, val playPopupFragment: PlayPopupFragment,
                        val playPopupViewModel: PlayPopupViewModel, val navigator: Navigator) {

    // Timer 연습코드
    var timer: TimerTask = Timer().schedule(100, 1000){}

    fun clickListener() {
        // 백키를 눌렀을 떄 리스너
        activity.fPlayPopup_flo_back.setOnClickListener {
            activity.finish()
        }

        // 확인 키를 눌렀을 때 리스너
        activity.fPlayPopup_iv_confirm.setOnClickListener {
            activity.fPlayPopup_clo_confirm.invisible()
            activity.fPlayPopup_clo_next.visible()

            // margin
            playPopupFragment.marginPartImg(0)
            //

            // plus number init
            plusClickNumber = 0

            mode = PROGRESS_MODE

            timer = Timer("TimerDown", false).schedule(100, 1000){
                interval -= 1
                runBlocking {
                    launch(Dispatchers.Main){
                        activity.fPlayPopup_tv_rest_time?.text = settingFormatForTimer(interval)
                    }
                }

                if(interval == 0){
                    interval = 0
                    timer.cancel()
                    // TODO : 진동이나 소리로 알려줘야 될 것 같다.

                }
            }

        }

        // Next 버튼 눌렀을 떄. 이벤트 함수
        activity.fPlayPopup_flo_next.setOnClickListener{
            timer.cancel()

            val handler = Handler(Looper.getMainLooper())
            handler.post{
                activity.fPlayPopup_clo_confirm.visible()
                activity.fPlayPopup_clo_next.invisible()

                playPopupFragment.showSet()
            }

            playPopupFragment.marginPartImg(25)
        }

        // +10초 눌렀을 때수 이벤트 함수
        activity.fPlayPopup_flo_plus.setOnClickListener {
            Log.d("test", "study cafe")
            if(plusClickNumber <= maxPlusClickNumber) plusClickNumber++
            if(plusClickNumber <= maxPlusClickNumber) interval+=plusInterval
        }

        // 오른쪽 화살 이미지 눌렀을 때 이벤트 함수
        activity.fPlayPopup_flo_right_arrow.setOnClickListener {
            navigator.showGiveUpExerciseDialog(activity, isNightMode, playPopupViewModel, currentPlayPopupData)
        }

        // 왼쪽 화살 이미지 눌렀을 때 이벤트 함수
        activity.fPlayPopup_flo_left_arrow.setOnClickListener {
            navigator.showGiveUpAllExerciseDialog(activity, isNightMode, playPopupViewModel, allPlayPopupDataList)
        }

/*
        if(mode == STOP_MODE){
            // stop_mode일 떄 이 코드를 수행한다.
            mode = PROGRESS_MODE

            timer = Timer("TimerDown", false).schedule(100, 1000){
                interval -= 1
                runBlocking {
                    launch(Dispatchers.Main){
                        progressTimer()
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
*/
    }


}