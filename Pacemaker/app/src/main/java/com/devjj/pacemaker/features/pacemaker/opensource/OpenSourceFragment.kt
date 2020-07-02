package com.devjj.pacemaker.features.pacemaker.opensource

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseFragment
import kotlinx.android.synthetic.main.activity_open_source.*
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.fragment_open_source.*
import javax.inject.Inject

class OpenSourceFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_open_source

    @Inject
    lateinit var openSourceAdapter: OpenSourceAdapter
    @Inject
    lateinit var setting: SettingSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView(){
        fOpenSource_recyclerview.layoutManager = LinearLayoutManager(activity)
        fOpenSource_recyclerview.adapter = openSourceAdapter
        Dlog.d(openSourceAdapter.itemCount.toString())
        openSourceAdapter.collection =
            listOf(OpenSourceView("CardSlider","Copyright 2019 IslamKhSh","Apache 2.0"),
            OpenSourceView("Circle-progress-view","Copyright(c) 2015 jakob-grabner","The MIT License"),
            OpenSourceView("leakCanary","Copyright 2015 Square, Inc.","Apache 2.0"),
            OpenSourceView("material-calendarview","Copyright (c) 2018 Prolific Interactive","The MIT License"),
            OpenSourceView("MPAndroidChart","Copyright 2020 Philipp Jahoda","Apache 2.0"),
            OpenSourceView("threetenabp","Copyright (C) 2015 Jake Wharton","Apache 2.0")
        )
        Dlog.d(openSourceAdapter.itemCount.toString())

        setColors()
    }

    private fun setColors(){
        when(setting.isNightMode){
            true -> {
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.grey_444646)
                activity!!.aOpenSource_clo_title.setBackgroundResource(R.drawable.img_title_background_nighttime)
                activity!!.aOpenSource_flo_container.setBackgroundColor(loadColor(activity!!,R.color.grey_444646))

                fOpenSource_tv_apache_title.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOpenSource_tv_apache_title.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fOpenSource_tv_mit_title.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                fOpenSource_tv_mit_title.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                fOpenSource_tv_apache.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                fOpenSource_tv_mit.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
            }

            false -> {
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.blue_5C83CF)
                activity!!.aOpenSource_clo_title.setBackgroundResource(R.drawable.img_title_background_daytime)
                activity!!.aOpenSource_flo_container.setBackgroundColor(loadColor(activity!!,R.color.white_FFFFFF))

                fOpenSource_tv_apache_title.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOpenSource_tv_apache_title.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                fOpenSource_tv_mit_title.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                fOpenSource_tv_mit_title.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                fOpenSource_tv_apache.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                fOpenSource_tv_mit.setTextColor(loadColor(activity!!,R.color.black_3B4046))

            }
        }
    }

}