package com.devjj.pacemaker.features.pacemaker.historydetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.databinding.RecyclerviewExerciseDetailItemBinding
import javax.inject.Inject
import kotlin.properties.Delegates

class HistoryDetailAdapter
@Inject constructor(val context: Context, val setting: SettingSharedPreferences) : RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>(){

    internal var collection: List<HistoryDetailView> by Delegates.observable(emptyList()){
        _,_,_-> notifyDataSetChanged()
    }
    internal var clickListener: (View,Int,String) -> Unit={ _,_,_->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RecyclerviewExerciseDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(collection[position],context,setting,clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(private val itemBinding: RecyclerviewExerciseDetailItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(historyDetailView: HistoryDetailView, context: Context,setting: SettingSharedPreferences,clickListener :(View,Int,String)->Unit={_,_,_->}){
            val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
            itemBinding.rvExerciseItemIvPart.setImageResource(partImgResource)
            itemBinding.rvExerciseItemTvName.text = String.regLen(historyDetailView.name, EXERCISE_NAME_HISTORY)
           // itemView.rvExerciseItem_tv_mass.text = context.getString(R.string.rvexerciseitem_tv_unit_mass_str, historyDetailView.mass)
           // itemView.rvExerciseItem_tv_set.text = context.getString(R.string.unit_sets_goal_done, historyDetailView.setDone,historyDetailView.setGoal,historyDetailView.rep)
            itemBinding.rvExerciseItemTvMass.text = context.getString(R.string.unit_mass, historyDetailView.mass)
            itemBinding.rvExerciseItemTvSet.text = context.getString(R.string.unit_sets_goal_done, historyDetailView.setDone,historyDetailView.setGoal)
            itemBinding.rvExerciseItemTvRep.text = context.getString(R.string.unit_reps, historyDetailView.rep)
            itemBinding.rvExerciseItemCloMain.setOnClickListener{
                clickListener(itemView,historyDetailView.id ,historyDetailView.date)
            }

            if(setting.isNightMode) {
                val partImgResource = convertPartImgToResource(historyDetailView.part_img, true)
                itemBinding.rvExerciseItemIvPart.setImageResource(partImgResource)
                itemBinding.rvExerciseItemCloMain.setBackgroundColor(loadColor(context,R.color.grey_606060))
                itemBinding.rvExerciseItemTvName.setTextColor(loadColor(context,R.color.white_F7FAFD))

                itemBinding.rvExerciseItemCloDetail.setBackgroundColor(loadColor(context,R.color.grey_F9F9F9_70))
                itemBinding.rvExerciseItemTvMass.setTextColor(loadColor(context,R.color.black_3B4046))
                itemBinding.rvExerciseItemTvSet.setTextColor(loadColor(context,R.color.black_3B4046))
                itemBinding.rvExerciseItemTvRep.setTextColor(loadColor(context,R.color.black_3B4046))

            }else{
                val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
                itemBinding.rvExerciseItemIvPart.setImageResource(partImgResource)
                itemBinding.rvExerciseItemCloMain.setBackgroundColor(loadColor(context,R.color.grey_F9F9F9_70))
                itemBinding.rvExerciseItemTvName.setTextColor(loadColor(context,R.color.black_3B4046))

                itemBinding.rvExerciseItemCloDetail.setBackgroundColor(loadColor(context,R.color.grey_606060))
                itemBinding.rvExerciseItemTvMass.setTextColor(loadColor(context,R.color.white_F7FAFD))
                itemBinding.rvExerciseItemTvSet.setTextColor(loadColor(context,R.color.white_F7FAFD))
                itemBinding.rvExerciseItemTvRep.setTextColor(loadColor(context,R.color.white_F7FAFD))
            }


            //if(historyDetailView.achievement == 1) itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.GREEN) else itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.RED)
        }
    }
}