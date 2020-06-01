package com.devjj.pacemaker.features.pacemaker.home

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class HomeAdapter
@Inject constructor() : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    @Inject lateinit var context: Context
    @Inject lateinit var setting: SettingSharedPreferences

    internal var collection: List<HomeView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (AdditionView) -> Unit = { _ -> }

    internal var longClickListener: (HomeView) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.recyclerview_exercise_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], context, setting, clickListener, longClickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(homeView: HomeView, context: Context, setting: SettingSharedPreferences,
                 clickListener: (AdditionView) -> Unit, longClickListener: (HomeView) -> Unit) {
            itemView.rvExerciseItem_tv_name.text = String.regLen(homeView.name, EXERCISE_NAME_HOME)
            itemView.rvExerciseItem_tv_mass.text = context.getString(R.string.unit_mass , homeView.mass)
            itemView.rvExerciseItem_tv_set.text = context.getString(R.string.unit_sets, homeView.set)

            if(!setting.isNightMode) {
                // 화이트 모드
                val partImgResource = convertPartImgToResource(homeView.part_img, false)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(loadColor(context, R.color.grey_F9F9F9))
                itemView.rvExerciseItem_tv_name.setTextColor(loadColor(context, R.color.black_3B4046))
                itemView.rvExerciseItem_tv_mass.setTextColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_tv_slash.setTextColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_tv_set.setTextColor(loadColor(context, R.color.grey_88898A))
            }else{
                // 다크 모드
                val partImgResource = convertPartImgToResource(homeView.part_img, true)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_tv_name.setTextColor(loadColor(context, R.color.white_F7FAFD))
                itemView.rvExerciseItem_tv_mass.setTextColor(loadColor(context, R.color.grey_444646))
                itemView.rvExerciseItem_tv_slash.setTextColor(loadColor(context, R.color.grey_444646))
                itemView.rvExerciseItem_tv_set.setTextColor(loadColor(context, R.color.grey_444646))

            }

            // 메인 레이아웃 클릭 시 이벤트 함수.
            itemView.rvExerciseItem_clo_main.setOnClickListener {
                // HomeView에서 AdditionView로 컨버팅하는 함수 필요.
                clickListener(AdditionView(homeView.id, homeView.part_img, homeView.name,
                    homeView.mass, homeView.rep, homeView.set, homeView.interval))
            }

            // 메인 레이아웃 롱 클릭 시 이벤트 함수.
            itemView.rvExerciseItem_clo_main.setOnLongClickListener {
                longClickListener(HomeView(homeView.id, homeView.part_img, homeView.name,
                    homeView.mass, homeView.rep, homeView.set, homeView.interval))
                true
            }
        }
    }
}