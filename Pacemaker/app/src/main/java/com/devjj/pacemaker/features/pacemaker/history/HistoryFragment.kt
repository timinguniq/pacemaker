package com.devjj.pacemaker.features.pacemaker.history

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_history.*
import java.text.SimpleDateFormat

import java.util.*
import javax.inject.Inject

class HistoryFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_history

    private lateinit var historyViewModel: HistoryViewModel
    @Inject lateinit var db : ExerciseHistoryDatabase
    @Inject lateinit var historyAdapter : HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        historyViewModel = viewModel(viewModelFactory) {
            observe(histories, ::renderHistoryList)
        //    failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

    }

    private fun renderHistoryList(histories : List<HistoryView>?){
        historyAdapter.collection = histories.orEmpty()
    }


    fun initializeView(){
        fHistory_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistory_recyclerview.adapter = historyAdapter


        val date = Calendar.getInstance()
        date.time = Date()
        //Log.d("dddd",SimpleDateFormat("yyyy-MM-dd").format(date.time))


        val a = ExerciseHistoryEntity(0,
            SimpleDateFormat("yyyy-MM-dd").format(date.time),
            1,
            "벤치프레스",
            20,
            3,
            40,
            true,
            30
            )

        val b = ExerciseHistoryEntity(1,
            SimpleDateFormat("yyyy-MM-dd").format(date.time),
            2,
            "레그레이즈",
            50,
            3,
            40,
            false,
            50
        )

        Flowable.just("abc")
            .subscribeOn(Schedulers.io())
            .subscribe{
                db.ExerciseHistoryDAO().insert(a,b)
            }.isDisposed

        historyViewModel.loadHistories()
    }

}
