package com.devjj.pacemaker.features.pacemaker.addition

import android.os.Parcel
import android.os.Parcelable
import com.devjj.pacemaker.core.extension.empty

data class AdditionView(val id: Int, val part_img: Int, val name: String, val mass: Int, val rep: Int, val set: Int, val interval: Int) :
    Parcelable {

    companion object CREATOR : Parcelable.Creator<AdditionView> {
        override fun createFromParcel(parcel: Parcel): AdditionView {
            return AdditionView(parcel)
        }

        override fun newArray(size: Int): Array<AdditionView?> {
            return arrayOfNulls(size)
        }

        fun empty() = AdditionView(-1, 0, String.empty(), 0, 0, 0, 0)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readString()!!, parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeInt(part_img)
            writeString(name)
            writeInt(mass)
            writeInt(rep)
            writeInt(set)
            writeInt(interval)
        }
    }

}