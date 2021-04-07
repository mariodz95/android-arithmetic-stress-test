package com.example.arithmeticstresstest.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arithmeticstresstest.adapter.TestRecyclerViewAdapter
import com.example.arithmeticstresstest.databinding.FragmentStressTestResultsBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import org.koin.android.ext.android.inject


class StressTestResults : Fragment() {
    private lateinit var binding: FragmentStressTestResultsBinding

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStressTestResultsBinding.inflate(inflater, container, false)

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        binding.rvResults.layoutManager = LinearLayoutManager(this.context)

        dataViewModel.getStressTestResults()
        dataViewModel.testResults?.observe(viewLifecycleOwner, Observer {
            binding.rvResults.adapter =  TestRecyclerViewAdapter(it.sortedByDescending { it.dateInserted })
        })

        return binding.root

    }


}