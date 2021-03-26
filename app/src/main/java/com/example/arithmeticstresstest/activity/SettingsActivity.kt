package com.example.arithmeticstresstest.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.actionbar_layout_with_arrow);
        }

        val prefs = getSharedPreferences("DURATION_TIME", AppCompatActivity.MODE_PRIVATE)
        val durationTime = prefs?.getLong("duration", 180000)!!

        when (durationTime) {
            180000.toLong() -> {
                val rb1 : RadioButton= findViewById(R.id.radioThree)
                rb1.isChecked = true
            }
            300000.toLong() -> {
                val rb2 : RadioButton= findViewById(R.id.radioFive)
                rb2.isChecked = true
            }
            else -> {
                val rb3 : RadioButton= findViewById(R.id.radioSeven)
                rb3.isChecked = true
            }
        }

        val prefsNumberTime = getSharedPreferences(
            "NUMBER_RESET_TIME",
            AppCompatActivity.MODE_PRIVATE
        )
        val  numberResetTime = prefsNumberTime?.getLong("numberResetTime", 7000)!!

        when (numberResetTime) {
            3000.toLong() -> {
                binding.testSpinner.setSelection(2)
            }
            5000.toLong() -> {
                binding.testSpinner.setSelection(1)
            }
            else -> {
                binding.testSpinner.setSelection(2)
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.test_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.testSpinner.adapter = adapter
        }

        binding.testSpinner.onItemSelectedListener = this
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioThree ->
                    if (checked) {
                        val editor = getSharedPreferences("DURATION_TIME", MODE_PRIVATE).edit()
                        editor.putLong("duration", 180000)
                        editor.apply()
                    }
                R.id.radioFive ->
                    if (checked) {
                        val editor = getSharedPreferences("DURATION_TIME", MODE_PRIVATE).edit()
                        editor.putLong("duration", 300000)
                        editor.apply()
                    }
                R.id.radioSeven ->
                    if (checked) {
                        val editor = getSharedPreferences("DURATION_TIME", MODE_PRIVATE).edit()
                        editor.putLong("duration", 420000)
                        editor.apply()
                    }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.v("test", "test ${parent?.getItemAtPosition(position)}")
        when {
            parent?.getItemAtPosition(position) == "Easy" -> {
                val editor = getSharedPreferences("NUMBER_RESET_TIME", MODE_PRIVATE).edit()
                editor.putLong("numberResetTime", 7000)
                editor.apply()
            }
            parent?.getItemAtPosition(position) == "Medium" -> {
                val editor = getSharedPreferences("NUMBER_RESET_TIME", MODE_PRIVATE).edit()
                editor.putLong("numberResetTime", 5000)
                editor.apply()
            }
            else -> {
                val editor = getSharedPreferences("NUMBER_RESET_TIME", MODE_PRIVATE).edit()
                editor.putLong("numberResetTime", 3000)
                editor.apply()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}