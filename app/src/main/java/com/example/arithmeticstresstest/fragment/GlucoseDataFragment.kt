package com.example.arithmeticstresstest.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.FragmentGlucoseDataBinding
import com.example.arithmeticstresstest.helper.DateHelper
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.math.RoundingMode
import java.text.DecimalFormat
import android.text.format.DateFormat


class GlucoseDataFragment: Fragment() {

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: FragmentGlucoseDataBinding

    private var mAuth: FirebaseAuth? = null

    private var maxGlucose : Float? = null
    private var minGlucose : Float? = null
    private var avgGlucose : Float? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGlucoseDataBinding.inflate(inflater, container, false)

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        val dateHelper = DateHelper()
        val startDate = dateHelper.getWeekStartDate()
        val endDate = dateHelper.getWeekEndDate()

        dataViewModel.getFilteredGlucoseData(mAuth?.currentUser?.uid, startDate!!, endDate!!)

        dataViewModel.filteredGlucoseLevels?.observe(viewLifecycleOwner, Observer {
            val glucoseLevels = ArrayList<Float>()
            val glucoseBeforeType = ArrayList<Float>()
            val glucoseAfterType = ArrayList<Float>()

            val date = ArrayList<String>()
            val days = ArrayList<String>()

            val entries: ArrayList<Entry> = ArrayList()

            val grouped =
                    it.groupBy {  DateFormat.format("E", it.date) }.mapValues {
                        calculateAverage(
                                it.value.map { it.glucoseLevel })
                    }

            var j: Int = 0
            for(data in grouped){
                days.add(data.key.toString())
                entries.add(Entry(j.toFloat(), data.value))
                j++
            }

            var i: Int = 0
            for (data in it) {
                glucoseLevels.add(data.glucoseLevel!!.toFloat())
                if (data.glucoseType == "Before meal") {
                    glucoseBeforeType.add(data.glucoseLevel!!.toFloat())
                } else {
                    glucoseAfterType.add(data.glucoseLevel!!.toFloat())
                }
            }

            maxGlucose = it.maxByOrNull { it.glucoseLevel!! }?.glucoseLevel
            binding.txtMaxGlucose.text = "Max: $maxGlucose mmol"

            minGlucose = it.minByOrNull { it.glucoseLevel!! }?.glucoseLevel
            binding.txtMinGlucose.text = "Min: $minGlucose mmol"

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING

            avgGlucose = calculateAverage(glucoseLevels)
            binding.txtAvgValue.text = "Avg: ${df.format(avgGlucose)} mmol"

            binding.txtMaxValueBefore.text = "Max: ${glucoseBeforeType.maxOrNull()} mmol"
            binding.txtMinGlucoseBefore.text = "Min: ${glucoseBeforeType.minOrNull()} mmol"
            binding.txtAvgValueBefore.text = "Avg: ${df.format(calculateAverage(glucoseBeforeType))} mmol"

            binding.txtMaxValueAfter.text = "Max: ${glucoseAfterType.maxOrNull()} mmol"
            binding.txtMinAfter.text = "Min: ${glucoseBeforeType.minOrNull()} mmol"
            binding.txtAvgAfter.text = "Avg: ${df.format(calculateAverage(glucoseBeforeType))} mmol"

            val lineDataSet: LineDataSet = LineDataSet(entries, "Glucose level")
            lineDataSet.color = Color.RED
            lineDataSet.circleHoleColor =  Color.RED
            lineDataSet.fillColor = Color.RED
            lineDataSet.setCircleColor(Color.RED)
            lineDataSet.circleSize = 5f

            val xAxis: XAxis = binding.lineChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(days)
            binding.lineChart.axisLeft.axisMinimum = 0F
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1F
            xAxis.isGranularityEnabled = true

            binding.lineChart.axisRight.isEnabled = false

            val lineData: LineData = LineData(lineDataSet)
            binding.lineChart.data = lineData
            binding.lineChart.invalidate()
        })

        return binding.root
    }

    private fun calculateAverage(marks: List<Float?>): Float {
        var sum = 0f

        if (marks.isNotEmpty()) {
            for (mark in marks) {
                sum += mark!!
            }
            return sum / marks.size
        }
        return sum
    }

}