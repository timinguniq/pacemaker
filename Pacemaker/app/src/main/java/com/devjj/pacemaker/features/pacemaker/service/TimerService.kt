package com.devjj.pacemaker.features.pacemaker.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.*
import android.widget.RemoteViews
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.databinding.FragmentPlayPopupBinding
import com.devjj.pacemaker.features.pacemaker.PlayPopupActivity
import com.devjj.pacemaker.features.pacemaker.playpopup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Singleton
import kotlin.concurrent.schedule

@Singleton
class TimerService : Service() {

    private val channelId = "channelId"

    override fun onBind(intent: Intent?): IBinder? {
        // TODO("Return the communication channel to the service.")
        //super.onBind(intent)
        throw UnsupportedOperationException("Not yet")
    }

    companion object {
        // 타이머 처음 지연 시간(0.1초)
        private val timerStartDelay: Long = 100
        // 타이머 간격시간 (1초)
        private val timerInterval: Long = 1000
        // 타이머 이름
        private val timerName = "countTimer"
        // 진동 지속 시간
        private val vibPeriod: Long = 1300

        private lateinit var vib: Vibrator

        private lateinit var pm: PowerManager

        private lateinit var w1: PowerManager.WakeLock

        private fun startPowerManager() = w1.acquire(6 * 60 * 1000L /* 6 minutes */)

        private fun endPowerManager() {
            if (w1.isHeld) w1.release() else Dlog.d( "w1.isHeld false")
        }

        fun startService(context: Context) {
            val startIntent = Intent(context, TimerService::class.java)
            context.startService(startIntent)
            vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLockTag = context.getString(R.string.stimer_tv_tag_str)
            w1 = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                wakeLockTag
            ) as PowerManager.WakeLock
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, TimerService::class.java)
            context.stopService(stopIntent)
        }

        private var timer = Timer(timerName, false).schedule(timerStartDelay, timerInterval) {}

        // timer가 진행중인지 나타내는 변수
        private var isTimerProgress: Boolean = false

        fun timerStart(activity: Activity, binding: FragmentPlayPopupBinding) {
            timerFinish = false
            startPowerManager()
            timer.cancel()

            timer = Timer(timerName, false).schedule(timerStartDelay, timerInterval) {
                interval -= 1
                Dlog.d( "timerStart interval : $interval")
                runBlocking {
                    launch(Dispatchers.Main) {
                        binding.fPlayPopupTvRestTime?.text = settingFormatForTimer(interval)
                    }
                }

                if (interval <= 0) {
                    interval = 0
                    binding.fPlayPopupTvRestTime?.text = settingFormatForTimer(interval)
                    timer.cancel()
                    isTimerProgress = false
                    endPowerManager()

                    timerFinish = true
                    // 진동하는 코드
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val audioAttributes = AudioAttributes.Builder().build()
                        vib.vibrate(
                            VibrationEffect.createOneShot(vibPeriod, VibrationEffect.DEFAULT_AMPLITUDE),
                            audioAttributes
                        )
                    } else {
                        vib.vibrate(vibPeriod)
                    }
                    //
                }
            }

            if (!isTimerProgress)
                timer.scheduledExecutionTime()

            isTimerProgress = true
        }

        fun timerStop() {
            timer.cancel()
            isTimerProgress = false
            mode = STOP_MODE
            endPowerManager()
        }

        // Timer가 진행중인지 확인하는 함수
        fun isProgressTimer() = isTimerProgress

        // Timer 진행여부 셋팅
        fun setProgressTimer(isProgress: Boolean) {
            isTimerProgress = isProgress
        }
    }

    override fun onCreate() {
        super.onCreate()

        startNotification()
    }

    // 처음 시작되면 호출되는 함수.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
        endPowerManager()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    // 채널을 등록하는 함수.
    private fun channelRegister() {
        val channelName = "service channel name"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // foreground 시작하는 함수.
    private fun startNotification() {
        channelRegister()

        // PendingIntent 입니다.
        val contentIntent =
            PendingIntent.getActivity(this, 0, Intent(this, PlayPopupActivity::class.java), 0)

        // Foreground Service의 layout입니다.
        val view = RemoteViews(packageName, R.layout.service_timer)

        // notification 셋팅
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)  // 아이콘 셋팅
                .setCustomContentView(view)       // 레이아웃 셋팅
                .setContentIntent(contentIntent)  // pendingIntent 클릭시 화면 전환을 위해
                .build()
        } else {
            // TODO("VERSION.SDK_INT < O")
            Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // 아이콘 셋팅
                .setContent(view)                 // 레이아웃 셋팅
                .setContentIntent(contentIntent)  // pendingIntent 클릭시 화면 전환을 위해
                .build()
        }

        // Foreground 시작하는 코드
        startForeground(1, notification)

    }
}


