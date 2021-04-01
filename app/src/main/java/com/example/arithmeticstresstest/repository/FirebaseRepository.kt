package com.example.arithmeticstresstest.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.arithmeticstresstest.model.GlucoseLevel
import com.example.arithmeticstresstest.model.ProfileData
import com.example.arithmeticstresstest.model.SmartDevice
import com.example.arithmeticstresstest.model.TestData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*


class FirebaseRepository {
    var testData: MutableLiveData<List<TestData>> = MutableLiveData<List<TestData>>()
    var profileData: MutableLiveData<ProfileData>?= MutableLiveData<ProfileData>()
    var glucoseLevels: MutableLiveData<List<GlucoseLevel>> = MutableLiveData<List<GlucoseLevel>>()
    var filteredGlucoseData: MutableLiveData<List<GlucoseLevel>> = MutableLiveData<List<GlucoseLevel>>()

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

    fun getProfileData(userId: String?) : MutableLiveData<ProfileData>?
    {
        val db = Firebase.firestore
        val data = mutableListOf<ProfileData>()

        val docRef = db.collection("personalInformation").document(userId!!)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val profile = document.toObject<ProfileData>()
                        profileData!!.postValue( profile)

                    } else {
                    }
                }
                .addOnFailureListener { exception ->
                }
        return  profileData
    }

    fun getGlucoseLevelData(userId: String?): MutableLiveData<List<GlucoseLevel>>?{
        val db = Firebase.firestore
        val data = mutableListOf<GlucoseLevel>()

        db.collection("glucoseLevels").document(userId!!).collection("levels")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val glucoseLevel = document.toObject<GlucoseLevel>()
                    data.add(glucoseLevel)
                }
                glucoseLevels.postValue(data);
            }
            .addOnFailureListener { exception ->
                Log.w("sada", "Error getting documents: ", exception)
            }
        return  glucoseLevels
    }

    fun getFilteredGlucoseData(userId: String?, startDate: Date, endDate: Date ): MutableLiveData<List<GlucoseLevel>>?{
        val db = Firebase.firestore
        val data = mutableListOf<GlucoseLevel>()

        db.collection("glucoseLevels").document(userId!!).collection("levels")
            .whereLessThanOrEqualTo("date", endDate)
                .whereGreaterThanOrEqualTo("date", startDate)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val glucoseLevel = document.toObject<GlucoseLevel>()
                    data.add(glucoseLevel)
                }
                glucoseLevels.postValue(data)
            }
            .addOnFailureListener { exception ->
                Log.w("sada", "Error getting documents: ", exception)
            }
        return  glucoseLevels
    }

    fun insertGlucoseLevel(userId: String?, glucoseLevel: GlucoseLevel){
        val db = Firebase.firestore
        db.collection("glucoseLevels").document(userId!!).collection("levels").add(glucoseLevel)
    }

    fun insertSmartDeviceData(userId: String?, smartDevice: SmartDevice){
        val db = Firebase.firestore
        db.collection("smartDevice").document(userId!!).collection("data").add(smartDevice)
    }

    fun saveProfileData(userId: String?, profileData: ProfileData){
        val db = Firebase.firestore
        db.collection("personalInformation").document(userId!!).set(profileData)
    }
}