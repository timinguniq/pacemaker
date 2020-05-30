package com.devjj.pacemaker.features.pacemaker.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.iterator

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.platform.BaseFragment
import com.github.islamkhsh.CardSliderViewPager
import com.github.islamkhsh.viewpager2.ViewPager2
import kotlinx.android.synthetic.main.fragment_tutorial.*

/**
 * A simple [Fragment] subclass.
 */
class TutorialFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_tutorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)


    }

    override fun onResume() {
        super.onResume()
        // 데이터 초기화
        val tutorials = ArrayList<TutorialView>().apply{
            // add items to arraylist
            add(TutorialView(0, R.drawable.img_part_upper_body_daytime))
            add(TutorialView(1, R.drawable.img_part_abdomen_daytime))
            add(TutorialView(2, R.drawable.img_part_arm_daytime))
            add(TutorialView(3, R.drawable.img_part_chest_daytime))
            add(TutorialView(4, R.drawable.img_part_lower_body_daytime))
            add(TutorialView(5, R.drawable.img_part_shoulder_daytime))
        }

        tutorials.map{ Dlog.d("it : $it")}
        viewPager.adapter = TutorialAdapter(tutorials)

        Dlog.d("currentItem : ${ viewPager.currentItem}")
        indicator[5].setOnClickListener {
            Dlog.d("5")
        }

    }
}
