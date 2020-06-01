package com.devjj.pacemaker.features.pacemaker.playpopup

import android.os.*
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.features.pacemaker.dialog.showProfileDialog
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.service.TimerService
import com.devjj.pacemaker.features.pacemaker.usecases.UpdateProfile
import kotlinx.android.synthetic.main.fragment_play_popup.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PlayPopupFragment : BaseFragment() {
    private var totalTimeStart: Long = 0
    private var totalTimeEnd: Long = 0

    @Inject lateinit var setting: SettingSharedPreferences
    @Inject lateinit var navigator: Navigator
    @Inject lateinit var updateProfile: UpdateProfile

    private lateinit var playPopupListener : PlayPopupListener

    private lateinit var playPopupViewModel : PlayPopupViewModel

    // 진행바들 변수 리스트
    val progressBars: List<View> by lazy{
        listOf(
            fPlayPopup_vi_progress_10, fPlayPopup_vi_progress_9, fPlayPopup_vi_progress_8, fPlayPopup_vi_progress_7,
            fPlayPopup_vi_progress_6, fPlayPopup_vi_progress_5, fPlayPopup_vi_progress_4, fPlayPopup_vi_progress_3,
            fPlayPopup_vi_progress_2, fPlayPopup_vi_progress_1)
    }

    override fun layoutId() = R.layout.fragment_play_popup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        Dlog.d( "onCreate PlayPopupFragment")
        playPopupViewModel = viewModel(viewModelFactory){
            observe(existPlayPopupList, ::existPlayPopupList)
            observe(playPopupList, ::renderPlayPopupList)
            observe(playPopupStatisticsData, ::getPlayPopupStatisticsView)
            failure(failure, ::handleFailure)
        }

        // 서비스 시작
        TimerService.startService(activity!!)
        //

        // 운동 시작 시간 측정
        totalTimeStart = System.currentTimeMillis()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Dlog.d( "onViewCreated")
    }

    override fun onResume() {
        super.onResume()

        // 총 운동 시간 초기화
        totalTime = 0

        isFinalExercise = false
        isFinalExerciseFinalSet = false

        isNightMode = setting.isNightMode

        // 모드에 따른 셋팅(ex 화이트모드, 다크모드)
        initSettingMode(isNightMode)

        // 모드별 셋팅(ex 진행모드, 정지모드)
        settingForMode()

        // playPopupListener 초기화
        playPopupListener = PlayPopupListener(activity!!, this, playPopupViewModel, navigator)

        // 클릭 리스너들 모아둔 함수.
        playPopupListener.clickListener()

        // 데이터를 리스트를 로드하는 함수.
        playPopupViewModel.existPlayPopupList()

        if(!TimerService.isProgressTimer()) {
            // 초기 마진 셋팅
            marginPartImg(25)

            // 데이터를 리스트를 로드하는 함수.
            playPopupViewModel.loadPlayPopupList()

        }else{
            // isProgressTimer = true 일때
            val playPopupViewCurrentSet = currentPlayPopupData.setDone

            settingNextProgressBars(playPopupViewCurrentSet)
        }

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
            listResource.add(loadColor(activity!!, R.color.blue_5F87D6))
            listResource.add(R.drawable.img_popup_background_daytime)
            listResource.add(R.drawable.fplaypopup_img_progress_unselect_bar_daytime)
            listResource.add(loadColor(activity!!, R.color.blue_5C83CF_70))
            listResource.add(loadColor(activity!!, R.color.blue_5C83CF))
            listResource.add(loadColor(activity!!, R.color.black_3B4046))
            listResource.add(loadColor(activity!!, R.color.grey_B2B2B2))
            listResource.add(R.drawable.img_save_daytime)
            listResource.add(loadColor(activity!!, R.color.blue_5F87D6))
            listResource.add(R.drawable.fplaypopup_img_plus_background_daytime)
            listResource.add(R.drawable.fplaypopup_img_under_background_daytime)
            listResource.add(R.drawable.fplaypopup_img_under_next_background_daytime)
        }else{
            // 다크모드
            listResource.add(loadColor(activity!!, R.color.grey_444646))
            listResource.add(R.drawable.img_popup_background_nighttime)
            listResource.add(R.drawable.fplaypopup_img_progress_unselect_bar_nighttime)
            listResource.add(loadColor(activity!!, R.color.orange_B0463C))
            listResource.add(loadColor(activity!!, R.color.orange_F74938))
            listResource.add(loadColor(activity!!, R.color.white_FFFFFF))
            listResource.add(loadColor(activity!!, R.color.grey_87888A))
            listResource.add(R.drawable.img_save_nighttime)
            listResource.add(loadColor(activity!!, R.color.orange_F74938))
            listResource.add(R.drawable.fplaypopup_img_plus_background_nighttime)
            listResource.add(R.drawable.fplaypopup_img_under_background_nighttime)
            listResource.add(R.drawable.fplaypopup_img_under_next_background_nighttime)
        }

        activity?.window?.statusBarColor = listResource[0]
        fPlayPopup_clo_main.setBackgroundResource(listResource[1])
        for(view in progressBars){
            view.setBackgroundResource(listResource[2])
        }
        fPlayPopup_cv_rate.rimColor = listResource[3]
        fPlayPopup_cv_rate.setBarColor(listResource[4])
        fPlayPopup_tv_name.setTextColor(listResource[5])
        fPlayPopup_tv_m_r.setTextColor(listResource[6])
        fPlayPopup_iv_confirm.setImageResource(listResource[7])
        fPlayPopup_tv_plus.setTextColor(listResource[8])
        fPlayPopup_tv_plus.setBackgroundResource(listResource[9])
        fPlayPopup_vi_under.setBackgroundResource(listResource[10])
        fPlayPopup_flo_next.setBackgroundResource(listResource[11])

        fPlayPopup_clo_next.visibility = View.GONE
    }

    // 재생 목록 확인하고 창 종료하는 함수
    private fun existPlayPopupList(playPopupView: List<PlayPopupView>?){
        Dlog.d( "existPlayPopupList")
        // 재생 목록이 없으면 화면 종료
        if(playPopupView!!.isEmpty()){
            // 팝업
            Toast.makeText(context, R.string.fplaypopup_tv_playlist_not_exist_str, Toast.LENGTH_LONG).show()
            // 서비스 종료
            TimerService.stopService(context!!)
            // 화면 종료
            activity?.finish()
        }
        //
    }

    // playPopup 데이터들 갱신하는 함수.
    private fun renderPlayPopupList(playPopupView: List<PlayPopupView>?) {
        //existPlayPopupList(playPopupView)

        Dlog.d( "renderPlayPopupList")

        // 전체 포기를 위한 초기화 데이터 리스트에 추가
        for(i in playPopupView.orEmpty().indices){
            var playPopupData = PlayPopupData(playPopupView.orEmpty()[i].id, playPopupView.orEmpty()[i].part, playPopupView.orEmpty()[i].name,
                playPopupView.orEmpty()[i].mass, playPopupView.orEmpty()[i].rep, playPopupView.orEmpty()[i].setGoal,
                playPopupView.orEmpty()[i].setDone, playPopupView.orEmpty()[i].interval, playPopupView.orEmpty()[i].achievement == 1)
            if(allPlayPopupDataList.size >= playPopupView.orEmpty().size) {
                allPlayPopupDataList[i] = playPopupData
            }else{
                allPlayPopupDataList.add(playPopupData)
            }
        }
        //

        // PlayPopupView를 핸들링하는 함수.
        handlePlayPopupView(playPopupView)
    }

    // 핸들링하는 함수
    fun handlePlayPopupView(playPopupView: List<PlayPopupView>?){
        var achivementCount = 0
        var playPopupDataList = mutableListOf<PlayPopupData>()
        // 만일 운동이 하나일 때 예외처리(운동 간 휴식 시간이 나오지 않게 하기 위한 예외 처리)
        if(playPopupView.orEmpty().size == 1) isFinalExercise = true

        for(popupView in playPopupView.orEmpty()){
            val achivement = popupView.achievement == 1
            if(!achivement){
                if(popupView.setDone == 0) popupView.setDone++

                currentPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.setGoal, popupView.setDone, popupView.interval, popupView.achievement == 1)

                showInitSetting(popupView)
                break
            }else{
                achivementCount++
                Dlog.d( "achivementCount : ${achivementCount}")
                Dlog.d( "playPopupView : ${playPopupView?.size}")

                val initPlayPopupData = PlayPopupData(popupView.id, popupView.part, popupView.name, popupView.mass,
                    popupView.rep, popupView.setGoal, popupView.setDone, popupView.interval,
                    popupView.achievement == 1
                )
                playPopupDataList.add(initPlayPopupData)

                if(achivementCount == playPopupView?.size){
                    totalTimeEnd = System.currentTimeMillis()

                    totalTime = ((totalTimeEnd - totalTimeStart)/60000).toInt()

                    val sSaveDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
                    //date = sSaveDate
                    date = sSaveDate
                    height = setting.height
                    weight = setting.weight

                    // 날짜가 같은 DB에 데이터 다 지우기
                    playPopupViewModel.deleteExerciseHistoryData()

                    // 오늘 운동의 전체 성공세트, 전체 목표세트
                    var todayTotalSetDone = 0
                    var todayTotalSetGoal = 0

                    for(playPopupData in playPopupDataList) {
                        Dlog.d( "id : ${playPopupData.id}, part_img : ${playPopupData.part_img}, name : ${playPopupData.name},\n"
                                + "mass : ${playPopupData.mass}, rep : ${playPopupData.rep}, setGoal : ${playPopupData.setGoal},\n"
                                + "setDone : ${playPopupData.setDone}, interval : ${playPopupData.interval}, achievement : ${playPopupData.achievement}")

                        todayTotalSetDone += playPopupData.setDone
                        todayTotalSetGoal += playPopupData.setGoal

                        var insertPlayPopupData =
                            PlayPopupData(
                                playPopupData.id, playPopupData.part_img, playPopupData.name,
                                playPopupData.mass, playPopupData.rep, playPopupData.setGoal,
                                playPopupData.setDone, playPopupData.interval, playPopupData.achievement
                            )

                        playPopupViewModel.saveExerciseHistoryData(insertPlayPopupData)
                    }

                    playPopupViewModel.saveStatisticsData(todayTotalSetDone, todayTotalSetGoal)

                    // 데이터 초기화
                    for(playPopupData in playPopupDataList){
                        playPopupData.achievement = false
                        playPopupData.setDone = 0
                        playPopupViewModel.updateExercisePlayPopupData(playPopupData)
                    }
                    //

                    // TimerService 종료
                    TimerService.stopService(activity!!)
                    //
                    Dlog.d( "totalTimer : $totalTime")
                }

                if(achivementCount == playPopupView?.size?.minus(1)) {
                    isFinalExercise = true
                }
            }

        }
    }

    // 보드에 세트화면 셋팅하는 함수.
    fun showSet(){
        if(maxSet > currentSet){
            // 현재 세트에 1을 더하는 함수.
            currentPlayPopupData.setDone++
            val currentPlayPopupView = PlayPopupView(currentPlayPopupData.id, currentPlayPopupData.part_img, currentPlayPopupData.name,
                currentPlayPopupData.mass, currentPlayPopupData.rep, currentPlayPopupData.setGoal,
                currentPlayPopupData.setDone, currentPlayPopupData.interval,
                if(currentPlayPopupData.achievement) 1 else 0)
            showInitSetting(currentPlayPopupView)

            // 운동 전체 포기를 위한 데이터 갱신
            for(i in allPlayPopupDataList.indices){
                if(allPlayPopupDataList[i].id == currentPlayPopupData.id){
                    allPlayPopupDataList[i] = currentPlayPopupData
                }
            }

            // setDone 업데이트 해주는 코드
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
        Dlog.d( "showBoardSetting")
        // 최대 셋 설정하는 코드
        maxSet = currentPlayPopupView.setGoal
        currentSet = currentPlayPopupView.setDone
        //

        // progressBar 셋팅하는 코드
        isVisibleProgressBars(maxSet)
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

        fPlayPopup_tv_name.text = String.regLen(currentPlayPopupView.name, EXERCISE_NAME_PLAY)
        var slash = getString(R.string.template_slash_str)
        var massUnit = getString(R.string.fplaypopup_tv_unit_mass_str)
        var repUnit = getString(R.string.fplaypopup_tv_unit_rep_str)
        var mstxv = currentPlayPopupView.mass.toString() + massUnit + " $slash " +
                currentPlayPopupView.rep.toString() + repUnit
        fPlayPopup_tv_m_r.text = mstxv

        interval = currentPlayPopupView.interval
        // 휴식 시간 타이머 시간 조정하는 함수
        settingRestTimeTv()
        Dlog.d("interval $interval")
        Dlog.d("timerFinish $timerFinish")

        // 세트가 마지막 세트로 왔을 때 휴식 시간을 운동간 휴식시간으로 셋팅하기 위한 코드
        if(currentSet == maxSet){
            if(isFinalExercise) isFinalExerciseFinalSet = true

            interval = setting.restTime
            // 휴식 시간 타이머 시간 조정하는 함수
            settingRestTimeTv()
        }
        //

        if(timerFinish)
            fPlayPopup_tv_rest_time.text = settingFormatForTimer(0)

    }

    // 휴식 시간 타이머 시간 조정하는 함수
    fun settingRestTimeTv(){
        val timerText = settingFormatForTimer(interval)
        fPlayPopup_tv_rest_time.text = timerText
    }

    // 모드별 화면 셋팅
    fun settingForMode(){
        when (mode) {
            STOP_MODE -> {
                fPlayPopup_clo_confirm.visible()
                fPlayPopup_clo_next.gone()
            }
            PROGRESS_MODE -> {
                fPlayPopup_clo_confirm.gone()
                fPlayPopup_clo_next.visible()
            }
            else -> {
                Dlog.d( "settingForMode Method error")
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
                progressBars[index].gone()
            }
        }
    }

    // progressbar 진행 셋팅하는 함수
    // input은 현재 set
    private fun settingNextProgressBars(currentCount: Int){
        val range = 9 downTo (10-currentCount)
        val resourcesSelect = if(!isNightMode) R.drawable.fplaypopup_img_progress_select_bar_daytime
                         else R.drawable.fplaypopup_img_progress_select_bar_nighttime

        val resourcesUnSelect = if(!isNightMode) R.drawable.fplaypopup_img_progress_unselect_bar_daytime
                        else R.drawable.fplaypopup_img_progress_unselect_bar_nighttime

       val handler = Handler(Looper.getMainLooper())

        for(index in 0..9){
            handler.post{
                progressBars[index].setBackgroundResource(resourcesUnSelect)
            }
        }

        clearAllProgressBarsAnimation()

        for(index in range){
            handler.post{
                progressBars[index].setBackgroundResource(resourcesSelect)
            }
        }

        if( mode == STOP_MODE ) {
            //10-currentCount 깜빡이게
            var blinkAnim = AlphaAnimation(0.2f, 1.0f)
            blinkAnim.duration = 500
            blinkAnim.repeatMode = Animation.REVERSE
            blinkAnim.repeatCount = Animation.INFINITE
            progressBars[10 - currentCount].animation = blinkAnim
            blinkAnim.start()
        }
    }

    // ExerciseHistroyData 추가 후 데이터 받아오는 함수
    private fun getPlayPopupStatisticsView(playPopupView: PlayPopupView?){
        Dlog.d( "getPlayPopupStatisticsView id : ${playPopupView?.id}")
        if(!setting.isUpdateHeight && !setting.isUpdateWeight){
            // 둘다 false 팝업창 안 띄우기
            activity?.finish()

            // 전면 광고를 띄우는 메소드
            showInterstitialAd(activity!!, setting)

            return
        }

        var standard = 0
        if(setting.isUpdateHeight) standard += GET_HEIGHT_ONLY
        if(setting.isUpdateWeight) standard += GET_WEIGHT_ONLY

        clearAllProgressBarsAnimation()

        when(standard){
            GET_HEIGHT_ONLY -> showProfileDialog(activity!!, setting, date, GET_HEIGHT_ONLY,updateProfile)
            GET_WEIGHT_ONLY -> showProfileDialog(activity!!, setting, date, GET_WEIGHT_ONLY,updateProfile)
            GET_HEIGHT_WEIGHT -> showProfileDialog(activity!!, setting, date, GET_HEIGHT_WEIGHT,updateProfile)
            else -> Dlog.d( "getPlayPopupStatisticsView error")
        }

        //2020-05-27 marker_1
        /*
        when(standard){
            1 -> showProfileDialog(activity!!, setting, date, GET_HEIGHT_ONLY,updateProfile)
            2 -> showProfileDialog(activity!!, setting, date, GET_WEIGHT_ONLY,updateProfile)
            3 -> showProfileDialog(activity!!, setting, date, GET_HEIGHT_WEIGHT,updateProfile)
            else -> Dlog.d( "getPlayPopupStatisticsView error")
        }*/
    }

    // playPopup 데이터 갱신 실패시 핸들링하는 함수.
    private fun handleFailure(failure: Failure?) {
        when (failure) {
            //is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            //is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is PlayPopupFailure.ListNotAvailable -> renderFailure(R.string.fplaypopup_tv_list_unavailable_str)
            else -> Dlog.d( "error")
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        // TODO : 나중에 메세지에 따른 구현 해야 될듯.
    }

    private fun clearAllProgressBarsAnimation(){
        for(progressBar in progressBars){
            progressBar.clearAnimation()
        }
    }

/*
    setting.interstitialCount++
    if(setting.interstitialCount >= FINISH_MAX_COUNT){
        setting.interstitialCount = 0
        // 전면 광고를 띄우는 메소드
        showInterstitialAd(this)
    }
*/
}
