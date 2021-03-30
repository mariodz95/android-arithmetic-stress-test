package com.example.arithmeticstresstest.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository

class DataViewModel(private val repository: FirebaseRepository): ViewModel(){
    var data : MutableLiveData<List<TestData>>? = null

    fun insertTestData(testData: TestData, uid: String?){
        repository.insertTestData(testData, uid)
    }

    fun getAllTestData(uid: String?) : MutableLiveData<List<TestData>>?{
        data = repository.getAllTestData(uid)
        return data
    }
}