package com.devjj.pacemaker.features.pacemaker.opensource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentOpenSourceBinding
import com.devjj.pacemaker.features.pacemaker.OpenSourceActivity
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import javax.inject.Inject

class OpenSourceFragment : BaseFragment() {

    @Inject
    lateinit var openSourceAdapter: OpenSourceAdapter
    @Inject
    lateinit var setting: SettingSharedPreferences

    private var _binding: FragmentOpenSourceBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun layoutId() = R.layout.fragment_open_source

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenSourceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeView(){
        binding.fOpenSourceRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.fOpenSourceRecyclerview.adapter = openSourceAdapter
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
                (requireActivity() as OpenSourceActivity).getBinding()
                    .aOpenSourceCloTitle.setBackgroundResource(R.drawable.img_title_background_nighttime)
                (requireActivity() as OpenSourceActivity).getBinding()
                    .aOpenSourceFloContainer.setBackgroundColor(loadColor(activity!!,R.color.grey_444646))

                binding.fOpenSourceTvApacheTitle.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOpenSourceTvApacheTitle.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fOpenSourceTvMitTitle.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOpenSourceTvMitTitle.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fOpenSourceTvApache.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOpenSourceTvMit.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
            }

            false -> {
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.blue_5C83CF)
                (requireActivity() as OpenSourceActivity).getBinding()
                    .aOpenSourceCloTitle.setBackgroundResource(R.drawable.img_title_background_daytime)
                (requireActivity() as OpenSourceActivity).getBinding()
                    .aOpenSourceFloContainer.setBackgroundColor(loadColor(activity!!,R.color.white_FFFFFF))

                binding.fOpenSourceTvApacheTitle.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOpenSourceTvApacheTitle.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                binding.fOpenSourceTvMitTitle.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOpenSourceTvMitTitle.setTextColor(loadColor(activity!!,R.color.grey_87888A))

                binding.fOpenSourceTvApache.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOpenSourceTvMit.setTextColor(loadColor(activity!!,R.color.black_3B4046))
            }
        }
    }

}