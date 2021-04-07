package com.example.arithmeticstresstest.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.activity.GlucoseFormActivity
import com.example.arithmeticstresstest.activity.SmartDeviceFormActivity
import com.example.arithmeticstresstest.adapter.MyAdapter
import com.example.arithmeticstresstest.databinding.MyDataBinding
import com.example.arithmeticstresstest.helper.DateHelper
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class MyDataFragment : Fragment() {

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: MyDataBinding

    private var mAuth: FirebaseAuth? = null

    private val dateHelper = DateHelper()
    private val startDate = dateHelper.getWeekStartDate()
    private val endDate = dateHelper.getWeekEndDate()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = MyDataBinding.inflate(inflater, container, false)
        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        mAuth = FirebaseAuth.getInstance()

        binding.btnInsertGlucose.setOnClickListener{
            insertGlucose()
        }

        binding.btnInsertSmartDeviceData.setOnClickListener{
            insertSmartData()
        }

        binding.tabLayout!!.addTab(binding.tabLayout!!.newTab())
        binding.tabLayout!!.addTab(binding.tabLayout!!.newTab())
        binding.tabLayout!!.addTab(binding.tabLayout!!.newTab())
        binding.tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = MyAdapter(this, childFragmentManager, binding.tabLayout!!.tabCount)
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

        setDateTxt()

        binding.imgArrowBack.setOnClickListener{
            startDate?.add(Calendar.DATE, -7)
            endDate?.add(Calendar.DATE, -7)
            setDateTxt()
            dataViewModel.getFilteredGlucoseData(mAuth?.currentUser?.uid, startDate!!.time, endDate!!.time)
            dataViewModel.getSmartDeviceData(mAuth?.currentUser?.uid, startDate!!.time, endDate!!.time)


        }

        binding.imgArrowNext.setOnClickListener{
            startDate?.add(Calendar.DATE, 7)
            endDate?.add(Calendar.DATE, 7)
            setDateTxt()
            dataViewModel.getFilteredGlucoseData(mAuth?.currentUser?.uid, startDate!!.time, endDate!!.time)
            dataViewModel.getSmartDeviceData(mAuth?.currentUser?.uid, startDate!!.time, endDate!!.time)
        }
        return binding.root
    }

    fun getFromDate() : Date?{
        return startDate?.time
    }

    fun getToDate() : Date?{
        return endDate?.time
    }

    private fun setDateTxt(){
        val format = SimpleDateFormat("yyyy/MM/dd")

        var formatedStartDate = format.format(startDate!!.time)
        var formatedEndDate = format.format(endDate!!.time)

        binding.txtFromDate.text = "$formatedStartDate"
        binding.txtToDate.text = "$formatedEndDate"
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