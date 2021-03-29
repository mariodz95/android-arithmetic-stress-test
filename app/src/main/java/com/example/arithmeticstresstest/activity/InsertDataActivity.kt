package com.example.arithmeticstresstest.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.R
import com.example.arithmeticstresstest.databinding.ActivityInsertDataBinding
import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.model.TestData
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.util.*


class InsertDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertDataBinding

    private val repository : FirebaseRepository by inject()
    private lateinit var dataViewModel: DataViewModel

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance();

        var factory = DataViewModelFactory(repository)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.actionbar_layout_with_arrow);
        }

        binding.btnSubmit.setOnClickListener{
            getValue()
        }

    }

    private fun getValue(){
        var glucoseLevel : String = binding.editTxtGlucose.text.toString().trim()
        var editTextHeartRate : String? = binding.editTextHeartRate.text.toString().trim()
        var editTextStressLevel : String? = binding.editTextStressLevel.text.toString().trim()

        if(glucoseLevel == ""){
            binding.editTxtGlucose.error = "Glucose level is required"
            binding.editTxtGlucose.requestFocus()
            return
        }

        var heartRate: Int? = null
        if(editTextHeartRate != "") {
            heartRate = editTextHeartRate?.toInt()
        }

        var stressLevel: Int? = null
        if(editTextStressLevel != ""){
            stressLevel = editTextStressLevel?.toInt()
        }

        val currentTime: Date = Calendar.getInstance().time
        val testData = TestData(glucoseLevel.toFloat(), heartRate, stressLevel, currentTime,"BeforeTest")
        dataViewModel.insertBeforeOrAfterTest(testData, mAuth?.currentUser?.uid)
        openTestActivity()
    }

    private fun openTestActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}