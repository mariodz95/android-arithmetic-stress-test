package com.example.arithmeticstresstest.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.databinding.ActivitySmartDeviceFormBinding
import com.example.arithmeticstresstest.fragment.DatePickerFragment
import com.example.arithmeticstresstest.fragment.TimePickerFragment
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.GlucoseLevel
import com.example.arithmeticstresstest.model.SmartDevice
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class SmartDeviceFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmartDeviceFormBinding

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel

    private var mAuth: FirebaseAuth? = null

    var minute: Int = 0
    var hour: Int = 0

    var date: Date? = null
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmartDeviceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        binding.btnSubmit.setOnClickListener {
            insertSmartWatchData()
        }

        binding.editTxtTime.setOnClickListener{
            showTimePickerDialog()
        }

        binding.editTextDate.setOnClickListener{
            showDatePickerDialog()
        }
    }

    private fun insertSmartWatchData() {
        val calories = binding.editTxtCalories.text.toString().trim()
        val steps = binding.editTxtSteps.text.toString().trim()
        val distance = binding.editTxtDistance.text.toString().trim()
        val heartRate = binding.editTextHeartRate.text.toString().trim()
        val duration = binding.edtTxtDuration.text.toString().trim()

        if (duration.isEmpty()) {
            binding.editTextHeartRate.error = "Duration time is required"
            binding.editTextHeartRate.requestFocus()
            return
        }

        if (calories.isEmpty()) {
            binding.editTxtCalories.error = "Calories are required"
            binding.editTxtCalories.requestFocus()
            return
        }

        if (steps.isEmpty()) {
            binding.editTxtSteps.error = "Steps are required"
            binding.editTxtSteps.requestFocus()
            return
        }

        if (distance.isEmpty()) {
            binding.editTxtDistance.error = "Distance are required"
            binding.editTxtDistance.requestFocus()
            return
        }

        if (heartRate.isEmpty()) {
            binding.editTextHeartRate.error = "Average heart rate are required"
            binding.editTextHeartRate.requestFocus()
            return
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val dateTime = calendar.time

        var smartDevice: SmartDevice = SmartDevice(calories.toInt(), steps.toInt(), distance.toInt(), heartRate.toInt(), dateTime, duration.toInt())

        dataViewModel.insertSmartDeviceData(mAuth?.currentUser?.uid, smartDevice)

        openHomeActivity()
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

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}