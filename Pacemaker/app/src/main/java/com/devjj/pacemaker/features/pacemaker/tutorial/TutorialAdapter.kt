package com.devjj.pacemaker.features.pacemaker.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.databinding.RecyclerviewTutorialItemBinding
import com.github.islamkhsh.CardSliderAdapter

class TutorialAdapter(private val tutorials : ArrayList<TutorialView>) : CardSliderAdapter<TutorialAdapter.TutorialHolder>() {

    override fun getItemCount() = tutorials.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialHolder {
        val itemBinding = RecyclerviewTutorialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TutorialHolder(itemBinding.root)
    }

    override fun bindVH(holder: TutorialHolder, position: Int) {
        //TODO bind item object with item layout view
        var tutorial = tutorials[position]
        Dlog.d("bindVh : ${tutorial.tutorial_img}")
        holder.itemView.findViewById<ImageView>(R.id.rvTutorialItem_iv_main).setImageResource(tutorial.tutorial_img)
        //holder.itemView.rvTutorialItem_iv_main.setImageResource(tutorial.tutorial_img)
    }

    class TutorialHolder(view: View) : RecyclerView.ViewHolder(view)
}