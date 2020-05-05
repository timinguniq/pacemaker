package com.devjj.pacemaker.features.pacemaker.historydetail

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.convertPartImgToResource
import com.devjj.pacemaker.core.extension.gone
import com.devjj.pacemaker.core.extension.inflate
import com.devjj.pacemaker.core.extension.visible
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.fragment_history_detail.view.*
import kotlinx.android.synthetic.main.recyclerview_exercise_detail_item.view.*
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.rvExerciseItem_clo_main
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.rvExerciseItem_iv_part
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.rvExerciseItem_tv_mass
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.rvExerciseItem_tv_name
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.rvExerciseItem_tv_set
import javax.inject.Inject
import kotlin.properties.Delegates

class HistoryDetailAdapter
@Inject constructor(val context: Context, val setting: SettingSharedPreferences) : RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>(){

    internal var collection: List<HistoryDetailView> by Delegates.observable(emptyList()){
        _,_,_-> notifyDataSetChanged()
    }
    internal var clickListener: (View,Int,String) -> Unit={ _,_,_->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.recyclerview_exercise_detail_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(collection[position],context,setting,clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(historyDetailView: HistoryDetailView, context: Context,setting: SettingSharedPreferences,clickListener :(View,Int,String)->Unit={_,_,_->}){
            val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
            itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
            itemView.rvExerciseItem_tv_name.text = historyDetailView.name
           // itemView.rvExerciseItem_tv_mass.text = context.getString(R.string.rvexerciseitem_tv_unit_mass_str, historyDetailView.mass)
           // itemView.rvExerciseItem_tv_set.text = context.getString(R.string.unit_sets_goal_done, historyDetailView.setDone,historyDetailView.setGoal,historyDetailView.rep)
            itemView.rvExerciseItem_tv_mass.text = context.getString(R.string.unit_mass, historyDetailView.mass)
            itemView.rvExerciseItem_tv_set.text = context.getString(R.string.unit_sets_goal_done, historyDetailView.setDone,historyDetailView.setGoal)
            itemView.rvExerciseItem_tv_rep.text = context.getString(R.string.unit_reps, historyDetailView.rep)
            itemView.rvExerciseItem_clo_main.setOnClickListener{
                clickListener(itemView,historyDetailView.id ,historyDetailView.date)
            }

            if(setting.isNightMode) {
                val partImgResource = convertPartImgToResource(historyDetailView.part_img, true)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(context.getColor(R.color.grey_606060))
                itemView.rvExerciseItem_tv_name.setTextColor(context.getColor(R.color.white_F7FAFD))

                itemView.rvExerciseItem_clo_detail.setBackgroundColor(context.getColor(R.color.grey_F9F9F9_70))
                itemView.rvExerciseItem_tv_mass.setTextColor(context.getColor(R.color.black_3B4046))
                itemView.rvExerciseItem_tv_set.setTextColor(context.getColor(R.color.black_3B4046))
                itemView.rvExerciseItem_tv_rep.setTextColor(context.getColor(R.color.black_3B4046))

            }else{
                val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(context.getColor(R.color.grey_F9F9F9_70))
                itemView.rvExerciseItem_tv_name.setTextColor(context.getColor(R.color.black_3B4046))

                itemView.rvExerciseItem_clo_detail.setBackgroundColor(context.getColor(R.color.grey_606060))
                itemView.rvExerciseItem_tv_mass.setTextColor(context.getColor(R.color.white_F7FAFD))
                itemView.rvExerciseItem_tv_set.setTextColor(context.getColor(R.color.white_F7FAFD))
                itemView.rvExerciseItem_tv_rep.setTextColor(context.getColor(R.color.white_F7FAFD))
            }


            //if(historyDetailView.achievement == 1) itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.GREEN) else itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.RED)
        }
    }
}