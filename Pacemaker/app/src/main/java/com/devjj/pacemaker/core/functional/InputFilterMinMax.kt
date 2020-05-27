package com.devjj.pacemaker.core.functional

import android.app.Activity
import android.text.InputFilter
import android.text.Spanned
import android.widget.Toast
import com.devjj.pacemaker.R

class InputFilterMinMax(
    private val activity: Activity,
    private var min: Float,
    private var max: Float
) : InputFilter {

    private var toast = Toast.makeText(
        activity,
        "${activity.getString(R.string.dprofile_msg_out_of_range_str)}(${min.toInt()} - ${max.toInt()})",
        Toast.LENGTH_SHORT
    )

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        try {
            var value = dest.toString().substring(0, dstart) + dest.toString()
                .substring(dend, dest.toString().length)
            value = value.substring(0, dstart) + source.toString() + value.substring(
                dstart,
                value.length
            )
            var input: Float = value.toFloat()
            if (isInRange(min, max, input)) return null
            else {
                toast.show()
            }
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(min: Float, max: Float, input: Float): Boolean {
        return if (max > min) (input >= min) && (input <= max) else (input >= max) && (input <= min)
    }

}