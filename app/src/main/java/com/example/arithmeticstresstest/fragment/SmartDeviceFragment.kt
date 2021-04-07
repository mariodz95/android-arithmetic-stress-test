package com.example.arithmeticstresstest.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.FragmentSmartDeviceBinding
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


class SmartDeviceFragment : Fragment(){

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: FragmentSmartDeviceBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSmartDeviceBinding.inflate(inflater, container, false)

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        val fromDate = (parentFragment as MyDataFragment).getFromDate()
        val toDate = (parentFragment as MyDataFragment).getToDate()

        mAuth = FirebaseAuth.getInstance()
        dataViewModel.getSmartDeviceData(mAuth?.currentUser?.uid, fromDate!!, toDate!!)

        dataViewModel.smartDeviceData?.observe(viewLifecycleOwner, Observer {
            Log.v("sada", "IT IT $it")

            val smartDeviceData = ArrayList<Float>()

            val date = ArrayList<String>()
            val days = ArrayList<String>()

            val entries: ArrayList<Entry> = ArrayList()

            val grouped =
                    it.groupBy {  DateFormat.format("E", it.dateTime) }.mapValues {
                        calculateSum(
                                it.value.map { it.steps })
                    }

            var j: Int = 0
            for(data in grouped){
                days.add(data.key.toString())
                entries.add(Entry(j.toFloat(), data.value))
                j++
            }


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


            Log.v("sada", "Grouped $grouped")
        })



        return binding.root
    }

    private fun calculateSum(items: List<Int?>): Float {
        var sum = 0f

        if (items.isNotEmpty()) {
            for (item in items) {
                sum += item!!
            }
            return sum
        }
        return sum
    }
}