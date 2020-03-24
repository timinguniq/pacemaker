package com.devjj.pacemaker.core.extension

import com.devjj.pacemaker.R

// TODO : 나중에 상의
// part_img를 resource로 변경해주는 함수.
fun convertPartImgToResource(part_img: Int, isDarkMode: Boolean) =
        if(!isDarkMode) {
                // 화이트 모드
                when (part_img) {
                        0 -> R.drawable.part_wm_upper_body_img
                        1 -> R.drawable.part_wm_arm_img
                        2 -> R.drawable.part_wm_chest_img
                        3 -> R.drawable.part_wm_abdomen_img
                        4 -> R.drawable.part_wm_lower_body_img
                        5 -> R.drawable.part_wm_shoulder_img
                        else -> R.drawable.part_wm_upper_body_img
                }
        }else {
                // 다크 모드
                when (part_img) {
                        0 -> R.drawable.part_dm_upper_body_img
                        1 -> R.drawable.part_dm_arm_img
                        2 -> R.drawable.part_dm_chest_img
                        3 -> R.drawable.part_dm_abdomen_img
                        4 -> R.drawable.part_dm_lower_body_img
                        5 -> R.drawable.part_dm_shoulder_img
                        else -> R.drawable.part_dm_upper_body_img
                }
        }