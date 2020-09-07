package com.devjj.pacemaker.core.di.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SettingSharedPreferences(context: Context) {

    val PREFS_FILENAME = "setting"
    val PREF_KEY_DARK_MODE = "nightMode"
    val PREF_KEY_HEIGHT = "height"
    val PREF_KEY_WEIGHT = "weight"
    val PREF_KEY_REST_TIME = "restTime"
    val PREF_KEY_UPDATE_WEIGHT = "updateWeight"
    val PREF_KEY_UPDATE_HEIGHT = "updateHeight"
    val PREF_KEY_SORT_MODE = "sortMode"
    val PREF_KEY_FINISH_COUNT = "interstitialCount"
    val PREF_KEY_SHOW_BMI = "showbmi"
    val PREF_KEY_BMI = "BMI"
    val PREF_KEY_MONTLY_STATISTICS = "monthStat"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    /* 파일 이름과 데이터를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    var isNightMode: Boolean
        get() = prefs.getBoolean(PREF_KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_DARK_MODE, value).apply()
    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 false
     * set(value) 실행 시 value로 값을 대체한 후 저장 */

    var isUpdateWeight: Boolean
        get() = prefs.getBoolean(PREF_KEY_UPDATE_WEIGHT, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_UPDATE_WEIGHT, value).apply()

    var isUpdateHeight: Boolean
        get() = prefs.getBoolean(PREF_KEY_UPDATE_HEIGHT, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_UPDATE_HEIGHT, value).apply()

    var height: Float
        get() = prefs.getFloat(PREF_KEY_HEIGHT, -1.0f)
        set(value) = prefs.edit().putFloat(PREF_KEY_HEIGHT, value).apply()

    var weight: Float
        get() = prefs.getFloat(PREF_KEY_WEIGHT, -1.0f)
        set(value) = prefs.edit().putFloat(PREF_KEY_WEIGHT, value).apply()

    var isEditMode: Boolean
        get() = prefs.getBoolean(PREF_KEY_SORT_MODE, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_SORT_MODE, value).apply()

    var restTime: Int
        get() = prefs.getInt(PREF_KEY_REST_TIME, 60)
        set(value) = prefs.edit().putInt(PREF_KEY_REST_TIME, value).apply()

    var interstitialCount: Int
        get() = prefs.getInt(PREF_KEY_FINISH_COUNT, 0)
        set(value) = prefs.edit().putInt(PREF_KEY_FINISH_COUNT, value).apply()
  
    var isShowbmi : Boolean
        get() = prefs.getBoolean(PREF_KEY_SHOW_BMI, true)
        set(value) = prefs.edit().putBoolean(PREF_KEY_SHOW_BMI,value).apply()

    var bmi : Int
        get() = prefs.getInt(PREF_KEY_BMI, 25)
        set(value) = prefs.edit().putInt(PREF_KEY_BMI,value).apply()

    var isMonthly : Boolean
        get() = prefs.getBoolean(PREF_KEY_MONTLY_STATISTICS, false)
        set(value) = prefs.edit().putBoolean(PREF_KEY_MONTLY_STATISTICS,value).apply()
}
