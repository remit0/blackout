package com.example.blackout.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.charts.Pie
import com.example.blackout.database.Party
import com.example.blackout.database.PartyDatabaseDao
import com.example.blackout.utils.getCurrentWeek
import com.example.blackout.utils.getLastWeek
import com.example.blackout.utils.getWeekStartMilli
import com.example.blackout.utils.timeMilliFormatter
import kotlinx.coroutines.*


class StatsViewModel(private val database: PartyDatabaseDao, application: Application):
    AndroidViewModel(application) {

    /* Data attributes */
    private var _maxScore = MutableLiveData<Double>()
    val maxScore: LiveData<Double> get() = _maxScore

    private var _increase = MutableLiveData<Int>()
    val increase: LiveData<Int> get() = _increase

    /* Coroutines */
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    /* Data for plots */
    // private lateinit var parties: List<Party>

    init {
        getMaxScore()
        getIncrease()
    }


    /*
    * Max score
    */
    private fun getMaxScore() {
        uiScope.launch {
            _maxScore.value = getMaxScoreDatabase()
        }
    }

    private suspend fun getMaxScoreDatabase(): Double{
        return withContext(Dispatchers.IO){
            database.getMaxScore()
        }
    }


    /*
    * Increase
    */
    private fun getIncrease(){
        uiScope.launch {

            /* get timestamps */
            val currWeekStartMilli = getWeekStartMilli(getCurrentWeek())
            val lastWeekStartMilli = getWeekStartMilli(getLastWeek())

            /* retrieve data */
            val lastWeekParties = getPartiesBetweenDatesDB(lastWeekStartMilli, currWeekStartMilli)
            val currWeekParties = getPartiesBetweenDatesDB(currWeekStartMilli, System.currentTimeMillis())

            /* compute stats */
            val lastWeekConsumption = sumScore(lastWeekParties)
            val currWeekConsumption = sumScore(currWeekParties)
            if(lastWeekConsumption != 0.0) {
                _increase.value = (100 * (currWeekConsumption - lastWeekConsumption) / lastWeekConsumption).toInt()
            } else {
                _increase.value = 0
            }
        }
    }

    private suspend fun getPartiesBetweenDatesDB(startTimeMilli: Long, endTimeMilli: Long): List<Party> {
        return withContext(Dispatchers.IO){
            database.getPartiesBetweenDates(startTimeMilli, endTimeMilli)
        }
    }

    private fun sumScore(parties: List<Party>): Double {
        var sum = 0.0
        parties.forEach{
            party -> sum += party.score
        }
        return sum
    }

    /*
    *  Fetch Parties Data
    */
    /*
    private fun initPartiesData(){
        uiScope.launch {
            parties = getAllPartiesDatabase()
        }
    }*/

    private suspend fun getAllPartiesDatabase(): List<Party> {
        return withContext(Dispatchers.IO){
            database.getAllPartiesForCharts()
        }
    }

    /*
    * Pie Chart
    */
    fun getPieChart(): Pie {
        val pie = AnyChart.pie()
        uiScope.launch {
            val parties = getAllPartiesDatabase()
            val data = computePieAggregate(parties)
            val pieData: MutableList<DataEntry> = ArrayList()
            for ((key, value) in data) {
                pieData.add(ValueDataEntry(key, value))
            }
            pie.data(pieData)
        }
        return pie
    }


    private fun computePieAggregate(parties: List<Party>): Map<String, Int> {
        val aggregateCount = mutableMapOf("Beer" to 0, "Wine" to 0, "Hard" to 0)
        parties.forEach { party ->
            incrementMap(aggregateCount, "Beer", party.beerCount)
            incrementMap(aggregateCount, "Wine", party.wineCount)
            incrementMap(aggregateCount, "Hard", party.hardCount)
        }
        return aggregateCount
    }

    private fun incrementMap(map: MutableMap<String, Int>, key: String, increment: Int): MutableMap<String, Int> {
        val currentValue = map[key] ?: 0
        map[key] = currentValue + increment
        return map
    }

    /*
    * Pie Chart
    */
    fun getLineChart(): Cartesian {
        val line = AnyChart.line()
        uiScope.launch {
            val series :MutableList<DataEntry> = ArrayList()
            val parties = getAllPartiesDatabase()
            parties.forEach { party ->
                val x = timeMilliFormatter(party.timeMilli)
                val y = party.score
                series.add(ValueDataEntry(x, y))
            }
            line.data(series)
        }
        return line
    }
}