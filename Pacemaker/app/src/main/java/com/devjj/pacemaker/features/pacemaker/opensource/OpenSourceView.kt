package com.devjj.pacemaker.features.pacemaker.opensource

import android.os.Parcel
import android.os.Parcelable
import com.devjj.pacemaker.core.extension.empty

data class OpenSourceView(val name: String, val author : String,val license : String ):
    Parcelable {

    companion object CREATOR : Parcelable.Creator<OpenSourceView>{
        override fun createFromParcel(parcel: Parcel): OpenSourceView {
            return OpenSourceView(parcel)
        }

        override fun newArray(size: Int): Array<OpenSourceView?> {
            return arrayOfNulls(size)
        }

        fun empty() = OpenSourceView(String.empty(),String.empty(),String.empty())
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(parcel.readString()!!,parcel.readString()!!,parcel.readString()!!)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(name)
            writeString(author)
            writeString(license)
        }
    }

}