package com.example.arithmeticstresstest.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository

class DataViewModel(private val repository: FirebaseRepository): ViewModel(){
    var data : MutableLiveData<List<TestData>>? = null


    fun insertBeforeOrAfterTest(testData: TestData, uid: String?){
        repository.insertBeforeOrAfterTest(testData, uid)
    }

    fun insertTestScoreResult(testScore: TestScore, uid: String?){
        repository.insertTestScoreResult(testScore, uid)
    }


    fun getAllBeforeAndAfterTestResults(uid: String?) : MutableLiveData<List<TestData>>?{
        data = repository.getAllBeforeAndAfterTestResults(uid)
        return data
    }
}