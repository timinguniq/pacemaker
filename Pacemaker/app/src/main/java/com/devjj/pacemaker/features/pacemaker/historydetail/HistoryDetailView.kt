package com.devjj.pacemaker.features.pacemaker.historydetail

import android.os.Parcel
import android.os.Parcelable
import com.devjj.pacemaker.core.extension.empty

data class HistoryDetailView(val id: Int, val date:String,val part_img: Int, val name: String, val mass: Int, val rep: Int, val setGoal:Int ,val setDone: Int, val interval: Int, val achievement:Int,val achievementRate:Int):
    Parcelable {

    companion object CREATOR : Parcelable.Creator<HistoryDetailView>{
        override fun createFromParcel(parcel: Parcel): HistoryDetailView {
            return HistoryDetailView(parcel)
        }

        override fun newArray(size: Int): Array<HistoryDetailView?> {
            return arrayOfNulls(size)
        }

        fun empty() = HistoryDetailView(-1,String.empty(),0,String.empty(),0,0,0,0,0,0,0)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(parcel.readInt(),parcel.readString()!!, parcel.readInt(), parcel.readString()!!, parcel.readInt(),
        parcel.readInt(), parcel.readInt(),parcel.readInt(), parcel.readInt(), parcel.readInt(),parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(date)
            writeInt(part_img)
            writeString(name)
            writeInt(mass)
            writeInt(rep)
            writeInt(setGoal)
            writeInt(setDone)
            writeInt(interval)
            writeInt(achievement)
            writeInt(achievementRate)
        }
    }

}