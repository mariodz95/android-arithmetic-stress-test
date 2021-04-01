package com.example.arithmeticstresstest.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.FragmentTestDataBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.DateFormat

class TestDataFragment : Fragment() {

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: FragmentTestDataBinding

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTestDataBinding.inflate(inflater, container, false)


        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        dataViewModel.getAllTestData(mAuth?.currentUser?.uid)

        dataViewModel.data?.observe(viewLifecycleOwner, Observer {
            val entriesBeforeTest: ArrayList<BarEntry> = ArrayList()
            val entriesAfterTest: ArrayList<BarEntry> = ArrayList()

            val date = ArrayList<String>()

            var i: Int = 0
            for (item in it) {
                var glucoseBefore: Float = 0f
                if (item.glucoseLevelBeforeTest != null) {
                    glucoseBefore = item.glucoseLevelBeforeTest!!.toFloat()
                }
                val barEntryBefore = BarEntry(i.toFloat(), glucoseBefore)
                entriesBeforeTest.add(barEntryBefore)

                var glucoseAfter: Float = 0f
                if (item.glucoseLevelAfterTest != null) {
                    glucoseAfter = item.glucoseLevelAfterTest!!.toFloat()
                }
                val barEntryAfter = BarEntry((i).toFloat(), glucoseAfter)
                entriesAfterTest.add(barEntryAfter)

                val dateData =
                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(
                                item.insertedDate
                        )
                date.add(dateData)

                i++
            }

            val barDataSetBeforeTest = BarDataSet(
                    entriesBeforeTest,
                    "Glucose levelbefore test"
            )
            barDataSetBeforeTest.color = Color.BLUE

            val barDataSetAfterTest = BarDataSet(entriesAfterTest, "Glucose level after test")
            barDataSetAfterTest.color = Color.RED

            val data = BarData(barDataSetBeforeTest, barDataSetAfterTest)
            binding.barchart.data = data

            val xAxis: XAxis = binding.barchart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(date)
            binding.barchart.axisLeft.axisMinimum = 0F
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 0.5F
            xAxis.isGranularityEnabled = true
            xAxis.labelRotationAngle = 45f

            val barSpace = 0.02f
            val groupSpace = 0.3f
            val groupCount = date.size

            data.barWidth = 0.15f
            binding.barchart.xAxis.axisMinimum = 0f
            binding.barchart.xAxis.axisMaximum = 0 + binding.barchart.barData.getGroupWidth(
                    groupSpace,
                    barSpace
            ) * groupCount

            binding.barchart.groupBars(0f, groupSpace, barSpace)
            xAxis.setCenterAxisLabels(true)

            binding.barchart.invalidate()
        })

        return binding.root
    }

}