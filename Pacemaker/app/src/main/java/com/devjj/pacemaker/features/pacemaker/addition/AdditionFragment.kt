package com.devjj.pacemaker.features.pacemaker.addition

import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeView
import kotlinx.android.synthetic.main.fragment_addition.*
import kotlinx.android.synthetic.main.fragment_home.*

class AdditionFragment(private val intent: Intent) : BaseFragment() {

    private val ADDITION_MODE = 0
    private val EDITING_MODE = 1

    private lateinit var additionViewModel: AdditionViewModel

    override fun layoutId() = R.layout.fragment_addition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val additionView: AdditionView = intent.getParcelableExtra("view")?:AdditionView.empty()
        Log.d("addition", "additionView id : " + additionView.id)
        Log.d("addition", "additionView part_img : " + additionView.part_img)
        Log.d("addition", "additionView name : " + additionView.name)
        Log.d("addition", "additionView mass : " + additionView.mass)
        Log.d("addition", "additionView set : " + additionView.set)
        Log.d("addition", "additionView interval : " + additionView.interval)

        initializeView(additionView)
    }

    // additionFragment 초기화 하는 함수
    private fun initializeView(additionView: AdditionView) {
        if(additionView.name.isEmpty()){
            fAddition_tv.text = "addition"
        }else{
            fAddition_tv.text = additionView.name
        }
    }


}
