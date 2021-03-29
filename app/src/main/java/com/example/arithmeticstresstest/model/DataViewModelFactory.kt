package com.example.arithmeticstresstest.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arithmeticstresstest.repository.FirebaseRepository

class DataViewModelFactory constructor(private val repository: FirebaseRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DataViewModel::class.java!!)) {
            DataViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}