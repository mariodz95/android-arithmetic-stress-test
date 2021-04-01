package com.example.arithmeticstresstest.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arithmeticstresstest.repository.FirebaseRepository

class ProfileViewModel(private val repository: FirebaseRepository) : ViewModel() {
    var profileData : MutableLiveData<ProfileData>? = null

    fun getProfileData(userId: String?){
        profileData = repository.getProfileData(userId)
    }

    fun saveProfileData(userId: String?, profileData: ProfileData){
        repository.saveProfileData(userId, profileData)
    }
}