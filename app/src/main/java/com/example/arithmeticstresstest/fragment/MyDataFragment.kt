package com.example.arithmeticstresstest.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.activity.GlucoseFormActivity
import com.example.arithmeticstresstest.activity.ResultActivity
import com.example.arithmeticstresstest.activity.SignInActivity
import com.example.arithmeticstresstest.activity.SmartDeviceFormActivity
import com.example.arithmeticstresstest.adapter.MyAdapter
import com.example.arithmeticstresstest.databinding.MyDataBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
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

        binding.btnInsertGlucose.setOnClickListener{
            insertGlucose()
        }

        binding.btnInsertSmartDeviceData.setOnClickListener{
            insertSmartData()
        }

        binding.tabLayout!!.addTab(binding.tabLayout!!.newTab())
        binding.tabLayout!!.addTab(binding.tabLayout!!.newTab())
        binding.tabLayout!!.addTab( binding.tabLayout!!.newTab())
        binding.tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = MyAdapter(this, requireFragmentManager(),  binding.tabLayout!!.tabCount)
        binding.pager!!.adapter = adapter

        binding.pager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })


        return binding.root
    }

    private fun insertGlucose(){
        val intent = Intent(activity, GlucoseFormActivity::class.java)
        startActivity(intent)
    }

    private fun insertSmartData(){
        val intent = Intent(activity, SmartDeviceFormActivity::class.java)
        startActivity(intent)
    }
}