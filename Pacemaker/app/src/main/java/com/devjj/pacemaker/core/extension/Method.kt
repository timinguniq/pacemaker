package com.devjj.pacemaker.core.extension

import com.devjj.pacemaker.R

// TODO : 나중에 상의
// part_img를 resource로 변경해주는 함수.
fun convertPartImgToResource(part_img: Int) = when(part_img){
        0 -> R.drawable.part_one_unclicked_img
        1 -> R.drawable.part_two_unclicked_img
        2 -> R.drawable.part_three_unclicked_img
        3 -> R.drawable.part_four_unclicked_img
        4 -> R.drawable.part_five_unclicked_img
        else -> R.drawable.part_one_unclicked_img
}