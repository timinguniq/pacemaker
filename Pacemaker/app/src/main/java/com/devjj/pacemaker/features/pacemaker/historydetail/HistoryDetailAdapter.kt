package com.devjj.pacemaker.features.pacemaker.historydetail

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.convertPartImgToResource
import com.devjj.pacemaker.core.extension.inflate
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.fragment_history_detail.view.*
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class HistoryDetailAdapter
@Inject constructor(val context: Context,val setting: SettingSharedPreferences ) : RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>(){

    internal var collection: List<HistoryDetailView> by Delegates.observable(emptyList()){
        _,_,_-> notifyDataSetChanged()
    }
    internal var clickListener: (Int,String) -> Unit={ _,_->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.recyclerview_exercise_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(collection[position],context,setting,clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(historyDetailView: HistoryDetailView, context: Context,setting: SettingSharedPreferences,clickListener :(Int,String)->Unit={_,_->}){
            val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
            itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
            itemView.rvExerciseItem_tv_name.text = historyDetailView.name
            itemView.rvExerciseItem_tv_mass.text = context.getString(R.string.rvExerciseItem_mass, historyDetailView.mass)
            //itemView.rvExerciseItem_tv_rep.text = historyDetailView.rep.toString()
            itemView.rvExerciseItem_tv_set.text = context.getString(R.string.rvExerciseItem_set, historyDetailView.set)
            //itemView.rvExerciseItem_tv_interval.text = historyDetailView.interval.toString()

            itemView.rvExerciseItem_clo_main.setOnClickListener{
                clickListener(historyDetailView.id ,historyDetailView.date)
            }

            if(setting.isNightMode) {
                val partImgResource = convertPartImgToResource(historyDetailView.part_img, true)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(context.getColor(R.color.grey_bg_thick))
                itemView.rvExerciseItem_tv_name.setTextColor(context.getColor(R.color.white_txt_thick))
                itemView.rvExerciseItem_tv_mass.setTextColor(context.getColor(R.color.black_txt_thick))
                itemView.rvExerciseItem_tv_slash.setTextColor(context.getColor(R.color.black_txt_thick))
                itemView.rvExerciseItem_tv_set.setTextColor(context.getColor(R.color.black_txt_thick))
            }else{
                val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(context.getColor(R.color.grey_bg_light))
                itemView.rvExerciseItem_tv_name.setTextColor(context.getColor(R.color.black_txt_thick))
                itemView.rvExerciseItem_tv_mass.setTextColor(context.getColor(R.color.grey_txt_light))
                itemView.rvExerciseItem_tv_slash.setTextColor(context.getColor(R.color.grey_txt_light))
                itemView.rvExerciseItem_tv_set.setTextColor(context.getColor(R.color.grey_txt_light))

            }


            //if(historyDetailView.achievement == 1) itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.GREEN) else itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.RED)
        }
    }
}