package com.example.arithmeticstresstest.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.databinding.ActivityGlucoseFormBinding
import com.example.arithmeticstresstest.fragment.DatePickerFragment
import com.example.arithmeticstresstest.fragment.TimePickerFragment
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.GlucoseLevel
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class GlucoseFormActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityGlucoseFormBinding
    var glucoseType: String = ""

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel

    private var mAuth: FirebaseAuth? = null

    var minute: Int = 0
    var hour: Int = 0

    var date: Date? = null
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlucoseFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        ArrayAdapter.createFromResource(
                this,
                R.array.type,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.typeSpinner.adapter = adapter
        }

        binding.typeSpinner.onItemSelectedListener = this

        binding.btnSubmit.setOnClickListener {
            insertGlucoseLevel()
        }

        binding.editTxtTime.setOnClickListener{
            showTimePickerDialog()
        }

        binding.editTextDate.setOnClickListener{
            showDatePickerDialog()
        }
    }

    private fun insertGlucoseLevel() {
        val glucoseLevel = binding.editTxtGlucose.text.toString().trim()

        if (glucoseLevel.isEmpty()) {
            binding.editTxtGlucose.error = "Glucose level is required"
            binding.editTxtGlucose.requestFocus()
            return
        }

        if(glucoseType == "" || glucoseType == "Chose"){
            (binding.typeSpinner.getChildAt(0) as TextView).error = "Please select!"
            return
        }

        if(binding.editTextDate.text.toString().trim() == ""){
            binding.editTextDate.error = "Enter date!"
            binding.editTextDate.requestFocus()
            return
        }

        if(binding.editTxtTime.text.toString().trim() == ""){
            binding.editTxtTime.error = "Enter time!"
            binding.editTxtTime.requestFocus()
            return
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val dateTime = calendar.time

        var glucoseObject: GlucoseLevel = GlucoseLevel(glucoseLevel.toFloat(), glucoseType, dateTime)

        dataViewModel.insertGlucoseLevel(mAuth?.currentUser?.uid, glucoseObject)

        openDataActivity()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        glucoseType = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun openDataActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showTimePickerDialog() {
        var dialog = TimePickerFragment()

        dialog.setOnChangeListener(object : TimePickerFragment.OnChangeListener {
            override fun onChange(hours: Int, minutes: Int) {
                hour = hours
                minute = minutes
                binding.editTxtTime.setText("$hours hour, $minutes minutes")
            }
        })

        dialog.show(supportFragmentManager, "timePicker")
    }

    private fun showDatePickerDialog() {
        var dialog = DatePickerFragment()

        dialog.setOnChangeListener(object : DatePickerFragment.OnChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onChange(year: Int, month: Int, day: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                date = calendar.time

                val format = SimpleDateFormat("yyyy/MM/dd")
                var formatedDate = format.format(date)

                binding.editTextDate.setText("$formatedDate")
            }
        })
        dialog.show(supportFragmentManager, "timePicker")
    }
}