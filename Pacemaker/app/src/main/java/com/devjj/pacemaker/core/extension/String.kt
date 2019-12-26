package com.devjj.pacemaker.core.extension

import android.text.Editable
import android.widget.EditText

fun String.Companion.empty() = ""

// editText에서 스트링 값 넣을 떄 쓰는 함수.
fun String.Companion.editText(str: String) = Editable.Factory.getInstance().newEditable(str)
