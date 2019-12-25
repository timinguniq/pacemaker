package com.devjj.pacemaker.features.pacemaker.history

import android.os.Parcel
import android.os.Parcelable

data class HistoryView(
    val id: Int,
    val date: String,
    val achivementRate: Int
) : Parcelable{
    companion object CREATOR : Parcelable.Creator<HistoryView>{
        override fun createFromParcel(parcel: Parcel): HistoryView {
            return HistoryView(parcel)
        }

        override fun newArray(size: Int): Array<HistoryView?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel):this(
        parcel.readInt(), parcel.readString()!!,parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest){
            writeInt(id)
            writeString(date)
            writeInt(achivementRate)
        }
    }
}