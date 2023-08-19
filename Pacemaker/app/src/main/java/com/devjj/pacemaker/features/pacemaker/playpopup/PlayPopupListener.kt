package com.devjj.pacemaker.features.pacemaker.playpopup

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.databinding.FragmentPlayPopupBinding
import com.devjj.pacemaker.features.pacemaker.dialog.showGiveUpAllExerciseDialog
import com.devjj.pacemaker.features.pacemaker.dialog.showGiveUpExerciseDialog
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import java.util.*

class PlayPopupListener(val activity: Activity, val binding: FragmentPlayPopupBinding, val playPopupFragment: PlayPopupFragment,
                        val playPopupViewModel: PlayPopupViewModel, val navigator: Navigator) {

    val handler = Handler(Looper.getMainLooper())

    fun clickListener() {
        // 백키를 눌렀을 떄 리스너
        binding.fPlayPopupFloBack.setOnClickListener {
            mode = STOP_MODE
            TimerService.setProgressTimer(false)
            TimerService.stopService(activity)
            activity.finish()
        }

        // 확인 키를 눌렀을 때 리스너
        binding.fPlayPopupIvConfirm.setOnClickListener {
            if(isFinalExerciseFinalSet){
                binding.fPlayPopupFloNext.callOnClick()
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
            TimerService.timerStart(activity, binding)


        }

        // Next 버튼 눌렀을 떄. 이벤트 함수
        binding.fPlayPopupFloNext.setOnClickListener{
            mode = STOP_MODE
            TimerService.timerStop()

            handler.post{
                playPopupFragment.settingForMode()

                playPopupFragment.showSet()

                playPopupFragment.marginPartImg(0)
            }

        }

        // +10초 눌렀을 때수 이벤트 함수
        binding.fPlayPopupFloPlus.setOnClickListener {
            Dlog.d("interval : $interval")

            if(plusClickNumber <= maxPlusClickNumber) plusClickNumber++
            if(plusClickNumber <= maxPlusClickNumber&& !timerFinish){
                // 진행된 인터벌
                var progressInterval = intervalMax - interval

                interval+=plusInterval
                intervalMax+=plusInterval

                var currentValue = (progressInterval/((intervalMax).toFloat()))*100
                val currentCircleViewValue = binding.fPlayPopupCvRate.currentValue
                Dlog.d("currentValue : $currentValue")
                if(currentCircleViewValue <= currentValue){
                    currentValue = currentCircleViewValue
                }

                if(currentValue <= 0)
                    currentValue = 0f

                handler.post {
                    playPopupFragment.circleViewAnimation(currentValue, (interval*1000).toLong())
                }
            }

            handler.post {
                // 휴식 시간 타이머 시간 조정하는 함수
                if(!timerFinish)
                    playPopupFragment.settingRestTimeTv()
            }
        }

        // 오른쪽 화살 이미지 눌렀을 때 이벤트 함수
        binding.fPlayPopupFloRightArrow.setOnClickListener {
            showGiveUpExerciseDialog(activity, isNightMode, playPopupViewModel, currentPlayPopupData)
        }

        // 왼쪽 화살 이미지 눌렀을 때 이벤트 함수
        binding.fPlayPopupFloLeftArrow.setOnClickListener {
            showGiveUpAllExerciseDialog(activity, isNightMode, playPopupViewModel, allPlayPopupDataList)
        }
    }
}