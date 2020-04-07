package com.devjj.pacemaker.core.di.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SettingSharedPreferences(context: Context) {
    val PREFS_FILENAME = "setting"

    val PREF_KEY_DARK_MODE = "nightMode"
    val PREF_KEY_HEIGHT = "height"
    val PREF_KEY_WEIGHT = "weight"
    val PREF_KEY_REST_TIME = "restTime"
    val PREF_KEY_LANGUAGE = "language"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    /* 파일 이름과 데이터를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    var isNightMode: Boolean
        get() = prefs.getBoolean(PREF_KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_DARK_MODE, value).apply()
    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 false
     * set(value) 실행 시 value로 값을 대체한 후 저장 */

    var height: Int
        get() = prefs.getInt(PREF_KEY_HEIGHT, 0)
        set(value) = prefs.edit().putInt(PREF_KEY_HEIGHT, value).apply()

    var weight: Int
        get() = prefs.getInt(PREF_KEY_WEIGHT, 0)
        set(value) = prefs.edit().putInt(PREF_KEY_WEIGHT, value).apply()

    var restTime: Int
        get() = prefs.getInt(PREF_KEY_REST_TIME, 0)
        set(value) = prefs.edit().putInt(PREF_KEY_REST_TIME, value).apply()

    var language: Int
        get() = prefs.getInt(PREF_KEY_LANGUAGE, 0)
        set(value) = prefs.edit().putInt(PREF_KEY_LANGUAGE, value).apply()

}
