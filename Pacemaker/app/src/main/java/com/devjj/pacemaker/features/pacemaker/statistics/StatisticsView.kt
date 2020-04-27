package com.devjj.pacemaker.features.pacemaker.statistics

import android.os.Parcel
import android.os.Parcelable

data class StatisticsView(val id : Int, val date: String, val height : Float, val weight : Float) :
    Parcelable {

    companion object CREATOR : Parcelable.Creator<StatisticsView> {
        override fun createFromParcel(parcel : Parcel): StatisticsView {
            return StatisticsView(parcel)
        }
        override fun newArray(size: Int): Array<StatisticsView?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents() = 0

    constructor(parcel : Parcel) : this(parcel.readInt(), parcel.readString()!!, parcel.readFloat(), parcel.readFloat())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest){
            writeInt(id)
            writeString(date)
            writeFloat(height)
            writeFloat(weight)
        }
    }

}