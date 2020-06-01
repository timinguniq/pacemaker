package com.devjj.pacemaker.features.pacemaker.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.functional.Dlog
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.recyclerview_tutorial_item.view.*

class TutorialAdapter(private val tutorials : ArrayList<TutorialView>) : CardSliderAdapter<TutorialAdapter.TutorialHolder>() {

    override fun getItemCount() = tutorials.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_tutorial_item, parent, false)
        return TutorialHolder(view)
    }

    override fun bindVH(holder: TutorialHolder, position: Int) {
        //TODO bind item object with item layout view
        var tutorial = tutorials[position]
        Dlog.d("bindVh : ${tutorial.tutorial_img}")
        holder.itemView.rvTutorialItem_iv_main.setImageResource(tutorial.tutorial_img)
        /*
        holder.itemView.rvTutorialItem_iv_main.setOnClickListener {
            if(position == 5){
                Dlog.d("main clicked")
            }
        }
        */
    }

    class TutorialHolder(view: View) : RecyclerView.ViewHolder(view)
}