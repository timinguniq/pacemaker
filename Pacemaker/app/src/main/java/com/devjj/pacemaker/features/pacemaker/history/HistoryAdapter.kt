package com.devjj.pacemaker.features.pacemaker.history

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.inflate
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailView
import kotlinx.android.synthetic.main.recyclerview_exercise_history_item.view.*
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class HistoryAdapter
@Inject constructor() : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    @Inject lateinit var context: Context

    internal var collection: List<HistoryView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (String) -> Unit = { _-> }

    override fun getItemCount() = collection.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(collection[position], context, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.recyclerview_exercise_history_item))


    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(historyView: HistoryView , context: Context, clickListener: (String) -> Unit ){
            itemView.rvExerciseHistoryItem_tv_date.text = historyView.date
            itemView.rvExerciseHistoryItem_tv_rate.text = context.getString(R.string.rh_rate,historyView.achivementRate)
            itemView.rvExerciseHistoryItem_pb_rate.setProgress(historyView.achivementRate)

            itemView.rvExerciseHistoryItem_clo_main.setOnClickListener{
                clickListener(historyView.date)
            }
        }
    }
}