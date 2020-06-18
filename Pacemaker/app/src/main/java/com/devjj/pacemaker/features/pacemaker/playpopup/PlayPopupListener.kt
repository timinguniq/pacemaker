package com.devjj.pacemaker.features.pacemaker.playpopup

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.invisible
import com.devjj.pacemaker.core.extension.visible
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.features.pacemaker.addition.AdditionViewModel
import com.devjj.pacemaker.features.pacemaker.dialog.showGiveUpAllExerciseDialog
import com.devjj.pacemaker.features.pacemaker.dialog.showGiveUpExerciseDialog
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import kotlinx.android.synthetic.main.fragment_play_popup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.schedule

class PlayPopupListener(val activity: Activity, val playPopupFragment: PlayPopupFragment,
                        val playPopupViewModel: PlayPopupViewModel, val navigator: Navigator) {

    val handler = Handler(Looper.getMainLooper())

    fun clickListener() {
        // 백키를 눌렀을 떄 리스너
        activity.fPlayPopup_flo_back.setOnClickListener {
            mode = STOP_MODE
            TimerService.setProgressTimer(false)
            TimerService.stopService(activity)
            activity.finish()
        }

        // 확인 키를 눌렀을 때 리스너
        activity.fPlayPopup_iv_confirm.setOnClickListener {
            if(isFinalExerciseFinalSet){
                activity.fPlayPopup_flo_next.callOnClick()
                return@setOnClickListener
            }

            mode = PROGRESS_MODE

            val restText = activity.getString(R.string.fplaypopup_tv_rest_str)
            handler.post {
                playPopupFragment.settingExerciseName(restText)

                playPopupFragment.settingForMode()

                // margin
                playPopupFragment.marginPartImg(25)
                //
                for( progressBar in playPopupFragment.progressBars){
                    progressBar.clearAnimation()
                }
            }
            // plus number init
            plusClickNumber = 0

            // 타이머 시작
            TimerService.timerStart(activity)


        }

        // Next 버튼 눌렀을 떄. 이벤트 함수
        activity.fPlayPopup_flo_next.setOnClickListener{
            mode = STOP_MODE
            TimerService.timerStop()

            handler.post{
                playPopupFragment.settingForMode()

                playPopupFragment.showSet()

                playPopupFragment.marginPartImg(0)
            }

        }

        // +10초 눌렀을 때수 이벤트 함수
        activity.fPlayPopup_flo_plus.setOnClickListener {
            Dlog.d("interval : $interval")

            if(plusClickNumber <= maxPlusClickNumber) plusClickNumber++
            if(plusClickNumber <= maxPlusClickNumber&& !timerFinish){
                interval+=plusInterval

                val currentValue = activity.fPlayPopup_cv_rate.currentValue
                playPopupFragment.circleViewAnimation(currentValue, (interval*1000).toLong())
            }

            handler.post {
                // 휴식 시간 타이머 시간 조정하는 함수
                if(!timerFinish)
                    playPopupFragment.settingRestTimeTv()
            }
        }

        // 오른쪽 화살 이미지 눌렀을 때 이벤트 함수
        activity.fPlayPopup_flo_right_arrow.setOnClickListener {
            showGiveUpExerciseDialog(activity, isNightMode, playPopupViewModel, currentPlayPopupData)
        }

        // 왼쪽 화살 이미지 눌렀을 때 이벤트 함수
        activity.fPlayPopup_flo_left_arrow.setOnClickListener {
            showGiveUpAllExerciseDialog(activity, isNightMode, playPopupViewModel, allPlayPopupDataList)
        }
    }
}