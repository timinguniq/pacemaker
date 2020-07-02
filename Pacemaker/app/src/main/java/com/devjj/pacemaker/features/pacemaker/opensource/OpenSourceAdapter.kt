package com.devjj.pacemaker.features.pacemaker.opensource

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.inflate
import com.devjj.pacemaker.core.extension.loadColor
import kotlinx.android.synthetic.main.recyclerview_opensource_license_item.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class OpenSourceAdapter
@Inject constructor(val context : Context, val setting: SettingSharedPreferences) : RecyclerView.Adapter<OpenSourceAdapter.ViewHolder>(){
    internal var collection: List<OpenSourceView> by Delegates.observable(emptyList()){
            _,_,_-> notifyDataSetChanged()
    }
    //internal var clickListener: (View) -> Unit={ _->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.recyclerview_opensource_license_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(collection[position],context,setting)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(openSourceView: OpenSourceView,context : Context, setting: SettingSharedPreferences){
            itemView.rvOpenSourceItem_tv_name.text = openSourceView.name
            itemView.rvOpenSourceItem_tv_author.text = openSourceView.author
            itemView.rvOpenSourceItem_tv_license.text = openSourceView.license

            when(setting.isNightMode){
                true -> {
                    //itemView.rvOpenSourceItem_clo_copyright.setBackgroundColor(loadColor(context,R.color.grey_606060))
                    itemView.rvOpenSourceItem_tv_name.setTextColor(loadColor(context,R.color.white_F7FAFD))
                    itemView.rvOpenSourceItem_tv_author.setTextColor(loadColor(context,R.color.white_F7FAFD))
                    itemView.rvOpenSourceItem_tv_license.setTextColor(loadColor(context,R.color.white_F7FAFD))
                }
                false -> {
                    //itemView.rvOpenSourceItem_clo_copyright.setBackgroundColor(loadColor(context,R.color.grey_F9F9F9_70))
                    itemView.rvOpenSourceItem_tv_name.setTextColor(loadColor(context,R.color.black_3B4046))
                    itemView.rvOpenSourceItem_tv_author.setTextColor(loadColor(context,R.color.black_3B4046))
                    itemView.rvOpenSourceItem_tv_license.setTextColor(loadColor(context,R.color.black_3B4046))
                }
            }
        }
    }
}