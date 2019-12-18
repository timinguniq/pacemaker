package com.devjj.pacemaker.features.pacemaker.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ComplexColorCompat
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.extension.inflate
import kotlinx.android.synthetic.main.recyclerview_home.view.*
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
        ViewHolder(parent.inflate(R.layout.recyclerview_home))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], context, clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(homeView: HomeView, context: Context, clickListener: (HomeView) -> Unit) {
            itemView.rh_part_img_imv.setImageResource(homeView.part_img)
            itemView.rh_name_txv.text = homeView.name
            itemView.rh_mass_txv.text = context.getString(R.string.rh_mass, homeView.mass)
            itemView.rh_set_txv.text = context.getString(R.string.rh_set, homeView.set)
            itemView.rh_interval_txv.text = context.getString(R.string.rh_interval, homeView.interval)

            // 메인 레이아웃 클릭시 이벤트 함수.
            itemView.rh_main_constraint.setOnClickListener { clickListener(homeView) }
        }
    }
}