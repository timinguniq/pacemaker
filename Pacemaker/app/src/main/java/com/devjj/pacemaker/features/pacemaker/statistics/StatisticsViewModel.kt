package com.devjj.pacemaker.features.pacemaker.statistics

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.interactor.UseCase.None
import com.devjj.pacemaker.core.platform.BaseViewModel
import com.devjj.pacemaker.features.pacemaker.usecases.GetStatistics
import javax.inject.Inject

class StatisticsViewModel
@Inject constructor(
    private val setting : SettingSharedPreferences,
    private val getStatistics: GetStatistics
): BaseViewModel() {
    var statistics : MutableLiveData<List<StatisticsView>> = MutableLiveData()

    fun loadStatistics() = getStatistics(None()){ it.fold(::handleFailure , ::handleStatistics) }

    private fun handleStatistics(statistics: List<Statistics>){
        when(setting.isMonthly) {
            false-> {
                this.statistics.value = statistics.map {
                    StatisticsView(it.date.substring(5,it.date.length), it.height, it.weight)
                }
            }
            true->{
                val map = statistics.groupBy { it.date.substring(0,7)}

                var perMonthList = mutableListOf<StatisticsView>()
                for( key in map.keys){
                    var list = map.getValue(key)
                    var averageHeight = 0f
                    var averageWeight = 0f
                    for( item in list){
                        averageHeight += item.height
                        averageWeight += item.weight
                    }
                    averageHeight /= list.size.toFloat()
                    averageWeight /= list.size.toFloat()

                    perMonthList.add(StatisticsView(key.substring(2,key.length),averageHeight,averageWeight))
                }
                this.statistics.value = perMonthList
                //Dlog.d(this.statisticsPerMonth.value.toString())
            }
        }
    }
}
