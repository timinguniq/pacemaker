package com.devjj.pacemaker.features.pacemaker.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.inflate
import kotlinx.android.synthetic.main.recyclerview_home_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class HomeAdapter
@Inject constructor() : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    @Inject lateinit var context: Context

    internal var collection: List<HomeView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (HomeView) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.recyclerview_home_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], context, clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(homeView: HomeView, context: Context, clickListener: (HomeView) -> Unit) {
            itemView.rvHomeItem_part_iv.setImageResource(homeView.part_img)
            itemView.rvHomeItem_name_tv.text = homeView.name
            itemView.rvHomeItem_mass_tv.text = context.getString(R.string.rh_mass, homeView.mass)
            itemView.rvHomeItem_set_tv.text = context.getString(R.string.rh_set, homeView.set)
            itemView.rvHomeItem_interval_tv.text = context.getString(R.string.rh_interval, homeView.interval)

            // 메인 레이아웃 클릭시 이벤트 함수.
            itemView.rvHomeItem_main_clo.setOnClickListener { clickListener(homeView) }
        }
    }
}