package com.devjj.pacemaker.features.pacemaker.history

import android.app.Activity
import com.devjj.pacemaker.R

fun getComment(activity : Activity , accumulatedWeight : Int) :String{

    when(accumulatedWeight){
        in 0.rangeTo(50)        ->  return activity.getString(R.string.fhistory_0)
        in 50.rangeTo(100)      ->  return activity.getString(R.string.fhistory_50)
        in 100.rangeTo(200)     ->  return activity.getString(R.string.fhistory_100)
        in 200.rangeTo(500)     ->  return activity.getString(R.string.fhistory_200)
        in 500.rangeTo(1000)    ->  return activity.getString(R.string.fhistory_500)
        in 1000.rangeTo(2000)   ->  return activity.getString(R.string.fhistory_1000)
        in 2000.rangeTo(5000)   ->  return activity.getString(R.string.fhistory_2000)
        in 5000.rangeTo(10000)  ->  return activity.getString(R.string.fhistory_5000)
        in 10000.rangeTo(20000) ->  return activity.getString(R.string.fhistory_10000)
        in 20000.rangeTo(50000) ->  return activity.getString(R.string.fhistory_20000)
        else                            ->  return activity.getString(R.string.fhistory_50000)

    }
    return ""
}