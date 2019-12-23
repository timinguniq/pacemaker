package com.devjj.pacemaker.features.pacemaker.home

import android.os.Parcel
import android.os.Parcelable

// part_img는 운동 부위 아이콘(이미지) ex) R.drawable.icon
// name는 사용자가 입력한 운동이름
// mass는 중량 없을시 0
// set는 횟수 ex) 3
// interval는 운동간 휴식시간 단위는 초 ex) 40
data class HomeView(
    val id: Int,
    val part_img: Int,
    val name: String,
    val mass: Int,
    val set: Int,
    val interval: Int
) :
    Parcelable {
    /*
    companion object{
        @JvmField val CREATOR = parcelableCreator(::HomeView)
    }
    */

    companion object CREATOR : Parcelable.Creator<HomeView> {
        override fun createFromParcel(parcel: Parcel): HomeView {
            return HomeView(parcel)
        }

        override fun newArray(size: Int): Array<HomeView?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents() = 0


    constructor(parcel: Parcel) : this(
        parcel.readInt(), parcel.readInt(), parcel.readString()!!, parcel.readInt(),
        parcel.readInt(), parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeInt(part_img)
            writeString(name)
            writeInt(mass)
            writeInt(set)
            writeInt(interval)
        }
    }
}