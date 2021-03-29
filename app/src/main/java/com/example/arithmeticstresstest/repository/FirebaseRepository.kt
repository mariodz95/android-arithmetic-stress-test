package com.example.arithmeticstresstest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.arithmeticstresstest.model.TestData
import com.example.arithmeticstresstest.model.TestScore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class FirebaseRepository {
    var listMutableLiveData: MutableLiveData<List<TestData>> =
        MutableLiveData<List<TestData>>()

    fun insertBeforeOrAfterTest(testData: TestData, uid: String?){
        val db = Firebase.firestore
        db.collection("testData").document(uid!!).collection("measurement").add(testData)
    }

    fun insertTestScoreResult(testScore: TestScore, uid: String?){
        val db = Firebase.firestore
        db.collection("testData").document(uid!!).collection("testScore").add(testScore)
    }


    fun getAllBeforeAndAfterTestResults(uid: String?) : MutableLiveData<List<TestData>>? {
        val db = Firebase.firestore
        val data = mutableListOf<TestData>()

        db.collection("testData").document(uid!!).collection("measurement")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val testData = document.toObject<TestData>()
                    data.add(testData)
                }
                listMutableLiveData.postValue(data);
            }
            .addOnFailureListener { exception ->
                Log.w("sada", "Error getting documents: ", exception)
            }
        return  listMutableLiveData
    }
}