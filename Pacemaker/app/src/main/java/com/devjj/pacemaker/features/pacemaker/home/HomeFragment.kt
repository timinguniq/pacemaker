package com.devjj.pacemaker.features.pacemaker.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun layoutId() = R.layout.fragment_home

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    // PacemakerActivity 초기화 하는 함수
    private fun initializeView() {
        // 플로팅 버튼 클릭 이벤트
        fh_floatingactionbtn?.setOnClickListener {
            navigator.showAddition(context!!)
        }
    }
}
