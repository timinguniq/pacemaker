package com.devjj.pacemaker.features.pacemaker.home

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.functional.OnStartDragListener
import com.devjj.pacemaker.features.pacemaker.addition.AdditionView
import kotlinx.android.synthetic.main.recyclerview_exercise_item.view.*
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

class HomeAdapter(
    private val startDragListener: OnStartDragListener,
    private val context: Context, private val setting: SettingSharedPreferences,
    private val homeViewModel: HomeViewModel
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>(),
    HomeItemMoveCallbackListener.Listener {

    //@Inject lateinit var context: Context
    //@Inject lateinit var setting: SettingSharedPreferences

    internal var collection: List<HomeView> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (AdditionView) -> Unit = { _ -> }

    internal var deleteClickListener: (HomeView) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.recyclerview_exercise_item))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(
            collection[position],
            context,
            setting,
            startDragListener,
            clickListener,
            deleteClickListener
        )
    }

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            homeView: HomeView, context: Context, setting: SettingSharedPreferences,
            startDragListener: OnStartDragListener,
            clickListener: (AdditionView) -> Unit, deleteClickListener: (HomeView) -> Unit
        ) {
            itemView.rvExerciseItem_tv_name.text = String.regLen(homeView.name, EXERCISE_NAME_HOME)
            itemView.rvExerciseItem_tv_mass.text =
                context.getString(R.string.unit_mass, homeView.mass)
            itemView.rvExerciseItem_tv_set.text =
                context.getString(R.string.unit_sets, homeView.set)
            if (!setting.isNightMode) {
                // 화이트 모드
                val partImgResource = convertPartImgToResource(homeView.part_img, false)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(loadColor(context, R.color.grey_F9F9F9))
                itemView.rvExerciseItem_tv_name.setTextColor(loadColor(context, R.color.black_3B4046))
                itemView.rvExerciseItem_tv_mass.setTextColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_tv_slash.setTextColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_tv_set.setTextColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_iv_sort.setImageResource(R.drawable.rvexerciseitem_img_wm_drag_handle)
                itemView.rvExerciseItem_iv_delete.setImageResource(R.drawable.rvexerciseitem_img_wm_delete)
            } else {
                // 다크 모드
                val partImgResource = convertPartImgToResource(homeView.part_img, true)
                itemView.rvExerciseItem_iv_part.setImageResource(partImgResource)
                itemView.rvExerciseItem_clo_main.setBackgroundColor(loadColor(context, R.color.grey_88898A))
                itemView.rvExerciseItem_tv_name.setTextColor(loadColor(context, R.color.white_F7FAFD))
                itemView.rvExerciseItem_tv_mass.setTextColor(loadColor(context, R.color.grey_444646))
                itemView.rvExerciseItem_tv_slash.setTextColor(loadColor(context, R.color.grey_444646))
                itemView.rvExerciseItem_tv_set.setTextColor(loadColor(context, R.color.grey_444646))
                itemView.rvExerciseItem_iv_sort.setImageResource(R.drawable.rvexerciseitem_img_dm_drag_handle)
                itemView.rvExerciseItem_iv_delete.setImageResource(R.drawable.rvexerciseitem_img_dm_delete)
            }

            if (setting.isEditMode) {
                itemView.rvExerciseItem_flo_sort.visible()
                itemView.rvExerciseItem_flo_delete.visible()

                itemView.rvExerciseItem_tv_mass.gone()
                itemView.rvExerciseItem_tv_slash.gone()
                itemView.rvExerciseItem_tv_set.gone()
            } else {
                itemView.rvExerciseItem_flo_sort.gone()
                itemView.rvExerciseItem_flo_delete.gone()

                itemView.rvExerciseItem_tv_mass.visible()
                itemView.rvExerciseItem_tv_slash.visible()
                itemView.rvExerciseItem_tv_set.visible()
            }

            // 메인 레이아웃 클릭 시 이벤트 함수.
            itemView.rvExerciseItem_clo_main.setOnClickListener {
                // HomeView에서 AdditionView로 컨버팅하는 함수 필요.
                clickListener(
                    AdditionView(
                        homeView.id, homeView.part_img, homeView.name,
                        homeView.mass, homeView.rep, homeView.set, homeView.interval
                    )
                )
            }
/*
            // 메인 레이아웃 롱 클릭 시 이벤트 함수.
            itemView.rvExerciseItem_clo_main.setOnLongClickListener {
                deleteClickListener(
                    HomeView(
                        homeView.id, homeView.part_img, homeView.name,
                        homeView.mass, homeView.rep, homeView.set, homeView.interval
                    )
                )
                true
            }
*/
            // Item Delete를 위한 이벤트 함수.
            itemView.rvExerciseItem_flo_delete.setOnClickListener {
                deleteClickListener(
                    HomeView(
                        homeView.id, homeView.part_img, homeView.name,
                        homeView.mass, homeView.rep, homeView.set, homeView.interval
                    )
                )
            }

            // Item Move를 위한 이벤트 함수.
            itemView.rvExerciseItem_flo_sort.setOnTouchListener { view, event ->
                view.performClick()
                if (event.action == MotionEvent.ACTION_DOWN) {
                    startDragListener.onStartDrag(this)
                }
                return@setOnTouchListener true
            }
        }
    }

    private fun swapCollectionId(fromPosition: Int, toPosition: Int){
        val tempId1 = collection[fromPosition].id
        val tempId2 = collection[toPosition].id
        collection[fromPosition].id = tempId2
        collection[toPosition].id = tempId1
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(collection, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(collection, i, i - 1)
            }
        }
        Dlog.d("onRowMoved collection fromPosition : ${collection[fromPosition].id}")
        Dlog.d("onRowMoved collection toPosition : ${collection[toPosition].id}")
        Dlog.d("onRowMoved fromPosition : $fromPosition")
        Dlog.d("onRowMoved toPosition : $toPosition")
        homeViewModel.swapExerciseData(collection[fromPosition].id, collection[toPosition].id)
        notifyItemMoved(fromPosition, toPosition)
        swapCollectionId(fromPosition, toPosition)
    }

    override fun onRowSelected(itemViewHolder: HomeAdapter.ViewHolder) {
    }

    override fun onRowClear(itemViewHolder: HomeAdapter.ViewHolder) {
    }
}