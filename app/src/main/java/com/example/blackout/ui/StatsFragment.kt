package com.example.blackout.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.example.blackout.R
import com.example.blackout.database.PartyDatabase
import com.example.blackout.databinding.FragmentStatsBinding
import com.example.blackout.viewmodels.StatsViewModel


class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* Application reference for accessing database */
        val application = requireNotNull(this.activity).application

        /* Data source */
        val dataSource = PartyDatabase.getInstance(application).partyDatabaseDao

        /* Data binding object */
        val binding: FragmentStatsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stats, container, false)

        /* ViewModel interactions & hook-up binding */
        val viewModel = StatsViewModel(dataSource, application)
        binding.statsViewModel = viewModel
        binding.lifecycleOwner = this

        /* charting */
        /*
        APIlib.getInstance().setActiveAnyChartView(binding.pieChart)
        val pie = viewModel.getPieChart()
        pie.background().enabled(true).fill("#00000c")
        binding.pieChart.setChart(pie)
        */

        APIlib.getInstance().setActiveAnyChartView(binding.lineChart)
        val line = viewModel.getLineChart()
        line.background().enabled(true).fill("#00000c")
        binding.lineChart.setChart(line)

        return binding.root
    }


}
