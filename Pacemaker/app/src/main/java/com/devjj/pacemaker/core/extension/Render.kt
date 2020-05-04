package com.devjj.pacemaker.core.extension

import android.app.Activity
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.R


// TODO : 나중에 상의
// part_img를 resource로 변경해주는 함수.
fun convertPartImgToResource(part_img: Int, isNightMode: Boolean) =
        if(!isNightMode) {
                // 화이트 모드
                when (part_img) {
                        0 -> R.drawable.img_part_upper_body_daytime
                        1 -> R.drawable.img_part_arm_daytime
                        2 -> R.drawable.img_part_chest_daytime
                        3 -> R.drawable.img_part_abdomen_daytime
                        4 -> R.drawable.img_part_lower_body_daytime
                        5 -> R.drawable.img_part_shoulder_daytime
                        else -> R.drawable.img_part_upper_body_daytime
                }
        }else {
                // 다크 모드
                when (part_img) {
                        0 -> R.drawable.img_part_upper_body_nighttime
                        1 -> R.drawable.img_part_arm_nighttime
                        2 -> R.drawable.img_part_chest_nighttime
                        3 -> R.drawable.img_part_abdomen_nighttime
                        4 -> R.drawable.img_part_lower_body_nighttime
                        5 -> R.drawable.img_part_shoulder_nighttime
                        else -> R.drawable.img_part_upper_body_nighttime
                }
        }

fun convertPxToDp(context : Context, px : Float) :Float {
        return px / context.resources.displayMetrics.density
}

fun convertDpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
}

// color resource 불러오기
fun loadColor(activity: Activity, resource: Int) = ResourcesCompat.getColor(activity.resources, resource, null)