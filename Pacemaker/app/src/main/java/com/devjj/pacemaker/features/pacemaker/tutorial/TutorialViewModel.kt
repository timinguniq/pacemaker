package com.devjj.pacemaker.features.pacemaker.tutorial

import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.platform.BaseViewModel
import javax.inject.Inject

class TutorialViewModel
@Inject constructor(): BaseViewModel() {
    // 현재 아이템 값 저장하는 변수
    var currentItemData: MutableLiveData<Int> = MutableLiveData()
}