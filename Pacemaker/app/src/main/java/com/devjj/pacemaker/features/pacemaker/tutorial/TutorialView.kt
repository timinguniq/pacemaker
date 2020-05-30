package com.devjj.pacemaker.features.pacemaker.tutorial

import android.os.Parcel
import android.os.Parcelable

data class TutorialView(val id: Int, val tutorial_img: Int) : Parcelable {

    companion object CREATOR : Parcelable.Creator<TutorialView>{
        override fun createFromParcel(parcel: Parcel): TutorialView {
            return TutorialView(parcel)
        }

        override fun newArray(size: Int): Array<TutorialView?> {
            return arrayOfNulls(size)
        }

        fun empty() = TutorialView(-1, 0)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeInt(tutorial_img)
        }
    }

}