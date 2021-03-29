package com.example.arithmeticstresstest.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.MyDataBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.DateFormat


class MyDataFragment : Fragment() {

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: MyDataBinding

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = MyDataBinding.inflate(inflater, container, false)

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        dataViewModel.getAllBeforeAndAfterTestResults(mAuth?.currentUser?.uid)

        dataViewModel.data?.observe(viewLifecycleOwner, Observer {

            val entriesBeforeTest: ArrayList<BarEntry> = ArrayList()
            val entriesAfterTest: ArrayList<BarEntry> = ArrayList()

            val date = ArrayList<String>()

            var i: Int = 0
            for (item in it){
                if(item.type == "BeforeTest") {
                    val barEntry = BarEntry(i.toFloat(), item.glucoseLevel!!.toFloat())
                    val test = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(item.date)
                    Log.v("sada", "kako izgleda $test")
                    date.add(test)
                    entriesBeforeTest.add(barEntry)
                    i++
                }/*else{
                    val barEntry = BarEntry(i.toFloat(), item.glucoseLevel!!.toFloat())
                    date.add(item.date.toString())
                    entriesAfterTest.add(barEntry)
                    i++
                }*/
            }

            val barDataSetBeforeTest = BarDataSet(entriesBeforeTest, "Glucose level glucose level before test")
            barDataSetBeforeTest.color = Color.BLUE
            val barDataSetAfterTest = BarDataSet(entriesAfterTest, "Glucose level after test")
            barDataSetAfterTest.color = Color.RED

            val data = BarData( barDataSetBeforeTest, barDataSetAfterTest)
            binding.barchart.data = data

            val xAxis = binding.barchart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM;
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawLabels(true);
            xAxis.labelCount = date.size // important
            xAxis.setSpaceMax(0.5f) // optional
            xAxis.setSpaceMin(0.5f) // optional
            xAxis.valueFormatter = object : ValueFormatter() {
                override
                fun getFormattedValue(value: Float): String {
                    // value is x as index
                    return date[value.toInt()]
                }
            }

            binding.barchart.invalidate()

        })

        return binding.root
    }
}