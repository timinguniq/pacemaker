package com.devjj.pacemaker.features.pacemaker.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.get

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.invisible
import com.devjj.pacemaker.core.extension.visible
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.github.islamkhsh.doOnPageSelected
import kotlinx.android.synthetic.main.fragment_tutorial.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TutorialFragment : BaseFragment() {

    @Inject lateinit var navigator: Navigator

    override fun layoutId() = R.layout.fragment_tutorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        // 버튼 숨기기.
        fTutorial_btn.invisible()
    }

    override fun onResume() {
        super.onResume()
        // 데이터 초기화
        val tutorials = ArrayList<TutorialView>().apply{
            // add items to arraylist
            add(TutorialView(0, R.drawable.img_part_upper_body_daytime))
            add(TutorialView(0, R.drawable.img_part_abdomen_daytime))
            add(TutorialView(0, R.drawable.img_part_arm_daytime))
            add(TutorialView(0, R.drawable.img_part_chest_daytime))
            add(TutorialView(0, R.drawable.img_part_lower_body_daytime))
            add(TutorialView(0, R.drawable.img_part_shoulder_daytime))
        }

        tutorials.map{ Dlog.d("it : $it")}
        fTutorial_viewpager.adapter = TutorialAdapter(tutorials)

        fTutorial_viewpager.doOnPageSelected {
            if(fTutorial_viewpager.currentItem == 5){
                Dlog.d("currentItem 5")
                fTutorial_btn.visible()
            }else{
                fTutorial_btn.invisible()
            }
        }

        fTutorial_btn.setOnClickListener {
            navigator.showPacemaker(activity!!)
        }

    }
}
