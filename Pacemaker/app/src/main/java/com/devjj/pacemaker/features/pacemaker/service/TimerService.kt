package com.devjj.pacemaker.features.pacemaker.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.*
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.devjj.pacemaker.R
import com.devjj.pacemaker.features.pacemaker.PlayPopupActivity
import com.devjj.pacemaker.features.pacemaker.playpopup.*
import kotlinx.android.synthetic.main.fragment_play_popup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.AccessController.getContext
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class TimerService : Service() {

    private val channelId = "channelId"


    override fun onBind(intent: Intent?): IBinder? {
        // TODO("Return the communication channel to the service.")
        //super.onBind(intent)
        throw UnsupportedOperationException("Not yet")
    }

    companion object {

        private lateinit var vib: Vibrator

        fun startService(context: Context) {
            val startIntent = Intent(context, TimerService::class.java)
            context.startService(startIntent)
            vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, TimerService::class.java)
            context.stopService(stopIntent)
        }

        private var timer = Timer("timer", false).schedule(100, 1000) {}

        // timer가 진행중인지 나타내는 변수
        private var isTimerProgress: Boolean = false

        fun timerStart(activity: Activity) {
            isTimerProgress = true

            timer = Timer("timer", false).schedule(100, 1000) {
                //doSomethingTimer()
                interval -= 1
                Log.d("test", "timerStart interval : $interval")
                runBlocking {
                    launch(Dispatchers.Main) {
                        activity.fPlayPopup_tv_rest_time?.text = settingFormatForTimer(interval)
                    }
                }

                if (interval <= 0) {
                    interval = 0
                    activity.fPlayPopup_tv_rest_time?.text = settingFormatForTimer(interval)
                    timer.cancel()
                    isTimerProgress = false
                    mode = STOP_MODE
                    // TODO : 진동이나 소리로 알려줘야 될 것 같다.

                    //
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val audioAttributes = AudioAttributes.Builder().build()
                        vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE),
                            audioAttributes)
                    }else{
                        vib.vibrate(200)
                    }
                    //
                }
            }

            if(!isTimerProgress)
                timer.scheduledExecutionTime()
        }

        fun timerStop() {
            timer.cancel()
            isTimerProgress = false
            mode = STOP_MODE
        }

        // Timer가 진행중인지 확인하는 함수
        fun isProgressTimer() = isTimerProgress

        // Timer 진행여부 셋팅
        fun setProgressTimer(isProgress: Boolean){
            isTimerProgress = isProgress
        }
    }

    override fun onCreate() {
        super.onCreate()

        startNotification()
    }

    // 처음 시작되면 호출되는 함수.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        callEvent(intent)
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    // 이벤트를 호출(구현하고 싶은 코드를 구현하면 됩니다.)
    private fun callEvent(intent: Intent?) {
        Log.d("test", "callEvent()")
        timer.scheduledExecutionTime()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    // Timer에서 실행되는 함수.
    private fun doSomethingTimer() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, "test", Toast.LENGTH_SHORT).show()
        }
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
            //TODO("VERSION.SDK_INT < O")
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