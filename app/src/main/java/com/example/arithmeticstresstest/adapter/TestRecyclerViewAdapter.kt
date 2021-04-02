package com.example.arithmeticstresstest.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arithmeticstresstest.databinding.ResultRowLayoutBinding
import com.example.arithmeticstresstest.model.StressTestResult
import java.util.*

class TestRecyclerViewAdapter(
    private val historyItems: List<StressTestResult>?
) : RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {
    private lateinit var binding: ResultRowLayoutBinding

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ResultRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val view = binding!!.root
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(historyItems!![position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return historyItems!!.size
    }

    //the class is holding the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(stressTestResult: StressTestResult) {
            binding.txtName.text = "${stressTestResult.userEmail}: "
            binding.txtResult.text = "${stressTestResult.testScore!!} / ${stressTestResult.testQuestions!!}"
            binding.txtDate.text = "${DateFormat.format("yyyy-MM-dd hh:mm:ss a", stressTestResult.dateInserted)}"
        }
    }
}