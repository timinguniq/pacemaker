package com.devjj.pacemaker.core.extension

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.functional.Dlog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd


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

private val INTERSTITIAL_MAX_COUNT_MAX_VALUE = 8
private val INTERSTITIAL_MAX_COUNT_MIN_VALUE = 3
private var INTERSTITIAL_MAX_COUNT = (((INTERSTITIAL_MAX_COUNT_MAX_VALUE - INTERSTITIAL_MAX_COUNT_MIN_VALUE) * Math.random()) +
        INTERSTITIAL_MAX_COUNT_MIN_VALUE).round(0).toInt()

// 전면 광고를 셋팅하는 함수.
fun showInterstitialAd(activity: Activity, setting : SettingSharedPreferences){
        Dlog.d("INTERSTITIAL_MAX_COUNT : $INTERSTITIAL_MAX_COUNT")
        setting.interstitialCount++
        if(setting.interstitialCount < INTERSTITIAL_MAX_COUNT){
                return
        } else {
                setting.interstitialCount = 0
        }

        val AD_INTERSTITIAL_UNIT_ID = activity.getString(R.string.AD_INTERSTITIAL_UNIT_ID)

        // Create the InterstitialAd and set it up.
        val mInterstitialAd = InterstitialAd(activity).apply {
                adUnitId = AD_INTERSTITIAL_UNIT_ID
                adListener = (object : AdListener() {
                        override fun onAdLoaded() {
                                //Toast.makeText(this@PacemakerActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                                if (isLoaded) {
                                        show()
                                }
                        }

                        override fun onAdFailedToLoad(errorCode: Int) {
                                Toast.makeText(activity,
                                        "onAdFailedToLoad() with error code: $errorCode",
                                        Toast.LENGTH_SHORT).show()
                        }

                        override fun onAdClosed() {
                                Dlog.d( "onAdClosed")
                        }
                })
        }

        mInterstitialAd.loadAd(AdRequest.Builder().build())

}

fun convertPxToDp(context : Context, px : Float) :Float {
        return px / context.resources.displayMetrics.density
}

fun convertDpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
}

// color resource 불러오기
fun loadColor(context: Context, resource: Int) = ResourcesCompat.getColor(context.resources, resource, null)