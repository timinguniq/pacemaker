package com.devjj.pacemaker.features.pacemaker.historydetail


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.extension.observe
import com.devjj.pacemaker.core.extension.viewModel
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

class HistoryDetailFragment(private val intent: Intent) : BaseFragment() {
    override fun layoutId() = R.layout.fragment_history_detail

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var db: ExerciseHistoryDatabase
    @Inject
    lateinit var historyDetailAdapter: HistoryDetailAdapter

    private lateinit var historyDetailViewModel: HistoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        historyDetailViewModel = viewModel(viewModelFactory) {
            observe(historyDetails, ::renderHistoryDetails)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun renderHistoryDetails(historyDetailView: List<HistoryDetailView>?) {
        historyDetailAdapter.collection = historyDetailView.orEmpty()
        fHistoryDetail_tv_achievementRate.text =
            getString(R.string.rh_rate, historyDetailView.orEmpty()[0].achievementRate)

        Log.d("Rate check", "begin")
        for (text in historyDetailView!!) {
            Log.d("Rate check", " ${text.achievementRate}")
        }
    }

    private fun initializeView() {

        historyDetailAdapter.clickListener = { id, date ->
            when (fHistoryDetail_floating_action_btn.isSelected) {
                true -> {
                    Log.d("clicked", "clicked do action")
                    historyDetailViewModel.switchAchievementById(id)
                    historyDetailViewModel.updateAchievementRateByDate(date)
                    historyDetailViewModel.loadHistoryDetails(date)
                  /*  Flowable.just(historyDetailViewModel.updateAchievementRateByDate(date))

                    .observeOn(Schedulers.io())
                    .subscribe {

                    }.isDisposed
*/


                    /*
                    var mutex = Mutex()
                    runBlocking {
                        mutex.lock()
                        historyDetailViewModel.switchAchievementById(id)
                        mutex.unlock()
                        Log.d("Thread :" , Thread.currentThread().name+" 1")
                    }
                    runBlocking {
                        mutex.lock()
                        val b=historyDetailViewModel.updateAchievementRateByDate(date)
                        mutex.unlock()
                        Log.d("Thread :" , Thread.currentThread().name+" 2")
                    }
                    runBlocking {
                        mutex.lock()
                        val c= historyDetailViewModel.loadHistoryDetails(date)
                        mutex.unlock()
                        Log.d("Thread :" , Thread.currentThread().name+" 3")
                    }
                    */

                }
                false -> Log.d("clicked", "clicked no action")
            }
        }


        fHistoryDetail_floating_action_btn?.setOnClickListener {
            if (fHistoryDetail_floating_action_btn.isSelected) {
                fHistoryDetail_floating_action_btn.setImageResource(android.R.drawable.ic_menu_edit)
                fHistoryDetail_floating_action_btn.isSelected = false
                Log.d("clicked", "changes saved")
            } else {
                fHistoryDetail_floating_action_btn.setImageResource(android.R.drawable.ic_menu_save)
                fHistoryDetail_floating_action_btn.isSelected = true
                Log.d("clicked", "entered edit mode")
            }
        }

        fHistoryDetail_recyclerview.layoutManager = LinearLayoutManager(activity)
        fHistoryDetail_recyclerview.adapter = historyDetailAdapter

        val date: String = intent.getStringExtra("date")
        historyDetailViewModel.loadHistoryDetails(date)
    }
}

