package com.devjj.pacemaker.features.pacemaker.historydetail

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.convertPartImgToResource
import com.devjj.pacemaker.core.extension.inflate
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.fragment_history_detail.view.*
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class HistoryDetailAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>(){

    internal var collection: List<HistoryDetailView> by Delegates.observable(emptyList()){
        _,_,_-> notifyDataSetChanged()
    }
    internal var clickListener: (Int,String) -> Unit={ _,_->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.recyclerview_exercise_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(collection[position],context,clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(historyDetailView: HistoryDetailView, context: Context,clickListener :(Int,String)->Unit={_,_->}){
            val partImgResource = convertPartImgToResource(historyDetailView.part_img, false)
            itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
            itemView.rvExerciseItem_tv_name.text = historyDetailView.name
            itemView.rvExerciseItem_tv_mass.text = historyDetailView.mass.toString()
            //itemView.rvExerciseItem_tv_rep.text = historyDetailView.rep.toString()
            itemView.rvExerciseItem_tv_set.text = historyDetailView.set.toString()
            //itemView.rvExerciseItem_tv_interval.text = historyDetailView.interval.toString()

            itemView.rvExerciseItem_clo_main.setOnClickListener{
                clickListener(historyDetailView.id ,historyDetailView.date)
            }

            if(historyDetailView.achievement == 1) itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.GREEN) else itemView.rvExerciseItem_clo_main.setBackgroundColor(Color.RED)
        }
    }
}