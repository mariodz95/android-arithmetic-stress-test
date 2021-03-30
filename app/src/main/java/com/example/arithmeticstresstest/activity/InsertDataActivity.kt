package com.example.arithmeticstresstest.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.TextKeyListener.clear
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
import com.google.gson.Gson
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


        val type: String? = intent.getStringExtra("TYPE")

        if(type == "BeforeTest"){
            val testData = TestData(
                    glucoseLevel.toFloat(),
                    null,
                    heartRate,
                    null,
                    stressLevel,
                    null,
                    null,
                    null,
                    null,
                    null)

            val mPrefs = getPreferences(MODE_PRIVATE)

            val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
            val gson = Gson()
            val json = gson.toJson(testData)
            prefsEditor.putString("MY_OBJECT_BEFORE_TEST", json)
            prefsEditor.commit()

            openTestActivity()
        }else{
            val gson = Gson()
            val mPrefs = getPreferences(MODE_PRIVATE)
            val json: String? = mPrefs.getString("MY_OBJECT_BEFORE_TEST", "")
            var testData: TestData? = TestData()

            if(json != ""){
                testData= gson.fromJson(json, TestData::class.java)
                mPrefs.edit().remove("MY_OBJECT_BEFORE_TEST").commit()
            }
            val currentTime: Date = Calendar.getInstance().time
         /*   val prefs = getSharedPreferences(
                "DURATION_TIME",
                AppCompatActivity.MODE_PRIVATE
            )
            testData?.testDuration = prefs?.getLong("duration", 180000)!!

            val prefsNumberTime = this.activity?.getSharedPreferences(
                "NUMBER_RESET_TIME",
                AppCompatActivity.MODE_PRIVATE
            )
            timeLeftInMillisForNumber =
                prefsNumberTime?.getLong("numberResetTime", 7000)!!*/


            testData?.glucoseLevelAfterTest = glucoseLevel.toFloat()
            testData?.heartRateAfterTest = heartRate
            testData?.stressLevelAfterTest = stressLevel
            val userResult: Int = intent.getIntExtra("TEST_RESULT", 0)
            val numberOfCalculations: Int = intent.getIntExtra("NUMBER_OF_CALCULATIONS", 0)
            testData?.insertedDate = currentTime
            testData?.testResult = userResult
            testData?.testQuestions = numberOfCalculations
            testData?.userId = mAuth?.currentUser?.uid

            dataViewModel.insertTestData(testData!!, mAuth?.currentUser?.uid)
            openTestActivity()
        }
    }

    private fun openTestActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}