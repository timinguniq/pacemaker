package com.devjj.pacemaker.features.pacemaker.opensource

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.databinding.RecyclerviewOpensourceLicenseItemBinding
import javax.inject.Inject
import kotlin.properties.Delegates

class OpenSourceAdapter
@Inject constructor(val context : Context, val setting: SettingSharedPreferences) : RecyclerView.Adapter<OpenSourceAdapter.ViewHolder>(){
    internal var collection: List<OpenSourceView> by Delegates.observable(emptyList()){
            _,_,_-> notifyDataSetChanged()
    }
    //internal var clickListener: (View) -> Unit={ _->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RecyclerviewOpensourceLicenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(collection[position],context,setting)

    override fun getItemCount() = collection.size

    class ViewHolder(private val itemBinding: RecyclerviewOpensourceLicenseItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(openSourceView: OpenSourceView,context : Context, setting: SettingSharedPreferences){
            itemBinding.rvOpenSourceItemTvName.text = openSourceView.name
            itemBinding.rvOpenSourceItemTvAuthor.text = openSourceView.author
            itemBinding.rvOpenSourceItemTvLicense.text = openSourceView.license

            when(setting.isNightMode){
                true -> {
                    //itemView.rvOpenSourceItem_clo_copyright.setBackgroundColor(loadColor(context,R.color.grey_606060))
                    itemBinding.rvOpenSourceItemTvName.setTextColor(loadColor(context,R.color.white_F7FAFD))
                    itemBinding.rvOpenSourceItemTvAuthor.setTextColor(loadColor(context,R.color.white_F7FAFD))
                    itemBinding.rvOpenSourceItemTvLicense.setTextColor(loadColor(context,R.color.white_F7FAFD))
                }
                false -> {
                    //itemView.rvOpenSourceItem_clo_copyright.setBackgroundColor(loadColor(context,R.color.grey_F9F9F9_70))
                    itemBinding.rvOpenSourceItemTvName.setTextColor(loadColor(context,R.color.black_3B4046))
                    itemBinding.rvOpenSourceItemTvAuthor.setTextColor(loadColor(context,R.color.black_3B4046))
                    itemBinding.rvOpenSourceItemTvLicense.setTextColor(loadColor(context,R.color.black_3B4046))
                }
            }
        }
    }
}