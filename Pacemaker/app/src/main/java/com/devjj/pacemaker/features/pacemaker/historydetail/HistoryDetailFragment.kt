package com.devjj.pacemaker.features.pacemaker.historydetail

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.*
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentHistoryDetailBinding
import com.devjj.pacemaker.features.pacemaker.HistoryDetailActivity
import javax.inject.Inject

class HistoryDetailFragment(private val intent: Intent) : BaseFragment() {
    override fun layoutId() = R.layout.fragment_history_detail

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var db: ExerciseHistoryDatabase
    @Inject lateinit var historyDetailAdapter: HistoryDetailAdapter
    @Inject lateinit var setting: SettingSharedPreferences

    private lateinit var historyDetailViewModel: HistoryDetailViewModel
    private lateinit var historyDetailListener : HistoryDetailListener

    private var _binding: FragmentHistoryDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        historyDetailViewModel = viewModel(viewModelFactory) {
            observe(historyDetails, ::renderHistoryDetails)
            observe(statisticsOneDay,::renderStatisticsOneDay)
            observe(oneDaySets, ::renderOneDaySets)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun renderOneDaySets(oneDaySets: OneDaySets?){
        binding.fHistoryDetailTvTotalSets.text = this.getString(R.string.unit_sets, oneDaySets!!.sets )
    }

    private fun renderStatisticsOneDay(statisticsOneDay: StatisticsOneDay?){

        //fHistoryDetail_tv_totalSets.text = this.getString(R.string.unit_sets, statisticsOneDay!!.sets )
        binding.fHistoryDetailTvTotalReps.text = this.getString(R.string.unit_time_hour_min, statisticsOneDay!!.totalTime/60,statisticsOneDay.totalTime%60)
        binding.fHistoryDetailCircleViewRate.setValue(statisticsOneDay.achievementRate.toFloat())
    }

    private fun renderHistoryDetails(historyDetailViews: List<HistoryDetailView>?) {
        historyDetailAdapter.collection = historyDetailViews.orEmpty()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeView() {
        val date: String = intent.getStringExtra("date") ?: ""
        binding.fHistoryDetailIvDrop.setImageDrawable(activity!!.getDrawable(R.drawable.fhistorydetail_img_btn_dropdown_daytime))
        binding.fHistoryDetailIvDrop.tag = R.drawable.fhistorydetail_img_btn_dropdown_daytime

        historyDetailListener = HistoryDetailListener(activity!!, binding, historyDetailAdapter)
        historyDetailListener.initListener()

        setColors()

        binding.fHistoryDetailCircleViewRate.setTextTypeface(Typeface.DEFAULT_BOLD)
        binding.fHistoryDetailCircleViewRate.setUnitTextTypeface(Typeface.DEFAULT_BOLD)

        binding.fHistoryDetailRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.fHistoryDetailRecyclerview.adapter = historyDetailAdapter

        historyDetailViewModel.loadHistoryDetails(date)
        historyDetailViewModel.loadStatisticsOneDay(date)
        historyDetailViewModel.loadOneDaySets(date)
        Dlog.d( "${date.split("-")[0]}")

        (requireActivity() as HistoryDetailActivity).getBinding().aHistoryDetailTvTitle.text = date

    }

    private fun setColors(){
        binding.fHistoryDetailCircleViewRate.rimColor = loadColor(activity!!,R.color.orange_FF765B_70)
        when(setting.isNightMode){
            true->{
                binding.fHistoryDetailCircleViewRate.setBarColor(loadColor(activity!!,R.color.orange_F74938))
                binding.fHistoryDetailCircleViewRate.setTextColor(loadColor(activity!!,R.color.orange_F74938))
                binding.fHistoryDetailTvTotalReps.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryDetailTvTotalSets.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryDetailCloOpenAll.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fHistoryDetailTvOpenAll.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fHistoryDetailIvDrop.drawable.setTint(loadColor(activity!!,R.color.white_F7FAFD))
            }
            false->{
                binding.fHistoryDetailCircleViewRate.setBarColor(loadColor(activity!!,R.color.orange_FF765B))
                binding.fHistoryDetailCircleViewRate.setTextColor(loadColor(activity!!,R.color.orange_FF765B))
                binding.fHistoryDetailTvTotalReps.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryDetailTvTotalSets.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryDetailCloOpenAll.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9_70))
                binding.fHistoryDetailTvOpenAll.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fHistoryDetailIvDrop.drawable.setTint(loadColor(activity!!,R.color.black_3B4046))
            }
        }
    }
}