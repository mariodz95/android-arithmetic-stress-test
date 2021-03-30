package com.example.arithmeticstresstest.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository

class ProfileViewModel(private val repository: FirebaseRepository) : ViewModel() {
    var profileData : MutableLiveData<List<ProfileData>>? = null


    fun getProfileData(userId: String?){
        profileData = repository.getProfileData(userId)
    }
}