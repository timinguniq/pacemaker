package com.devjj.pacemaker.core.extension

import android.text.Editable
import android.widget.EditText
import com.devjj.pacemaker.core.functional.Dlog
import java.util.*

fun String.Companion.empty() = ""

// editText에서 스트링 값 넣을 떄 쓰는 함수.
fun String.Companion.editText(str: String): Editable = Editable.Factory.getInstance().newEditable(str)

// 스트링을 길이로 짜르는 함수. position은 EXERCISE_NAME_HOME, EXERCISE_NAME_PLAY가 들어올 수 있다.
fun String.Companion.regLen(str: String, position: Int): String{
    val displayLanguage = Locale.getDefault().displayLanguage
    // 글자 제한 크기
    var maxLen = 0

    // TODO : 안스가 해줌.
    when(displayLanguage){
        "English" -> {
            when (position) {
                EXERCISE_NAME_HOME -> {
                    maxLen = EXERCISE_NAME_ENG_HOME_LEN_MAX
                }
                EXERCISE_NAME_PLAY -> {
                    maxLen = EXERCISE_NAME_ENG_PLAY_LEN_MAX
                }
                EXERCISE_NAME_HISTORY -> {
                    maxLen = EXERCISE_NAME_ENG_HISTORY_LEN_MAX
                }
            }
        }
        "한국어" -> {
            when (position) {
                EXERCISE_NAME_HOME -> {
                    maxLen = EXERCISE_NAME_KOR_HOME_LEN_MAX
                }
                EXERCISE_NAME_PLAY -> {
                    maxLen = EXERCISE_NAME_KOR_PLAY_LEN_MAX
                }
                EXERCISE_NAME_HISTORY -> {
                    maxLen = EXERCISE_NAME_KOR_HISTORY_LEN_MAX
                }
            }
        }
        else -> {
            when (position) {
                EXERCISE_NAME_HOME -> {
                    maxLen = EXERCISE_NAME_ENG_HOME_LEN_MAX
                }
                EXERCISE_NAME_PLAY -> {
                    maxLen = EXERCISE_NAME_ENG_PLAY_LEN_MAX
                }
                EXERCISE_NAME_HISTORY -> {
                    maxLen = EXERCISE_NAME_ENG_HISTORY_LEN_MAX
                }
            }
        }
    }

    var result = str
    Dlog.d("str.substring maxLen : ${maxLen}")
    if(str.length >= maxLen){
        Dlog.d("str.substring(0, maxLen-3) : ${str.substring(0, maxLen-3)}")
        result = str.substring(0, maxLen-3)
        result += "..."
    }
    return result
}