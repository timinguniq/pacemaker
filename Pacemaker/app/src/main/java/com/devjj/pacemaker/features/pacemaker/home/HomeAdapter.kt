package com.devjj.pacemaker.features.pacemaker.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.convertPartImgToResource
import com.devjj.pacemaker.core.extension.inflate
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class HomeAdapter
@Inject constructor() : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    @Inject lateinit var context: Context

    internal var collection: List<HomeView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (AdditionView) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.recyclerview_exercise_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], context, clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(homeView: HomeView, context: Context, clickListener: (AdditionView) -> Unit) {
            val partImgResource = convertPartImgToResource(homeView.part_img)
            itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
            itemView.rvExerciseItem_tv_name.text = homeView.name
            itemView.rvExerciseItem_tv_mass.text = homeView.mass.toString()
            itemView.rvExerciseItem_tv_rep.text = homeView.rep.toString()
            itemView.rvExerciseItem_tv_set.text = homeView.set.toString()
            itemView.rvExerciseItem_tv_interval.text = homeView.interval.toString()

            // 메인 레이아웃 클릭시 이벤트 함수.
            itemView.rvExerciseItem_clo_main.setOnClickListener {
                // HomeView에서 AdditionView로 컨버팅하는 함수 필요.
                clickListener(AdditionView(homeView.id, homeView.part_img, homeView.name,
                    homeView.mass, homeView.rep, homeView.set, homeView.interval))
            }
        }
    }
}