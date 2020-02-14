package com.devjj.pacemaker.core.di.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PlayViewSharedPreferences(context: Context) {
    val PREFS_FILENAME = "playView"
    val PREF_KEY = "btnClicked"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    /* 파일 이름과 btnClicked를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    var btnClicked: Boolean
        get() = prefs.getBoolean(PREF_KEY, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY, value).apply()
    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 false
     * set(value) 실행 시 value로 값을 대체한 후 저장 */
}