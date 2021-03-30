package com.example.arithmeticstresstest.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.arithmeticstresstest.model.ProfileData
import com.example.arithmeticstresstest.model.TestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class FirebaseRepository {
    var testData: MutableLiveData<List<TestData>> = MutableLiveData<List<TestData>>()
    var profileData: MutableLiveData<List<ProfileData>> = MutableLiveData<List<ProfileData>>()

    fun insertTestData(testData: TestData, uid: String?){
        val db = Firebase.firestore
        db.collection("testData").document(uid!!).collection("information").add(testData)
    }

    fun getAllTestData(uid: String?) : MutableLiveData<List<TestData>>? {
        val db = Firebase.firestore
        val data = mutableListOf<TestData>()

        db.collection("testData").document(uid!!).collection("information")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val testData = document.toObject<TestData>()
                    data.add(testData)
                }
                val sortedData = data.sortedBy { it.insertedDate }
                testData.postValue(sortedData);
            }
            .addOnFailureListener { exception ->
                Log.w("sada", "Error getting documents: ", exception)
            }
        return  testData
    }

    fun getProfileData(userId: String?) : MutableLiveData<List<ProfileData>>?
    {
        val db = Firebase.firestore
        val data = mutableListOf<ProfileData>()

        db.collection("personalInformation").document(userId!!).collection("information")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val profileData = document.toObject<ProfileData>()
                    data.add(profileData)
                }
                profileData.postValue(data);
            }
            .addOnFailureListener { exception ->
                Log.w("sada", "Error getting documents: ", exception)
            }
        return  profileData
    }
}