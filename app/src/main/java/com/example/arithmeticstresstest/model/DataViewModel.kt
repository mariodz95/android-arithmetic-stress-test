package com.example.arithmeticstresstest.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class DataViewModel(private val repository: FirebaseRepository): ViewModel(){
    var data : MutableLiveData<List<TestData>>? = null
    var glucoseLevels : MutableLiveData<List<GlucoseLevel>>? = null
    var filteredGlucoseLevels : MutableLiveData<List<GlucoseLevel>>? = null
    var testResults: MutableLiveData<List<StressTestResult>>? = null
    var smartDeviceData: MutableLiveData<List<SmartDevice>>? = null

    fun getStressTestResults() :MutableLiveData<List<StressTestResult>>? {
        testResults = repository.getStressTestResults()
        return testResults
    }

    fun getSmartDeviceData(userId: String?, startDate: Date, endDate: Date ){
        smartDeviceData = repository.getSmartDeviceData(userId, startDate, endDate)
    }

    fun insertTestData(testData: TestData, uid: String?){
        repository.insertTestData(testData, uid)
    }

    fun getAllTestData(userId: String?){
        data = repository.getAllTestData(userId)
    }

    fun getGlucoseLevels(userId: String?){
        glucoseLevels = repository.getGlucoseLevelData(userId)
    }

    fun saveTestResult(stressTestResult: StressTestResult){
        repository.saveTestResult(stressTestResult)
    }

    fun getFilteredGlucoseData(userId: String?, startDate: Date, endDate: Date ){
        filteredGlucoseLevels = repository.getFilteredGlucoseData(userId, startDate, endDate)
    }

    fun insertGlucoseLevel(userId: String?, glucoseLevel: GlucoseLevel){
        repository.insertGlucoseLevel(userId, glucoseLevel)
    }

    fun insertSmartDeviceData(userId: String?, smartDevice: SmartDevice){
        repository.insertSmartDeviceData(userId, smartDevice)
    }
}