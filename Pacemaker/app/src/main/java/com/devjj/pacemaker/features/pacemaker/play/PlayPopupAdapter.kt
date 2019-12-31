package com.devjj.pacemaker.features.pacemaker.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.inflate
import kotlinx.android.synthetic.main.recyclerview_playpopup_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class PlayPopupAdapter
@Inject constructor() : RecyclerView.Adapter<PlayPopupAdapter.ViewHolder>() {

    internal var collection: List<PlayPopupView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (PlayPopupView) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.recyclerview_playpopup_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(playPopupView: PlayPopupView, clickListener: (PlayPopupView) -> Unit) {
            val isCheck = playPopupView.achivement != 0
            itemView.rvPlayPopupItem_cb_achivement.isChecked = isCheck
            itemView.rvPlayPopupItem_tv_name.text = playPopupView.name
            itemView.rvPlayPopupItem_tv_mass.text = playPopupView.mass.toString()
            itemView.rvPlayPopupItem_tv_rep.text = playPopupView.rep.toString()

        }
    }
}