package com.example.arithmeticstresstest.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository

class DataViewModel(private val repository: FirebaseRepository): ViewModel(){

    fun insertBeforeOrAfterTest(testData: TestData, uid: String?){
        Log.v("test", "test $testData")
        repository.insertBeforeOrAfterTest(testData, uid)
    }
}