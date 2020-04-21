package com.devjj.pacemaker.core.extension

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes


fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() { this.visibility = View.VISIBLE }

fun View.invisible() { this.visibility = View.INVISIBLE }

fun View.isGone() = this.visibility == View.GONE

fun View.gone() { this.visibility = View.GONE }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

// 값을 넣으면 pixel 값을 얻는 함수.
fun getPixelValue(context: Context, dimenId: Int): Int {
    val resources: Resources = context.getResources()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dimenId.toFloat(),
        resources.displayMetrics
    ).toInt()
}